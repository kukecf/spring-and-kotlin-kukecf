package com.infinum.academy.cars.repository

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
@Qualifier("db")
class DBCarCheckUpRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : CarCheckUpRepository {
    private val rowMapper = RowMapper() { r, _ ->
        CarCheckUp(
            r.getLong("checkUpId"),
            r.getTimestamp("datePerformed").toLocalDateTime(),
            r.getString("workerName"),
            r.getFloat("price"),
            r.getLong("carId"),
        )
    }

    override fun save(checkup: CarCheckUp): Long {
        jdbcTemplate.update(
            "INSERT INTO checkups (checkUpId,datePerformed,workerName,price,carId) VALUES (:id,:date,:worker,:price,:carId)",
            mapOf(
                "id" to checkup.checkUpId,
                "date" to LocalDateTime.now(),
                "worker" to checkup.workerName,
                "price" to checkup.price,
                "carId" to checkup.carId
            )
        )
        return checkup.checkUpId
    }

    override fun findById(id: Long): CarCheckUp? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM checkups WHERE checkUpId = :id",
            mapOf(
                "id" to id,
            ),
            rowMapper
        )
    }

    override fun findAll(): List<CarCheckUp> {
        return jdbcTemplate.query(
            "SELECT * FROM checkups",
            rowMapper
        )
    }
}