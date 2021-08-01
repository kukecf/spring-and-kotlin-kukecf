package com.infinum.academy.cars.repository

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.dto.Duration
import com.infinum.academy.cars.dto.TimeInterval
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param


interface CarCheckUpRepository : Repository<CarCheckUp, Long> {
    fun save(checkup: CarCheckUp): CarCheckUp

    fun saveAll(checkups: Iterable<CarCheckUp>): Iterable<CarCheckUp>

    fun findById(id: Long): CarCheckUp?

    fun findAll(): List<CarCheckUp>

    fun findAllByOrderByDatePerformedDesc(): List<CarCheckUp>

    //skuzio sam da u ovaj query trebaju ici svi checkupovi koji su vec napravljeni
    @Query("select * from checkups cu where date_performed <= current_timestamp order by date_performed desc fetch first :lim rows only", nativeQuery = true)
    fun findLatestCheckups(@Param("lim") limit: Int): List<CarCheckUp>

    @Query("select check from CarCheckUp check join fetch Car car on car.id=check.car.id where check.car.id = :carId order by check.datePerformed desc")
    fun findAllCheckupsForDetails(carId: Long): List<CarCheckUp>

    fun findAllByCar(car: Car, pageable: Pageable): Page<CarCheckUp>

    @Query(
        value = "select * from checkups cu where cu.date_performed BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + interval '1 weeks' * :quant order by cu.date_performed",
        countQuery = "select count(*) from checkups cu cu.date_performed BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + interval '1 weeks' * :quant",
        nativeQuery = true
    )
    fun findUpcomingWithinWeeks(@Param("quant") quant: Int, pageable: Pageable): Page<CarCheckUp>

    @Query(
        value = "select * from checkups cu where cu.date_performed BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + interval '1 months' * :quant order by cu.date_performed",
        countQuery = "select count(*) from checkups cu where cu.date_performed BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + interval '1 months' * :quant",
        nativeQuery = true
    )
    fun findUpcomingWithinMonths(@Param("quant") quant: Int, pageable: Pageable): Page<CarCheckUp>

    fun deleteAll()
}