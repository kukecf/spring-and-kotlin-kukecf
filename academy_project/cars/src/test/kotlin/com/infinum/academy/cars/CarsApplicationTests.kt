package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.infinum.academy.cars.repository.CarCheckUpDto
import com.infinum.academy.cars.repository.CarDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity.post
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.servlet.function.RequestPredicates.contentType


@SpringBootTest
@AutoConfigureMockMvc
class CarsApplicationTests() {

    @Autowired
    private lateinit var mvc: MockMvc
    private val mapper: ObjectMapper = ObjectMapper()

    @Test
    @DisplayName("should generate basic homepage")
    fun test1() {
        mvc.get("/").andExpect {
            content { equals("Welcome to my autobody shop! :)") }
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add car and check its addition")
    @DirtiesContext
    fun test2() {
        val car = CarDto(1, "Peugeot", "305", 2004, "2")
        mvc.post("/addCar") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }.toString().contains("added this car")
        }
    }

    @Test
    @DisplayName("should add car and check its details")
    @DirtiesContext
    fun test3() {
        val car = CarDto(1, "Peugeot", "305", 2004, "2")
        mvc.post("/addCar") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }.toString().contains("added this car")
        }

        mvc.get("/details/{id}", 1).andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }.toString().contains(mapper.writeValueAsString(car))
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should try adding two cars with same serial number and fail")
    @DirtiesContext
    fun test4() {
        val car1 = CarDto(1, "Peugeot", "305", 2004, "2")
        val car2 = CarDto(2, "Fiat", "Punto", 2007, "2")
        mvc.post("/addCar") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
        mvc.post("/addCar") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    @Test
    @DisplayName("should add second car and not alter details of first")
    @DirtiesContext
    fun test5() {
        val car1 = CarDto(1, "Peugeot", "305", 2004, "2")
        val car2 = CarDto(2, "Fiat", "Punto", 2007, "4")
        mvc.post("/addCar") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
        mvc.post("/addCar") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        mvc.get("/details/{id}", 1).andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }.toString().contains(mapper.writeValueAsString(car1)) &&
                    content().toString().contains(mapper.writeValueAsString(car2)).not()
            status { is2xxSuccessful() }
        }

        mvc.get("/details/{id}", 2).andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }.toString().contains(mapper.writeValueAsString(car2)) &&
                    content().toString().contains(mapper.writeValueAsString(car1)).not()
            status { is2xxSuccessful() }
        }
    }

    @Test
    @DisplayName("should add car and checkup for it")
    @DirtiesContext
    fun test6() {
        val car = CarDto(3, "Peugeot", "305", 2004, "2")
        val checkup = CarCheckUpDto("Josip", 2f, 1)
        mvc.post("/addCar") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
        mvc.post("/addCarCheckUp") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    @Test
    @DisplayName("should fail in adding checkup for nonexisting car")
    fun test7() {
        val checkup = CarCheckUpDto("Josip", 2f, 4)
        mvc.post("/addCarCheckUp") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    @Test
    @DisplayName("should generate list of checkups for the same car")
    fun test8() {
        val car = CarDto(3, "Peugeot", "305", 2004, "2")
        val checkup1 = CarCheckUpDto("Josip", 2f, 1)
        val checkup2 = CarCheckUpDto("Stef", 2f, 1)
        val regex = """\"checkups\": [(^{)+{(^})+},(^{)+{(^})+}]""".toRegex()
        mvc.post("/addCar") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
        mvc.post("/addCarCheckUp") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup1)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
        mvc.post("/addCarCheckUp") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(checkup2)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
        mvc.get("/details/{id}",1).andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }.toString().contains(regex)
        }
    }
}
