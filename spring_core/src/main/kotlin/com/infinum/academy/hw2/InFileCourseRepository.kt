package com.infinum.academy.hw2

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.FileOutputStream

@Qualifier("in-file")
class InFileCourseRepository(
    private val coursesFileResource: Resource // will be provided through dependency injection
) : com.infinum.academy.hw2.CourseRepository {
    init {
        if (coursesFileResource.exists().not()) {
            coursesFileResource.file.createNewFile()
        }
        println("infile")
    }

    override fun insert(name: String): Long {
        val file = coursesFileResource.file
        val id = (file.readLines()
            .filter { it.isNotEmpty() }
            .map { line -> line.split(",").first().toLong() }
            .maxOrNull() ?: 0) + 1
        file.appendText("$id,$name\n")
        return id
    }

    override fun findById(id: Long): com.infinum.academy.hw2.Course {
        return coursesFileResource.file.readLines()
            .filter { it.isNotEmpty() }
            .find { line -> line.split(",").first().toLong() == id }
            ?.let { line ->
                val tokens = line.split(",")
                com.infinum.academy.hw2.Course(id = tokens[0].toLong(), name = tokens[1])
            }
            ?: throw com.infinum.academy.hw2.CourseNotFoundException(id)
    }

    override fun deleteById(id: Long): com.infinum.academy.hw2.Course {
        val coursesLines = coursesFileResource.file.readLines()
        var lineToDelete: String? = null
        FileOutputStream(coursesFileResource.file)
            .writer()
            .use { fileOutputWriter ->
                coursesLines.forEach { line ->
                    if (line.split(",").first().toLong() == id) {
                        lineToDelete = line
                    } else {
                        fileOutputWriter.appendLine(line)
                    }
                }
            }
        return lineToDelete?.let { line ->
            val tokens = line.split(",")
            com.infinum.academy.hw2.Course(id = tokens[0].toLong(), name = tokens[1])
        } ?: throw com.infinum.academy.hw2.CourseNotFoundException(id)
    }
}
