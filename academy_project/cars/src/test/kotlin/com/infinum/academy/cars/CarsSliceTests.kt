package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.cars.dto.AddCarDto
import com.infinum.academy.cars.dto.CheckUpDto
import com.infinum.academy.cars.dto.toCar
import com.infinum.academy.cars.exceptions.CarCheckUpNotFoundException
import com.infinum.academy.cars.exceptions.CarNotFoundException
import com.infinum.academy.cars.services.CarCheckUpService
import com.infinum.academy.cars.services.CarService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime

@WebMvcTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles=["test"])
class CarsSliceTests @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {
    @MockkBean
    lateinit var carService: CarService

    @MockkBean
    lateinit var checkupService: CarCheckUpService
/*
    @BeforeEach
    fun setUp() {
        val car = AddCarDto(
            1,
            2002,
            "2",
            "Peugeot",
            "305",
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