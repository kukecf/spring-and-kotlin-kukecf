package com.infinum.academy.cars

import com.infinum.academy.cars.dto.CarCheckUpDto
import com.infinum.academy.cars.dto.CarDto
import com.infinum.academy.cars.dto.toCarCheckUp
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JPATests @Autowired constructor(
    val carRepo: CarRepository,
    val checkupRepo: CarCheckUpRepository
) {
    @BeforeEach
    fun setUp() {

        carRepo.deleteAll()
        checkupRepo.deleteAll()

        val peugeot = CarDto(4,"Peugeot","305",2004,"72").toCarCheckUp()
        val dacia = CarDto(1,"Dacia","Sandero",2010,"4").toCarCheckUp()
        val renault = CarDto(4,"Opel","Corsa",2012,"8").toCarCheckUp()

        val cars = listOf(peugeot,dacia,renault)
        carRepo.saveAll(cars)
/*
        checkupRepo.saveAll(listOf(
            CarCheckUpDto("Josip",2f,peugeot.id).toCarCheckUp(),
            CarCheckUpDto("Mirko",5f,peugeot.id).toCarCheckUp(),
            CarCheckUpDto("Josip",10f,peugeot.id).toCarCheckUp(),
            CarCheckUpDto("Josip",2f,dacia.id).toCarCheckUp()
        ))

 */
    }
}