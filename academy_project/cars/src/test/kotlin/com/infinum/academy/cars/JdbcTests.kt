package com.infinum.academy.cars

import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarRepository
import com.infinum.academy.cars.repository.DBCarCheckUpRepository
import com.infinum.academy.cars.repository.DBCarRepository
import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import com.infinum.academy.cars.resource.CarCheckUpDto
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
import org.springframework.http.MediaType
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.test.annotation.Rollback
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import javax.sql.DataSource

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
class JdbcTests @Autowired constructor(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val dataSource: DataSource
) {
    private val carRowMapper = RowMapper() { r, _ ->
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

    private val checkUpRowMapper = RowMapper() { r, _ ->
        CarCheckUp(
            r.getLong("checkUpId"),
            r.getTimestamp("datePerformed").toLocalDateTime(),
            r.getString("workerName"),
            r.getFloat("price"),
            r.getLong("carId"),
        )
    }

    private val simpleJdbcInsertCars = SimpleJdbcInsert(dataSource).withTableName("cars")
    private val simpleJdbcInsertCheckups = SimpleJdbcInsert(dataSource).withTableName("checkups")

    init{
        simpleJdbcInsertCars.setGeneratedKeyName("id")
        simpleJdbcInsertCheckups.setGeneratedKeyName("id")
    }

    private fun insertCar(
        serial: String?,
        manName: String?,
        modelName: String?,
        ownerId: Long?,
        year: Int?
    ) : Long {
        return simpleJdbcInsertCars.executeAndReturnKey(
            mapOf(
                "ownerId" to ownerId,
                "dateAdded" to LocalDate.now(),
                "manufacturerName" to manName,
                "modelName" to modelName,
                "productionYear" to year,
                "serialNumber" to serial
            )
        ).toLong()
    }

    private fun insertCarCheckUp(worker: String?, price: Float?, carId: Long?) : Long {
        return simpleJdbcInsertCheckups.executeAndReturnKey(
            mapOf(
                "datePerformed" to LocalDateTime.now(),
                "workerName" to worker,
                "price" to price,
                "carId" to carId
            )
        ).toLong()
    }

    @BeforeEach
    fun setUp() {
        val id= insertCar("72", "Yugo", "GV Sport", 2, 1982)
        insertCarCheckUp( "Jura", 2.0f, id)
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
            insertCar( "72", "Fiat", "128", 2, 1985)
        }.isInstanceOf(DuplicateKeyException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - cars")
    fun test4() {
        assertThatThrownBy {
            insertCar( "72", null, "128", 2, 1985)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }


    @Test
    @DisplayName("should fail in null value exception - cars")
    fun test5() {
        assertThatThrownBy {
            insertCar( "72", "Fiat", null, 2, 1985)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - cars")
    fun test6() {
        assertThatThrownBy {
            insertCar( "72", "Fiat", "128", null, 1985)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - cars")
    fun test7() {
        assertThatThrownBy {
            insertCar( "72", "Fiat", "128", 2, null)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - checkups")
    fun test9() {
        assertThatThrownBy {
            insertCarCheckUp( null, 2.0f, 4)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - checkups")
    fun test10() {
        assertThatThrownBy {
            insertCarCheckUp( "Jura", null, 4)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should fail in null value exception - checkups")
    fun test11() {
        assertThatThrownBy {
            insertCarCheckUp( "Jura", 2.0f, null)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    @DisplayName("should return 2 checkups with worker Jura")
    fun test12() {
        val id= insertCar( "76", "Zastava", "101", 2, 1987)
        insertCarCheckUp( "Jura", 2.0f, id)
        insertCarCheckUp("Boris", 3.0f, id)
        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT count(*) FROM cars JOIN checkups ON cars.id = checkups.carId WHERE checkups.workerName=:worker GROUP BY checkups.workerName",
                mapOf("worker" to "Jura"),
                Int::class.java
            )
        ).isEqualTo(2)
    }

    @Test
    @DisplayName("should fail in adding checkup for nonexisting car")
    @Transactional
    fun test13() {
        assertThatThrownBy {
            insertCarCheckUp( "Josip", 2f, 189)
        }.isInstanceOf(DataIntegrityViolationException::class.java)
    }

}