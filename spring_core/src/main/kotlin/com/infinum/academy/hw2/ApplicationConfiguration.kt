package com.infinum.academy.hw2

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.*
import org.springframework.core.env.Environment
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.PathResource
import org.springframework.core.io.Resource

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
class ApplicationConfiguration {
    @Bean
    fun resource(src: DataSource): Resource = PathResource(src.dbName)

    @Bean
    @Qualifier("switch")
    fun switch(@Value("\${repo.switch}") crSwitch: String, r:Resource, d:DataSource): CourseRepository =
        if(crSwitch=="turned-on") InMemoryCourseRepository(d) else InFileCourseRepository(r)
}

data class Switch(
    val value: String
)

fun main() {
    val appContext = AnnotationConfigApplicationContext(ApplicationConfiguration::class.java)
    val courseService = appContext.getBean(CourseService::class.java)
    println(courseService.findCourseById(courseService.insertIntoRepo("Stef")))
}