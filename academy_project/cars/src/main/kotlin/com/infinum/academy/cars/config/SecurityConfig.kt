package com.infinum.academy.cars.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.servlet.HttpSecurityDsl
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
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
                authorize(HttpMethod.GET, "/movies", permitAll)
                authorize( HttpMethod.POST, "/movies", hasAuthority("SCOPE_MOVIE_MANAGER") )
//                authorize( HttpMethod.POST, "/directors", hasAuthority("SCOPE_DIRECTOR_MANAGER"))
                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer {
                jwt {}
            }
        }
        return http.build()
    }

    @Bean
    fun userDetailsService() : UserDetailsService {
        return InMemoryUserDetailsManager().apply {
            createUser(
                User.withUsername("marko")
                    .password("{bcrypt}\$2a\$10\$H9kQul9XwNtOKSya.aEus.BxSpWair1.yJn3rRHjaFdAI7gJlpZEa")
                    .authorities("MOVIE_ADD").build())

            createUser(
                User.withUsername("ivan")
                    .password("{bcrypt}\$2a\$10\$QNi1cmyWu3BYq6IMw1CeZ.QNPsPH.unnASxHkbIlj.rgKmsiQgbVe")
                    .authorities("ROLE_USER").build())
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
