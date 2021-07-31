package com.infinum.academy.cars

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class CarsApplication

fun main(args: Array<String>) {
    runApplication<CarsApplication>(*args)
}
