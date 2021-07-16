package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.cars.services.CarService
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

class CarsSliceTests @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {
    @MockkBean
    lateinit var service: CarService

    @Test
    fun test1(){

    }
}