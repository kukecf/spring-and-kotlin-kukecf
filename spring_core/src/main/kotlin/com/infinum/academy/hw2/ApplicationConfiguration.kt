package com.infinum.academy.hw2

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.*
import org.springframework.core.env.Environment
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.PathResource


@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
class ApplicationConfiguration {
    @Bean
    @Qualifier("in-memory")
    fun inMemoryCourseRepository(src: DataSource): CourseRepository = InMemoryCourseRepository(src)

    @Bean
    @Qualifier("in-file")
    fun inFileCourseRepository(src: DataSource): CourseRepository {
        val name = src.dbName
        val resource = PathResource(name) // ili FileSystemResource? Koja je razlika u praksi?
        return InFileCourseRepository(resource)
    }

    @Bean
    fun switch(@Value("\${repo.switch}") crSwitch:String):Switch = Switch(crSwitch)

}

data class Switch(
    val value:String
    )

fun main() {
    val appContext = AnnotationConfigApplicationContext(ApplicationConfiguration::class.java)
    val courseService = appContext.getBean(CourseService::class.java)
    println(courseService.findUserById(courseService.insertIntoRepo("Stef")))
}