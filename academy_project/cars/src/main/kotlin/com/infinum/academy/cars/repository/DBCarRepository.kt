package com.infinum.academy.cars.repository

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.sql.DataSource

@Repository
class DBCarRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val dataSource: DataSource
) : CarRepository {

    private val rowMapper = RowMapper() { r, _ ->
        Car(
            r.getLong("id"),
            r.getLong("ownerId"),
            r.getDate("dateAdded").toLocalDate(),
            r.getString("manufacturerName"),
            r.getString("modelName"),
            r.getInt("productionYear"),
            r.getString("serialNumber")
        )
    }

    private val simpleJdbcInsert = SimpleJdbcInsert(dataSource).withTableName("cars")

    init{
        simpleJdbcInsert.setGeneratedKeyName("id")
    }

    override fun save(car: Car): Long {
        return simpleJdbcInsert.executeAndReturnKey(
            mapOf(
                "ownerId" to car.ownerId,
                "dateAdded" to car.dateAdded,
                "manufacturerName" to car.manufacturerName,
                "modelName" to car.modelName,
                "productionYear" to car.productionYear,
                "serialNumber" to car.serialNumber
            )
        ).toLong()
    }

    override fun findById(id: Long): Car? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM cars WHERE id = :id",
            mapOf(
                "id" to id,
            ),
            rowMapper
        )
    }

    override fun findBySerialNumber(serialNo: String): Car? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM cars WHERE serialNumber = :serial",
            mapOf(
                "serial" to serialNo,
            ),
            rowMapper
        )
    }

    override fun findAll(): List<Car> {
        return jdbcTemplate.query(
            "SELECT * FROM cars",
            rowMapper
        )
    }
}