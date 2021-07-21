package com.infinum.academy.cars.repository

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
@Qualifier("db")
class DBCarRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : CarRepository {
    private val rowMapper = RowMapper() { r, _ ->
        Car(
            r.getLong("carId"),
            r.getLong("ownerId"),
            r.getDate("dateAdded").toLocalDate(),
            r.getString("manufacturerName"),
            r.getString("modelName"),
            r.getInt("productionYear"),
            r.getString("serialNumber")
        )
    }

    override fun save(car: Car): Long {
        jdbcTemplate.update(
            "INSERT INTO cars (carId,ownerId,dateAdded,manufacturerName,modelName,productionYear,serialNumber) VALUES (:id,:ownerId,:date,:manName,:modelName,:year,:serial)",
            mapOf(
                "id" to car.carId,
                "serial" to car.serialNumber,
                "date" to car.dateAdded,
                "manName" to car.manufacturerName,
                "modelName" to car.modelName,
                "ownerId" to car.ownerId,
                "year" to car.productionYear
            )
        )
        return car.carId
    }

    override fun findById(id: Long): Car? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM cars WHERE carId = :id",
            mapOf(
                "id" to id,
            ),
            rowMapper
        )
    }

    fun findBySerialNumber(serialNo: String): Car? {
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