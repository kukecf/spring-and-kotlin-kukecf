package com.infinum.academy.cars.config

import org.springframework.web.filter.CorsFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {

    @Bean
    fun cors(): CorsFilter {
        val corsConfiguration = CorsConfiguration().apply {
            allowedOriginPatterns = listOf("http://localhost:[*]")
        }

        val corsConfigSource = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfiguration)
        }
        return CorsFilter(corsConfigSource)
    }
}