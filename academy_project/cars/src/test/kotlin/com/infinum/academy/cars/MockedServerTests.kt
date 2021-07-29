package com.infinum.academy.cars

import org.junit.jupiter.api.Test
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@SpringBootTest
@AutoConfigureMockMvc
@MockServerTest
@Rollback
class MockedServerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
    ) {
    lateinit var mockServerClient: MockServerClient

    @Test
    fun `mock server`() {
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
                                    
                            ]
                        }
                    """.trimIndent()
                    )
            )
    }
}