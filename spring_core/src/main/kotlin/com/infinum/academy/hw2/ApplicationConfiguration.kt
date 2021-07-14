package com.infinum.academy.hw2

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBean
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
    fun switch(
        @Value("\${repo.switch}") isMemory: Boolean,
        resource: Resource,
        dataSource: DataSource
    ): CourseRepository =
        if (isMemory) InMemoryCourseRepository(dataSource) else InFileCourseRepository(resource)
}

//ovo je rucni test
fun main() {
    val appContext = AnnotationConfigApplicationContext(ApplicationConfiguration::class.java)
    val courseService = appContext.getBean<CourseService>()
    println(courseService.findCourseById(courseService.insertIntoRepo("Stef")))
}