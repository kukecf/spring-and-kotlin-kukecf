package com.infinum.academy.cars

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.internal.bytebuddy.implementation.FixedValue.value
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class CarsApplicationTests {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    @DisplayName("should generate basic homepage")
    fun test1() {
        mvc.get("/").andExpect {
            status{is2xxSuccessful()}
        }
    }

    @Test
    @DisplayName("should generate basic homepage")
    fun test2() {
        //TODO
        mvc.post("/addCar"){
            param("data")
        }
        mvc.get("/details/{id}",1).andExpect {
            status{is2xxSuccessful()}
        }
    }


}
