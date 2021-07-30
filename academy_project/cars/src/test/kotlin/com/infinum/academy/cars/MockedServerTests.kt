package com.infinum.academy.cars

import com.infinum.academy.cars.dto.AddCarDto
import io.mockk.InternalPlatformDsl.toStr
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@SpringBootTest
@MockServerTest
@PropertySource("classpath:mockserver.properties")
class MockedServerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {
    lateinit var mockServerClient: MockServerClient

    @BeforeEach
    fun setUp() {
        mockServerClient
            .`when`(
                HttpRequest.request()
                    .withPath("/api/v1/cars")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                        """
                        {
                            "data": [
                                {"manufacturer":"Abarth","model_name":"1000","is_common":0},
                                {"manufacturer":"Abarth","model_name":"1000 Bialbero ","is_common":0},
                                {"manufacturer":"Abarth","model_name":"1000 GT","is_common":0},
                                {"manufacturer":"Abarth","model_name":"1000 TC Corsa","is_common":0},
                                {"manufacturer":"Abarth","model_name":"103 GT","is_common":0},
                                {"manufacturer":"Abarth","model_name":"124","is_common":0},
                                {"manufacturer":"Abarth","model_name":"1300","is_common":0},
                                {"manufacturer":"Abarth","model_name":"1500","is_common":0},
                                {"manufacturer":"Abarth","model_name":"1600","is_common":0},
                                {"manufacturer":"Abarth","model_name":"2000","is_common":0},
                                {"manufacturer":"Abarth","model_name":"205","is_common":0},
                                {"manufacturer":"Abarth","model_name":"207","is_common":0},
                                {"manufacturer":"Abarth","model_name":"208","is_common":0},
                                {"manufacturer":"Abarth","model_name":"209","is_common":0},
                                {"manufacturer":"Abarth","model_name":"210","is_common":0},
                                {"manufacturer":"Abarth","model_name":"2200","is_common":0},
                                {"manufacturer":"Abarth","model_name":"2400","is_common":0},
                                {"manufacturer":"Abarth","model_name":"500","is_common":0},
                                {"manufacturer":"Abarth","model_name":"595","is_common":0},
                                {"manufacturer":"Abarth","model_name":"600","is_common":0},
                                {"manufacturer":"Abarth","model_name":"700","is_common":0},
                                {"manufacturer":"Abarth","model_name":"750","is_common":0}    
                            ]
                        }
                    """.trimIndent()
                    )
            )
    }

    fun `should add car`(){
        val car = AddCarDto(1, 2004, "89", "Abarth", "210")
        val result1 = mvc.post("/cars") {
            contentType = org.springframework.http.MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = org.springframework.http.MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            header { exists("Location") }
        }.andReturn()

        val id1 = result1.response.getHeaderValue("Location").toStr()
            .removePrefix("http://localhost/cars/")

        mvc.get("/cars").andExpect {
            status { is2xxSuccessful() }
            jsonPath("content") { isNotEmpty() }
        }
    }

    fun `should fail to add car because of lacking manufacturer name`(){
        val car = AddCarDto(1, 2004, "89", "Ford", "210")
        mvc.post("/cars") {
            contentType = org.springframework.http.MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = org.springframework.http.MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }

    fun `should fail to add car because of lacking model name`(){
        val car = AddCarDto(1, 2004, "89", "Abarth", "206")
        mvc.post("/cars") {
            contentType = org.springframework.http.MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = org.springframework.http.MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }
}