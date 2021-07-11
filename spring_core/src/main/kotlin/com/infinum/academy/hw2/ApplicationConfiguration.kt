package com.infinum.academy.hw2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.*
import org.springframework.core.io.Resource

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
class ApplicationConfiguration

fun main() {
    val appContext = AnnotationConfigApplicationContext(ApplicationConfiguration::class.java)
    val courseRepository = appContext.getBean(CourseRepository::class.java)
}