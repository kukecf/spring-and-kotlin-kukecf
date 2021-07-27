package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.dto.AddCarCheckUpDto
import com.infinum.academy.cars.dto.AddCarDto
import com.infinum.academy.cars.dto.CheckUpDto
import com.infinum.academy.cars.dto.toCar
import com.infinum.academy.cars.repository.CarCheckUpNotFoundException
import com.infinum.academy.cars.repository.CarNotFoundException
import com.infinum.academy.cars.services.CarCheckUpService
import com.infinum.academy.cars.services.CarService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime

@WebMvcTest
@AutoConfigureMockMvc
class CarsSliceTests @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {
    @MockkBean
    lateinit var carService: CarService

    @MockkBean
    lateinit var checkupService : CarCheckUpService

    @BeforeEach
    fun setUp() {
        val car=AddCarDto(
            1,
            "Peugeot",
            "305",
            2002,
            "2"
        ).toCar()
        every {
            checkupService.getCarCheckUp(1)
        } returns CheckUpDto(
            2,
            LocalDateTime.now(),
            "Josip",
            2.1f,
        )

        every {
            checkupService.getCarCheckUp(2)
        } throws CarCheckUpNotFoundException(2)

        every {
            carService.getCarDetails(2)
        } throws CarNotFoundException(2)

        every {
            carService.addCar(any())
        } returns 1

        every {
            checkupService.addCarCheckUp(any())
        } returns 1
    }
/*
    @Test
    fun `should fail to find nonexisting car`() {
        Assertions.assertThatThrownBy {
            carService.getCarDetails(2)
        }.isInstanceOf(CarNotFoundException::class.java)

    }

    @Test
    fun `should fail to find nonexisting checkup`() {
        Assertions.assertThatThrownBy {
            checkupService.getCarCheckUp(2)
        }.isInstanceOf(CarCheckUpNotFoundException::class.java)
    }
    */
}