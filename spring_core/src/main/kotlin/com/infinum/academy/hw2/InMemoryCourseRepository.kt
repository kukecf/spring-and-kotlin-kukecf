package com.infinum.academy.hw2

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Qualifier("in-memory")
class InMemoryCourseRepository(private val resource: DataSource) : CourseRepository {
    private val courses = mutableMapOf<Long, Course>()

    init {
        println(resource.dbName)
        println(resource.username)
        println(resource.password)
    }

    override fun insert(name: String): Long {
        val id = (courses.keys.maxOrNull() ?: 0) + 1
        courses[id] = Course(id, name)
        return id
    }

    override fun findById(id: Long): Course {
        return courses[id] ?: throw CourseNotFoundException(id)
    }

    override fun deleteById(id: Long): Course {
        return courses.remove(id) ?: throw CourseNotFoundException(id)
    }
}

class CourseNotFoundException(id: Long) : RuntimeException("com.infinum.academy.hw2.Course with and ID $id not found")