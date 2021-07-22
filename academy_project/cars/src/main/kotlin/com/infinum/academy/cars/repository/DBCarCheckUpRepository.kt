package com.infinum.academy.cars.repository

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.sql.DataSource

@Repository
class DBCarCheckUpRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val dataSource: DataSource
) : CarCheckUpRepository {

    private val rowMapper = RowMapper() { r, _ ->
        CarCheckUp(
            r.getLong("id"),
            r.getTimestamp("datePerformed").toLocalDateTime(),
            r.getString("workerName"),
            r.getFloat("price"),
            r.getLong("carId"),
        )
    }

    private val simpleJdbcInsert = SimpleJdbcInsert(dataSource).withTableName("checkups")

    init{
        simpleJdbcInsert.setGeneratedKeyName("id")
    }

    override fun save(checkup: CarCheckUp): Long {
        return simpleJdbcInsert.executeAndReturnKey(
            mapOf(
                "datePerformed" to LocalDateTime.now(),
                "workerName" to checkup.workerName,
                "price" to checkup.price,
                "carId" to checkup.carId
            )
        ).toLong()
    }

    override fun findById(id: Long): CarCheckUp? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM checkups WHERE id = :id",
            mapOf(
                "id" to id,
            ),
            rowMapper
        )
    }

    override fun findAllByCarId(id: Long): List<CarCheckUp> {
        return jdbcTemplate.query(
            "SELECT * FROM checkups WHERE carId=:id ORDER BY datePerformed DESC",
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