package com.infinum.academy.cars.domain

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "cars", uniqueConstraints = [UniqueConstraint(columnNames = ["serial_number"])])
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_SEQ")
    @SequenceGenerator(name = "CAR_SEQ", sequenceName = "CAR_SEQ", allocationSize = 1)
    val id: Long = 0,

    val owner_id: Long,

    val date_added: LocalDate,

    val manufacturer_name: String,

    val model_name: String,

    val production_year: Int,

    val serial_number: String
)
