package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.repository.CarCheckUpNotFoundException
import com.infinum.academy.cars.services.CarCheckUpService
import com.infinum.academy.cars.services.CarService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime

@WebMvcTest
class CarsSliceTests @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {
    @MockkBean
    lateinit var carService: CarService
    lateinit var checkupService : CarCheckUpService

    @BeforeEach
    fun setUp() {
        every {
            checkupService.getCarCheckUp(1)
        } returns CarCheckUp(
            1,
            LocalDateTime.now(),
            "Josip",
            2.1f,
            1
        )

        every {
            checkupService.getCarCheckUp(2)
        } throws CarCheckUpNotFoundException("")

        every {
            carService.addCar(any())
        } returns 1

        every {
            checkupService.addCarCheckUp(any())
        } returns 1
    }

/*
    @Test
    @DisplayName("should add and check checkup for car")
    fun test1() {
        mvc.get("/cars/{id}", 1).andExpect {
            status { is2xxSuccessful() }
            content {}.toString().contains("added this car:")
        }
    }

 */
}