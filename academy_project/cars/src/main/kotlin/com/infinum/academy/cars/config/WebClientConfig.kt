package com.infinum.academy.cars.config

import com.infinum.academy.cars.config.properties.CarInfoGetterParams
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CarInfoGetterParams::class)
class WebClientConfig(
    val params: CarInfoGetterParams
) {

    @Bean
    fun webClient(webClientBuilder: WebClient.Builder): WebClient {
        return webClientBuilder
            .baseUrl(params.baseUrl)
            .build()
    }
}
