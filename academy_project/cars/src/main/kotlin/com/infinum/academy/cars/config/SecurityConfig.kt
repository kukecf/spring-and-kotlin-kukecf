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
                authorize(HttpMethod.GET, "/models", permitAll)
                authorize( HttpMethod.POST, "/cars", authenticated)
                authorize( HttpMethod.GET, "/cars", hasAuthority("SCOPE_ADMIN"))
                authorize( HttpMethod.GET, "/cars/**", authenticated)
                authorize( HttpMethod.GET, "/cars/**/checkups", authenticated)
                authorize(anyRequest, hasAuthority("SCOPE_ADMIN"))
            }
            formLogin{}
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
                User.withUsername("admin")
                    .password("{bcrypt}$2a$10\$jmeu32gy.pLbvLRAnjXboe/W1abr/vh3G6Otn52SvABOfV3fVHsYu") //admin123
                    .roles("ADMIN")
                    .build()
            )

            createUser(
                User.withUsername("ivan")
                    .password("{bcrypt}\$2a\$10\$xK.5a7F4CY98451yA/qBsezVmDR0ZEHuijVLPRR5.oo05MMHplJsa") //ivan123
                    .roles("USER")
                    .build()
            )

            createUser(
                User.withUsername("hodor")
                    .password("{bcrypt}\$2a\$10\$tilD99Hvu/ju2SHLerAS8ewBvhdSV1fJ2IPzqepZZ/BGe9EGAftpG") //hodor123
                    .roles("USER")
                    .build()
            )
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
