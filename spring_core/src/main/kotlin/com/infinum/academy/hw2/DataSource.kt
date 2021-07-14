package com.infinum.academy.hw2

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
data class DataSource(
    @Value("\${data.database-name}") val dbName: String,
    @Value("\${data.username}") val username: String,
    @Value("\${data.password}") val password: String
)