package com.infinum.academy.cars.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeRequests {
                authorize(HttpMethod.GET, "/models", permitAll)
                authorize(HttpMethod.POST, "/cars", authenticated)
                authorize(HttpMethod.GET, "/cars", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "/cars/**", authenticated)
                authorize(HttpMethod.GET, "/cars/**/checkups", authenticated)
                authorize(anyRequest, hasAuthority("SCOPE_ADMIN"))
            }
            formLogin {}
            oauth2ResourceServer {
                jwt {}
            }
        }
        return http.build()
    }
}
