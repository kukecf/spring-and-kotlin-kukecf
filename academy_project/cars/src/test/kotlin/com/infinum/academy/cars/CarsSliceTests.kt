package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.infinum.academy.cars.repository.CarCheckUp
import com.infinum.academy.cars.repository.CarCheckUpNotFoundException
import com.infinum.academy.cars.repository.CarDto
import com.infinum.academy.cars.services.CarService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.internal.bytebuddy.implementation.FixedValue.value
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@WebMvcTest
class CarsSliceTests @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {
    @MockkBean
    lateinit var service: CarService

    @BeforeEach
    fun setUp() {
        every {
            service.getCarCheckUp(1)
        } returns CarCheckUp(
            LocalDateTime.now(),
            "Josip",
            2.1f,
            1
        )

        every {
            service.getCarCheckUp(2)
        } throws CarCheckUpNotFoundException("")

        every {
            service.addCar(any())
        } returns 1

        every {
            service.addCarCheckUp(any())
        } returns 1
    }


    @Test
    @DisplayName("should add and check checkup for car")
    fun test1() {
        mvc.get("/details/{id}", 1).andExpect {
            status { is2xxSuccessful() }
            content {}.toString().contains("added this car:")
        }
    }
}