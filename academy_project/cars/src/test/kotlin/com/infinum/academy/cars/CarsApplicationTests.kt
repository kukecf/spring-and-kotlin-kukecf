package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.cars.dto.CarCheckUpDto
import com.infinum.academy.cars.dto.CarDto
import com.infinum.academy.cars.dto.toCarCheckUp
import io.mockk.InternalPlatformDsl.toStr
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.Rollback
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


@SpringBootTest
@AutoConfigureMockMvc
@Rollback
class CarsApplicationTests @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {

    @Test
    @DisplayName("should return all cars")
    @Transactional
    fun test1() {
        val car = CarDto(1, "Peugeot", "305", 2004, "89")
        val car2 = CarDto(4, "Dacia", "Sandero", 2010, "98")
        val result1 = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id1 = result1.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost:8080/cars/created/")

        val result2 = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id2 = result2.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost:8080/cars/created/")

        mvc.get("/cars").andExpect{
            status { is2xxSuccessful() }
            content{mapper.writeValueAsString(listOf(car.toCarCheckUp(),car2.toCarCheckUp()))}
        }
    }

    @Test
    @DisplayName("should add car and check its addition")
    @Transactional
    fun test2() {
        val car = CarDto(1, "Peugeot", "305", 2004, "89")

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
    fun test3() {
        val car = CarDto(1, "Peugeot", "305", 2004, "89")

        val result = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id = result.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost:8080/cars/created/")

        mvc.get("/cars/{id}", id).andExpect {
            jsonPath("$.id") { value(id) }
            jsonPath("$.ownerId") { value("1") }
            jsonPath("$.dateAdded") { value(LocalDate.now().toStr()) }
            jsonPath("$.manufacturerName") { value("Peugeot") }
            jsonPath("$.modelName") { value("305") }
            jsonPath("$.productionYear") { value("2004") }
            jsonPath("$.serialNumber") { value("89") }
            jsonPath("$.checkUps") { isArray() }
            content { contentType(MediaType.APPLICATION_JSON) }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add second car and not alter details of first")
    @Transactional
    fun test4() {
        val car1 = CarDto(1, "Peugeot", "305", 2004, "889")
        val car2 = CarDto(2, "Fiat", "Punto", 2007, "988")

        val result1 = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id1 = result1.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost:8080/cars/created/")

        val result2 = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id2 = result2.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost:8080/cars/created/")

        mvc.get("/cars/{id}", id1).andExpect {
            jsonPath("$.id") { value(id1) }
            jsonPath("$.ownerId") { value("1") }
            jsonPath("$.dateAdded")
            jsonPath("$.manufacturerName") { value("Peugeot") }
            jsonPath("$.modelName") { value("305") }
            jsonPath("$.productionYear") { value("2004") }
            jsonPath("$.serialNumber") { value("889") }
            jsonPath("$.checkUps")
            content { contentType(MediaType.APPLICATION_JSON) }
            status { is2xxSuccessful() }
        }

        mvc.get("/cars/{id}", id2).andExpect {
            jsonPath("$.id") { value(id2) }
            jsonPath("$.ownerId") { value("2") }
            jsonPath("$.dateAdded")
            jsonPath("$.manufacturerName") { value("Fiat") }
            jsonPath("$.modelName") { value("Punto") }
            jsonPath("$.productionYear") { value("2007") }
            jsonPath("$.serialNumber") { value("988") }
            jsonPath("$.checkUps")
            content { contentType(MediaType.APPLICATION_JSON) }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add car and checkup for it")
    @Transactional
    fun test5() {
        val car = CarDto(3, "Peugeot", "305", 2004, "89")


        val result=mvc.post("/cars") {
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
            .removePrefix("http://localhost:8080/cars/created/")

        val checkup = CarCheckUpDto("Josip", 2f, id.toLong())

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
    @DisplayName("should generate list of checkups for the same car")
    @Transactional
    fun test6() {
        val car = CarDto(3, "Peugeot", "305", 2004, "89")


        val result = mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id = result.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost:8080/cars/created/")

        val checkup1 = CarCheckUpDto("Josip", 2f, id.toLong())
        val checkup2 = CarCheckUpDto("Stef", 2f, id.toLong())

        val result2 = mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        assert(result2.response.getHeaderValue("Location").toStr().contains("http://localhost:8080/checkups/created/"))

        val result3 = mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        assert(result3.response.getHeaderValue("Location").toStr().contains("http://localhost:8080/checkups/created/"))

        mvc.get("/cars/{id}", id).andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(id) }
            jsonPath("$.ownerId") { value("3") }
            jsonPath("$.dateAdded") { value(LocalDate.now().toStr()) }
            jsonPath("$.manufacturerName") { value("Peugeot") }
            jsonPath("$.modelName") { value("305") }
            jsonPath("$.productionYear") { value("2004") }
            jsonPath("$.serialNumber") { value("89") }
            jsonPath("$.checkUps") {
                isArray()
                isNotEmpty()
            }
        }
    }



}