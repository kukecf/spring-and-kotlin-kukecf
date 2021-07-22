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
import org.springframework.test.annotation.Rollback
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
@AutoConfigureMockMvc
@Rollback
class CarsApplicationTests @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {

    @Test
    @DisplayName("should add car and check its addition")
    @Transactional
    fun test2() {
        val car = CarDto(1, "Peugeot", "305", 2004, "89")

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
    @Transactional
    fun test3() {
        val car = CarDto(1, "Peugeot", "305", 2004, "89")

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }

        mvc.get("/cars/{id}", 1).andExpect {
            jsonPath("$.car.carId") { value("1") }
            jsonPath("$.car.ownerId") { value("1") }
            jsonPath("$.car.dateAdded")
            jsonPath("$.car.manufacturerName") { value("Peugeot") }
            jsonPath("$.car.modelName") { value("305") }
            jsonPath("$.car.productionYear") { value("2004") }
            jsonPath("$.car.serialNumber") { value("89") }
            jsonPath("$.checkUps")
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

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }

        mvc.get("/cars/{id}", 1).andExpect {
            jsonPath("$.car.carId") { value("1") }
            jsonPath("$.car.ownerId") { value("1") }
            jsonPath("$.car.dateAdded")
            jsonPath("$.car.manufacturerName") { value("Peugeot") }
            jsonPath("$.car.modelName") { value("305") }
            jsonPath("$.car.productionYear") { value("2004") }
            jsonPath("$.car.serialNumber") { value("889") }
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
            jsonPath("$.car.serialNumber") { value("988") }
            jsonPath("$.car.checkUps")
            content { contentType(MediaType.APPLICATION_JSON) }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add car and checkup for it")
    @Transactional
    fun test5() {
        val car = CarDto(3, "Peugeot", "305", 2004, "89")
        val checkup = CarCheckUpDto("Josip", 2f, 1)

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }

        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }
    }

    @Test
    @DisplayName("should fail in adding checkup for nonexisting car")
    fun test6() {
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
    @Transactional
    fun test7() {
        val car = CarDto(3, "Peugeot", "305", 2004, "89")
        val checkup1 = CarCheckUpDto("Josip", 2f, 1)
        val checkup2 = CarCheckUpDto("Stef", 2f, 1)

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }

        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }

        mvc.post("/checkups") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
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
            jsonPath("$.car.serialNumber") { value("89") }
            jsonPath("$.car.checkUps")
        }
    }

    @Test
    @DisplayName("should find car by serial number")
    @Transactional
    fun test8() {
        val car = CarDto(3, "Peugeot", "305", 2004, "89")

        mvc.post("/cars") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }

        mvc.get("/cars/serial/89" ).andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.carId") { value("1") }
            jsonPath("$.ownerId") { value("3") }
            jsonPath("$.dateAdded")
            jsonPath("$.manufacturerName") { value("Peugeot") }
            jsonPath("$.modelName") { value("305") }
            jsonPath("$.productionYear") { value("2004") }
            jsonPath("$.serialNumber") { value("89") }
        }
    }

}