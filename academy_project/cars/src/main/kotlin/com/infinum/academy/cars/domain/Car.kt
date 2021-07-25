package com.infinum.academy.cars.domain

import java.time.LocalDate
import javax.persistence.*

//stavio sam snake case zato sto mi se cini da je to neka praksa za baze prema primjerima s predavanja i u dokumentaciji

@Entity
@Table(name = "cars")
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_SEQ")
    @SequenceGenerator(name = "CAR_SEQ", sequenceName = "CAR_SEQ", allocationSize = 1)
    val id: Long = 0,

    @Column(name = "owner_id")
    val ownerId: Long,

    @Column(name = "date_added")
    val dateAdded: LocalDate,

    @Column(name = "owner_id")
    val manufacturerName: String,

    @Column(name = "model_name")
    val modelName: String,

    @Column(name = "production_year")
    val productionYear: Int,

    @Column(name = "serial_number")
    val serialNumber: String,

    @ElementCollection
    @CollectionTable(name = "car_checkups", joinColumns = [JoinColumn(name = "car_id")])
    val checkUps: List<CarCheckUp> = emptyList()
)
