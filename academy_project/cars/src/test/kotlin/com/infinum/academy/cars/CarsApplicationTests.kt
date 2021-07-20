package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.cars.resource.CarCheckUpDto
import com.infinum.academy.cars.resource.CarDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions.string
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
class CarsApplicationTests(
    @Autowired private val mapper: ObjectMapper
) {

    @Autowired
    private lateinit var mvc: MockMvc

    @BeforeEach
    fun setUp() {

    }

    @Test
    @DisplayName("should add car and check its addition")
    @DirtiesContext
    fun test2() {
        val car = CarDto(1, "Peugeot", "305", 2004, "2")

        mvc.post("/cars/") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add car and check its details")
    @DirtiesContext
    fun test3() {
        val car = CarDto(1, "Peugeot", "305", 2004, "2")

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header{exists("Location")}
        }

        mvc.get("/cars/{id}", 1).andExpect {
            jsonPath("$.car.carId") { value("1") }
            jsonPath("$.car.ownerId") { value("1") }
            jsonPath("$.car.dateAdded")
            jsonPath("$.car.manufacturerName") { value("Peugeot") }
            jsonPath("$.car.modelName") { value("305") }
            jsonPath("$.car.productionYear") { value("2004") }
            jsonPath("$.car.serialNumber") { value("2") }
            jsonPath("$.checkUps")
            content { contentType(MediaType.APPLICATION_JSON) }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should try adding two cars with same serial number and fail")
    @DirtiesContext
    fun test4() {
        val car1 = CarDto(1, "Peugeot", "305", 2004, "2")
        val car2 = CarDto(2, "Fiat", "Punto", 2007, "2")

        mvc.post("/cars/") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header{exists("Location")}
        }

        mvc.post("/cars/") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isConflict() }
        }
    }

    @Test
    @DisplayName("should add second car and not alter details of first")
    @DirtiesContext
    fun test5() {
        val car1 = CarDto(1, "Peugeot", "305", 2004, "2")
        val car2 = CarDto(2, "Fiat", "Punto", 2007, "4")

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header{exists("Location")}
        }

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header{exists("Location")}
        }

        mvc.get("/cars/{id}", 1).andExpect {
            jsonPath("$.car.carId") { value("1") }
            jsonPath("$.car.ownerId") { value("1") }
            jsonPath("$.car.dateAdded")
            jsonPath("$.car.manufacturerName") { value("Peugeot") }
            jsonPath("$.car.modelName") { value("305") }
            jsonPath("$.car.productionYear") { value("2004") }
            jsonPath("$.car.serialNumber") { value("2") }
            jsonPath("$.checkUps")
            content { contentType(MediaType.APPLICATION_JSON) }
            status { is2xxSuccessful() }
        }

        mvc.get("/cars/{id}", 2).andExpect {
            jsonPath("$.car.carId") { value("2") }
            jsonPath("$.car.ownerId") { value("2") }
            jsonPath("$.car.dateAdded")
            jsonPath("$.car.manufacturerName") { value("Fiat") }
            jsonPath("$.car.modelName") { value("Punto") }
            jsonPath("$.car.productionYear") { value("2007") }
            jsonPath("$.car.serialNumber") { value("4") }
            jsonPath("$.car.checkUps")
            content { contentType(MediaType.APPLICATION_JSON) }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add car and checkup for it")
    @DirtiesContext
    fun test6() {
        val car = CarDto(3, "Peugeot", "305", 2004, "2")
        val checkup = CarCheckUpDto("Josip", 2f, 1)

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header{exists("Location")}
        }

        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header{exists("Location")}
        }
    }

    @Test
    @DisplayName("should fail in adding checkup for nonexisting car")
    fun test7() {
        val checkup = CarCheckUpDto("Josip", 2f, 4)

        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is4xxClientError() }
        }
    }

    @Test
    @DisplayName("should generate list of checkups for the same car")
    @DirtiesContext
    fun test8() {
        val car = CarDto(3, "Peugeot", "305", 2004, "2")
        val checkup1 = CarCheckUpDto("Josip", 2f, 1)
        val checkup2 = CarCheckUpDto("Stef", 2f, 1)
        val regex = """\"checkups\": [(^{)+{(^})+},(^{)+{(^})+}]""".toRegex()

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header{exists("Location")}
        }

        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header{exists("Location")}
        }

        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header{exists("Location")}
        }

        mvc.get("/cars/{id}", 1).andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.car.carId") { value("1") }
            jsonPath("$.car.ownerId") { value("3") }
            jsonPath("$.car.dateAdded")
            jsonPath("$.car.manufacturerName") { value("Peugeot") }
            jsonPath("$.car.modelName") { value("305") }
            jsonPath("$.car.productionYear") { value("2004") }
            jsonPath("$.car.serialNumber") { value("2") }
            jsonPath("$.car.checkUps")
        }
    }
}
