package com.infinum.academy.cars

import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarRepository
import com.infinum.academy.cars.repository.DBCarCheckUpRepository
import com.infinum.academy.cars.repository.DBCarRepository
import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.annotation.Rollback
import java.time.LocalDate
import java.time.LocalDateTime

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
class JdbcTests @Autowired constructor(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    private val carRowMapper = RowMapper() { r, _ ->
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

    private val checkUpRowMapper = RowMapper() { r, _ ->
        CarCheckUp(
            r.getLong("checkUpId"),
            r.getTimestamp("datePerformed").toLocalDateTime(),
            r.getString("workerName"),
            r.getFloat("price"),
            r.getLong("carId"),
        )
    }

    private fun insertCar(
        id: Long?,
        serial: String?,
        manName: String?,
        modelName: String?,
        ownerId: Long?,
        year: Int?
    ) {
        jdbcTemplate.update(
            "INSERT INTO cars (carId,ownerId,dateAdded,manufacturerName,modelName,productionYear,serialNumber) VALUES (:id,:ownerId,:date,:manName,:modelName,:year,:serial)",
            mapOf(
                "id" to id,
                "serial" to serial,
                "date" to LocalDate.now(),
                "manName" to manName,
                "modelName" to modelName,
                "ownerId" to ownerId,
                "year" to year
            )
        )
    }

    private fun insertCarCheckUp(id: Long?, worker: String?, price: Float?, carId: Long?) {
        jdbcTemplate.update(
            "INSERT INTO checkups (checkUpId,datePerformed,workerName,price,carId) VALUES (:id,:date,:worker,:price,:carId)",
            mapOf(
                "id" to id,
                "date" to LocalDateTime.now(),
                "worker" to worker,
                "price" to price,
                "carId" to carId
            )
        )
    }

    @BeforeEach
    fun setUp() {
        insertCar(4, "72", "Yugo", "GV Sport", 2, 1982)
        insertCarCheckUp(2, "Jura", 2.0f, 4)
    }

    @Test
    @DisplayName("should retrieve modelName correctly")
    fun test1() {
        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT modelName FROM cars WHERE manufacturerName = :manName",
                mapOf("manName" to "Yugo"),
                String::class.java
            )
        ).isEqualTo("GV Sport")
    }

    @Test
    @DisplayName("should fail in adding second car with same serial number")
    fun test2() {
        assertThatThrownBy {
            insertCar(5, "72", "Fiat", "128", 2, 1985)
        }.isInstanceOf(DuplicateKeyException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - cars")
    fun test3() {
        assertThatThrownBy {
            insertCar(null, "72", "Fiat", "128", 2, 1985)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - cars")
    fun test4() {
        assertThatThrownBy {
            insertCar(5, "72", null, "128", 2, 1985)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }


    @Test
    @DisplayName("should fail in null value exception - cars")
    fun test5() {
        assertThatThrownBy {
            insertCar(5, "72", "Fiat", null, 2, 1985)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - cars")
    fun test6() {
        assertThatThrownBy {
            insertCar(5, "72", "Fiat", "128", null, 1985)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - cars")
    fun test7() {
        assertThatThrownBy {
            insertCar(5, "72", "Fiat", "128", 2, null)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - checkups")
    fun test8() {
        assertThatThrownBy {
            insertCarCheckUp(null, "Jura", 2.0f, 4)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - checkups")
    fun test9() {
        assertThatThrownBy {
            insertCarCheckUp(2, null, 2.0f, 4)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - checkups")
    fun test10() {
        assertThatThrownBy {
            insertCarCheckUp(2, "Jura", null, 4)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - checkups")
    fun test11() {
        assertThatThrownBy {
            insertCarCheckUp(2, "Jura", 2.0f, null)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should return 2 checkups with worker Jura")
    fun test12() {
        insertCar(6, "76", "Zastava", "101", 2, 1987)
        insertCarCheckUp(4, "Jura", 2.0f, 6)
        insertCarCheckUp(5, "Boris", 3.0f, 6)
        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT count(*) FROM cars JOIN checkups ON cars.carId = checkups.carId WHERE checkups.workerName=:worker GROUP BY checkups.workerName",
                mapOf("worker" to "Jura"),
                Int::class.java
            )
        ).isEqualTo(2)
    }

}