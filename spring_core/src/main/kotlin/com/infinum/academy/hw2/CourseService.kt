package com.infinum.academy.hw2

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class CourseService(
    @Qualifier("switch") private val repo: CourseRepository
) {
    fun insertIntoRepo(name: String): Long = repo.insert(name)

    fun findCourseById(id: Long): Course = repo.findById(id)

    fun deleteCourseById(id: Long): Course = repo.deleteById(id)
}