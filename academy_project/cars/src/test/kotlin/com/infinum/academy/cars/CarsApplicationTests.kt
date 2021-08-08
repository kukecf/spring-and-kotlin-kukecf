package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.cars.dto.AddCarCheckUpDto
import com.infinum.academy.cars.dto.AddCarDto
import com.infinum.academy.cars.services.CarCheckUpService
import com.infinum.academy.cars.services.CarInfoAdministrationService
import com.infinum.academy.cars.services.CarService
import io.mockk.InternalPlatformDsl.toStr
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarsApplicationTests @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
    private val infoService: CarInfoAdministrationService,
    private val carService: CarService,
    private val checkUpService: CarCheckUpService
) {

    private val ids = mutableListOf<Long>()
    @BeforeAll
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun setUp() {

        infoService.saveModelsFromServer()

        val cars = listOf(
            AddCarDto(1, 2004, "89", "Berkeley", "QB"),
            AddCarDto(4, 2010, "98", "Lamborghini", "Aventador"),
            AddCarDto(3, 2004, "899", "Mazda", "CX-7"),
            AddCarDto(1, 2004, "8921", "Ferrari", "California")
        )

        cars.forEach {
            ids.add(carService.addCar(it))
        }

        val checkups = listOf(
            AddCarCheckUpDto("Josip", 2f, ids[0], LocalDateTime.now().minus(Period.ofDays(2))),
            AddCarCheckUpDto("Josip", 2f, ids[1], LocalDateTime.now().minus(Period.ofDays(2))),
            AddCarCheckUpDto("Josip", 2f, ids[2], LocalDateTime.now().minus(Period.ofDays(2))),
            AddCarCheckUpDto("Josip", 2f, ids[2], LocalDateTime.now()),
            AddCarCheckUpDto("Stef", 2f, ids[2], LocalDateTime.now()),
            AddCarCheckUpDto("Josip", 2f, ids[0], LocalDateTime.now().minus(Period.ofDays(2))),
            AddCarCheckUpDto("Marko", 2f, ids[0], LocalDateTime.now().minus(Period.ofDays(2))),
            AddCarCheckUpDto("Ivan", 2f, ids[0], LocalDateTime.now().minus(Period.ofDays(2))),
            AddCarCheckUpDto("Hrvoje", 2f, ids[1], LocalDateTime.now().minus(Period.ofDays(2))),
            AddCarCheckUpDto("Josip", 2f, ids[3], LocalDateTime.now().plus(Period.ofMonths(1))),
            AddCarCheckUpDto("Marko", 2f, ids[3], LocalDateTime.now().plus(Period.ofMonths(1))),
            AddCarCheckUpDto("Ivan", 2f, ids[3], LocalDateTime.now().plus(Period.ofMonths(1))),
            AddCarCheckUpDto("Hrvoje", 2f, ids[3], LocalDateTime.now().plus(Period.ofMonths(1)))
        )

        checkups.forEach {
            checkUpService.addCarCheckUp(it)
        }
    }

    @AfterAll
    fun tearDown(){
        //checkUpService.deleteAll()
        //carService.deleteAll()
        //infoService.deleteModels()
    }

    @Test
    @DisplayName("should return all cars")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test1() {
        mvc.get("/cars").andExpect {
            status { is2xxSuccessful() }
            jsonPath("_embedded.item") {
                isNotEmpty()
                isArray()
            }
            jsonPath("$._links.self.href") { hasJsonPath() }
            jsonPath("$.page.totalElements") { value(4) }
        }
    }

    @Test
    @DisplayName("should add car and check its addition")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test2() {
        val car = AddCarDto(1, 2004, "8900", "Ferrari", "125")

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                is2xxSuccessful()
            }
        }
    }

    @Test
    @DisplayName("should add car and check its details")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test3() {
        val car = AddCarDto(1, 2004, "8900", "Maserati", "150S")

        val result = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id = result.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        mvc.get("/cars/{id}", id).andExpect {
            jsonPath("$.id") { value(id) }
            jsonPath("$.ownerId") { value("1") }
            jsonPath("$.dateAdded") { value(LocalDate.now().toStr()) }
            jsonPath("$.manufacturerName") { value("Maserati") }
            jsonPath("$.modelName") { value("150S") }
            jsonPath("$.productionYear") { value("2004") }
            jsonPath("$.serialNumber") { value("8900") }
            jsonPath("$._links.self") { hasJsonPath() }
            jsonPath("$._links.checkups.href") { value("http://localhost/cars/$id/checkups") }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add second car and not alter details of first")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test4() {
        val car1 = AddCarDto(1, 2004, "889", "Marcos", "Ugly Duckling")
        val car2 = AddCarDto(2, 2007, "988", "Maserati", "Tipo 65")

        val result1 = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id1 = result1.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        val result2 = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id2 = result2.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        mvc.get("/cars/{id}", id1).andExpect {
            jsonPath("$.id") { value(id1) }
            jsonPath("$.ownerId") { value("1") }
            jsonPath("$.dateAdded")
            jsonPath("$.manufacturerName") { value("Marcos") }
            jsonPath("$.modelName") { value("Ugly Duckling") }
            jsonPath("$.productionYear") { value("2004") }
            jsonPath("$.serialNumber") { value("889") }
            jsonPath("$._links.self") { hasJsonPath() }
            jsonPath("$._links.checkups.href") { value("http://localhost/cars/$id1/checkups") }
            status { is2xxSuccessful() }
        }

        mvc.get("/cars/{id}", id2).andExpect {
            jsonPath("$.id") { value(id2) }
            jsonPath("$.ownerId") { value("2") }
            jsonPath("$.dateAdded")
            jsonPath("$.manufacturerName") { value("Maserati") }
            jsonPath("$.modelName") { value("Tipo 65") }
            jsonPath("$.productionYear") { value("2007") }
            jsonPath("$.serialNumber") { value("988") }
            jsonPath("$.checkUps")
            jsonPath("$._links.self") { hasJsonPath() }
            jsonPath("$._links.checkups.href") { value("http://localhost/cars/$id2/checkups") }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add car and checkup for it")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test5() {
        val car = AddCarDto(3, 2004, "8998", "Mazda", "Activehicle")
        val result = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header {
                exists("Location")
            }
        }.andReturn()
        val id = result.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        val checkup = AddCarCheckUpDto("Josip", 2f, id.toLong(), LocalDateTime.now())

        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header {
                exists("Location")
            }
        }
    }

    @Test
    @DisplayName("should generate list of checkups for car")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test6() {
        mvc.get("/cars/{id}/checkups", ids[2]).andExpect {
            status { is2xxSuccessful() }
            jsonPath("$._embedded.item") {
                isArray()
                isNotEmpty()
            }
            jsonPath("$._links.self.href") { hasJsonPath() }
            jsonPath("$.page.totalElements") { value(2) }
        }
    }

    @Test
    @DisplayName("should get latest checkups")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test7() {
        mvc.get("/checkups/latest").andExpect {
            status { is2xxSuccessful() }
            jsonPath("$._embedded.item") {
                isArray()
                isNotEmpty()
            }
        }
    }

    @Test
    @DisplayName("should get checkup by id")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test8() {
        mvc.get("/checkups/{id}", 1).andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.id") { value(1) }
            jsonPath("$.datePerformed") { hasJsonPath() }
            jsonPath("$.workerName") { value("Josip") }
            jsonPath("$.price") { value(2f) }
            jsonPath("$._links.self.href") { hasJsonPath() }
        }
    }

    @Test
    @DisplayName("should schedule a checkup")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test9() {
        val checkupDto1 = AddCarCheckUpDto("Josip", 2f, 2, LocalDateTime.now().plus(Period.ofMonths(6)))

        val result = mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkupDto1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id = result.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/checkups/")

        mvc.get("/checkups/{id}", id).andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.id") { value(id) }
            jsonPath("$.datePerformed") { hasJsonPath() }
            jsonPath("$.workerName") { value("Josip") }
            jsonPath("$.price") { value(2f) }
            jsonPath("$._links.self.href") { hasJsonPath() }
        }
    }

    @Test
    @DisplayName("should generate paged list of upcoming checkups")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test10() {
        mvc.get("/checkups/upcoming").andExpect {
            status { is2xxSuccessful() }
            jsonPath("$._embedded.item") {
                isArray()
                isNotEmpty()
            }
            jsonPath("$._links.self.href") { hasJsonPath() }
            jsonPath("$.page.totalElements") { value(4) }
        }
    }

    @Test
    @DisplayName("should delete car and all involving checkups")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test11() {
        val car = AddCarDto(1, 2004, "897654", "Berkeley", "QB")
        val resultCar = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val idcar = resultCar.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        val checkupDtos = listOf(
            AddCarCheckUpDto("Josip", 2f, idcar.toLong(), LocalDateTime.now().plus(Period.ofMonths(1))),
            AddCarCheckUpDto("Marko", 2f, idcar.toLong(), LocalDateTime.now().plus(Period.ofMonths(1))),
            AddCarCheckUpDto("Ivan", 2f, idcar.toLong(), LocalDateTime.now().plus(Period.ofMonths(1))),
            AddCarCheckUpDto("Hrvoje", 2f, idcar.toLong(), LocalDateTime.now().plus(Period.ofMonths(1)))
        )

        checkupDtos.forEach {
            mvc.post("/checkups") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(it)
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { is2xxSuccessful() }
                header { exists("Location") }
            }
        }

        mvc.delete("/cars/{id}", idcar).andExpect {
            status { isOk() }
        }

        mvc.get("/cars/{id}", idcar).andExpect {
            status { isNotFound() }
        }

        mvc.get("/cars/{id}/checkups", idcar).andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @DisplayName("should delete checkup")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test12() {
        val car = AddCarDto(1, 2004, "891", "Berkeley", "QB")
        val resultcar = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val idCar = resultcar.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        val checkupDto = AddCarCheckUpDto("Josip", 2f, idCar.toLong(), LocalDateTime.now().plus(Period.ofMonths(1)))

        val result = mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkupDto)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val idCheckup =
            result.response.getHeaderValue("Location").toStr()
                .removePrefix("http://localhost/checkups/").toLong()

        mvc.delete("/checkups/{id}", idCheckup).andExpect {
            status { isOk() }
        }

        mvc.get("/checkups/{id}", idCheckup).andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @DisplayName("should generate paged list of existing models")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun test13() {
        mvc.get("/cars/models").andExpect {
            status { is2xxSuccessful() }
            jsonPath("$._embedded.item") {
                isArray()
                isNotEmpty()
            }
        }
    }

    @Test
    @DisplayName("should fail to return all cars for user")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test14() {
        mvc.get("/cars").andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @DisplayName("should add car and check its addition for user")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test15() {
        val car = AddCarDto(1, 2004, "12356", "Ferrari", "125")

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                is2xxSuccessful()
            }
        }
    }

    @Test
    @DisplayName("should add car and check its details")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test16() {
        val car = AddCarDto(1, 2004, "891681261688", "Maserati", "150S")

        val result = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id = result.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        mvc.get("/cars/{id}", id).andExpect {
            jsonPath("$.id") { value(id) }
            jsonPath("$.ownerId") { value("1") }
            jsonPath("$.dateAdded") { value(LocalDate.now().toStr()) }
            jsonPath("$.manufacturerName") { value("Maserati") }
            jsonPath("$.modelName") { value("150S") }
            jsonPath("$.productionYear") { value("2004") }
            jsonPath("$.serialNumber") { value("891681261688") }
            jsonPath("$._links.self") { hasJsonPath() }
            jsonPath("$._links.checkups.href") { value("http://localhost/cars/$id/checkups") }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add second car and not alter details of first")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test17() {
        val car1 = AddCarDto(1, 2004, "889", "Marcos", "Ugly Duckling")
        val car2 = AddCarDto(2, 2007, "988", "Maserati", "Tipo 65")

        val result1 = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id1 = result1.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        val result2 = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id2 = result2.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        mvc.get("/cars/{id}", id1).andExpect {
            jsonPath("$.id") { value(id1) }
            jsonPath("$.ownerId") { value("1") }
            jsonPath("$.dateAdded")
            jsonPath("$.manufacturerName") { value("Marcos") }
            jsonPath("$.modelName") { value("Ugly Duckling") }
            jsonPath("$.productionYear") { value("2004") }
            jsonPath("$.serialNumber") { value("889") }
            jsonPath("$._links.self") { hasJsonPath() }
            jsonPath("$._links.checkups.href") { value("http://localhost/cars/$id1/checkups") }
            status { is2xxSuccessful() }
        }

        mvc.get("/cars/{id}", id2).andExpect {
            jsonPath("$.id") { value(id2) }
            jsonPath("$.ownerId") { value("2") }
            jsonPath("$.dateAdded")
            jsonPath("$.manufacturerName") { value("Maserati") }
            jsonPath("$.modelName") { value("Tipo 65") }
            jsonPath("$.productionYear") { value("2007") }
            jsonPath("$.serialNumber") { value("988") }
            jsonPath("$.checkUps")
            jsonPath("$._links.self") { hasJsonPath() }
            jsonPath("$._links.checkups.href") { value("http://localhost/cars/$id2/checkups") }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should fail to add checkup as user")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test18() {
        val checkup = AddCarCheckUpDto("Josip", 2f, 2, LocalDateTime.now())
        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @DisplayName("should generate list of checkups for car")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test19() {
        mvc.get("/cars/{id}/checkups", 1).andExpect {
            status { is2xxSuccessful() }
            jsonPath("$._links.self.href") { hasJsonPath() }
            jsonPath("$.page.totalElements") { value(5) }
        }
    }

    @Test
    @DisplayName("should fail to get latest checkups as user")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test20() {
        mvc.get("/checkups/latest").andExpect { status { isForbidden() } }
    }

    @Test
    @DisplayName("should fail to schedule a checkup")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test21() {
        val checkupDto1 = AddCarCheckUpDto("Josip", 2f, 2, LocalDateTime.now().plus(Period.ofMonths(6)))
        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkupDto1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @DisplayName("should fail to get checkup by id")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test22() {
        mvc.get("/checkups/{id}", 1).andExpect { status { isForbidden() } }
    }

    @Test
    @DisplayName("should fail to generate paged list of upcoming checkups as user")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test23() {
        mvc.get("/checkups/upcoming").andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @DisplayName("should fail to delete car as user")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test24() {
        val car = AddCarDto(1, 2004, "89", "Berkeley", "QB")
        val resultCar = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val idcar = resultCar.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        mvc.delete("/cars/{id}", idcar).andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @DisplayName("should fail to delete checkup")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test25() {
        mvc.delete("/checkups/{id}", 1).andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @DisplayName("should generate paged list of existing models")
    @Transactional
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun test26() {
        mvc.get("/cars/models").andExpect {
            status { is2xxSuccessful() }
            jsonPath("$._embedded.item") {
                isArray()
                isNotEmpty()
            }
        }
    }

    @Test
    @DisplayName("should fail to return all cars for anon")
    @Transactional
    @WithAnonymousUser
    fun test27() {
        mvc.get("/cars").andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @DisplayName("should fail to add car as anon")
    @Transactional
    @WithAnonymousUser
    fun test28() {
        val car = AddCarDto(1, 2004, "892116916919", "Ferrari", "125")
        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isUnauthorized()
            }
        }
    }

    @Test
    @DisplayName("should fail to check car details as anon")
    @Transactional
    @WithAnonymousUser
    fun test29() {
        mvc.get("/cars/{id}", 1).andExpect { status { isUnauthorized() } }
    }

    @Test
    @DisplayName("should fail to add checkup as anon")
    @Transactional
    @WithAnonymousUser
    fun test30() {
        val checkup = AddCarCheckUpDto("Josip", 2f, 1, LocalDateTime.now())
        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized() }
        }
    }


    @Test
    @DisplayName("should fail to generate list of checkups as anon")
    @Transactional
    @WithAnonymousUser
    fun test31() {
        mvc.get("/cars/{id}/checkups", 1).andExpect { status { isUnauthorized() } }
    }


    @Test
    @DisplayName("should fail to get latest checkups as anon")
    @Transactional
    @WithAnonymousUser
    fun test32() {
        mvc.get("/checkups/latest").andExpect { status { isUnauthorized() } }
    }

    @Test
    @DisplayName("should fail to get checkup by id as anon")
    @Transactional
    @WithAnonymousUser
    fun test33() {
        mvc.get("/checkups/{id}", 1).andExpect { status { isUnauthorized() } }
    }

    @Test
    @DisplayName("should fail to schedule a checkup as anon")
    @Transactional
    @WithAnonymousUser
    fun test34() {
        val checkupDto1 = AddCarCheckUpDto("Josip", 2f, 1, LocalDateTime.now().plus(Period.ofMonths(6)))
        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkupDto1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @DisplayName("should fail to generate paged list of upcoming checkups as anon")
    @Transactional
    @WithAnonymousUser
    fun test35() {
        mvc.get("/checkups/upcoming").andExpect { status { isUnauthorized() } }
    }

    @Test
    @DisplayName("should fail to delete car as anon")
    @Transactional
    @WithAnonymousUser
    fun test36() {
        mvc.delete("/cars/{id}", 1).andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @DisplayName("should fail to delete checkup as anon")
    @Transactional
    @WithAnonymousUser
    fun test37() {
        mvc.delete("/checkups/{id}", 1).andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @DisplayName("should generate paged list of existing models as anon")
    @Transactional
    @WithAnonymousUser
    fun test38() {
        mvc.get("/cars/models").andExpect { status { is2xxSuccessful() } }
    }

}
