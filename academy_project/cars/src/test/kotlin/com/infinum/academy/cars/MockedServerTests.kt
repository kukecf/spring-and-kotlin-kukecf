package com.infinum.academy.cars

import com.infinum.academy.cars.dto.AddCarDto
import io.mockk.InternalPlatformDsl.toStr
import org.junit.jupiter.api.BeforeEach
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
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
                                {"manufacturer":"Abarth","modelName":"1000","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"1000 Bialbero ","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"1000 GT","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"1000 TC Corsa","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"103 GT","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"124","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"1300","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"1500","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"1600","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"2000","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"205","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"207","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"208","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"209","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"210","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"2200","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"2400","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"500","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"595","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"600","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"700","isCommon":0},
                                {"manufacturer":"Abarth","modelName":"750","isCommon":0}    
                            ]
                        }
                    """.trimIndent()
                    )
            )
    }

    fun `should add car`() {
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

    fun `should fail to add car because of lacking manufacturer name`() {
        val car = AddCarDto(1, 2004, "89", "Ford", "210")
        mvc.post("/cars") {
            contentType = org.springframework.http.MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(car)
            accept = org.springframework.http.MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }

    fun `should fail to add car because of lacking model name`() {
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