package com.infinum.academy.hw2

interface CourseRepository {
    fun insert(name: String): Long
    fun findById(id: Long): Course
    fun deleteById(id: Long): Course
}