package com.infinum.academy.cars

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.dto.AddCarCheckUpDto
import com.infinum.academy.cars.dto.AddCarDto
import com.infinum.academy.cars.dto.toCar
import com.infinum.academy.cars.dto.toCarCheckUp
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarNotFoundException
import com.infinum.academy.cars.repository.CarRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.properties.Delegates

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JPATests @Autowired constructor(
    val carRepo: CarRepository,
    val checkupRepo: CarCheckUpRepository
) {
    private var car_example_id : Long = 0

    @BeforeEach
    fun setUp() {

        carRepo.deleteAll()
        checkupRepo.deleteAll()
        val fetcher={id:Long->carRepo.findById(id) ?: throw CarNotFoundException(id)}
        val peugeot = AddCarDto(4,"Peugeot","305",2004,"72").toCar()
        val dacia = AddCarDto(1,"Dacia","Sandero",2010,"4").toCar()
        val renault = AddCarDto(4,"Opel","Corsa",2012,"8").toCar()

        car_example_id=peugeot.id
        val cars = listOf(peugeot,dacia,renault)
        carRepo.saveAll(cars)
        checkupRepo.saveAll(listOf(
            AddCarCheckUpDto("Josip",2f,peugeot.id).toCarCheckUp(fetcher),
            AddCarCheckUpDto("Mirko",5f,peugeot.id).toCarCheckUp(fetcher),
            AddCarCheckUpDto("Josip",10f,peugeot.id).toCarCheckUp(fetcher),
            AddCarCheckUpDto("Josip",2f,dacia.id).toCarCheckUp(fetcher)
        ))
    }

    @Test
    fun `can find all cars`(){
        val allCars = carRepo.findAll()
        assertThat(allCars.size).isEqualTo(3)
    }

    @Test
    fun `can find all cars paged`(){
        val pageable = PageRequest.of(0, 2)
        val allCars = carRepo.findAll(pageable)
        assertThat(allCars.size).isEqualTo(2)
        assertThat(allCars.content[0].serialNumber).isEqualTo("72")
    }

    @Test
    fun `can add a car and find it`(){
        val car = Car(
            ownerId=2,
            dateAdded= LocalDate.now(),
            manufacturerName = "Zastava",
            modelName="101",
            productionYear =1989,
            serialNumber = "800"
        )
        val id=carRepo.save(car).id
        assertThat(car).isEqualTo(carRepo.findById(id))
    }

    @Test
    fun `can add a checkup and find it`(){
        val car = Car(
            ownerId=2,
            dateAdded= LocalDate.now(),
            manufacturerName = "Zastava",
            modelName="101",
            productionYear =1989,
            serialNumber = "800"
        )
        val id=carRepo.save(car).id
        val checkup = CarCheckUp(
            datePerformed= LocalDateTime.now(),
            workerName = "Mario",
            price=20f,
            car=car
        )
        val id_check=checkupRepo.save(checkup).id
        assertThat(checkup).isEqualTo(checkupRepo.findById(id_check))
    }

    @Test
    fun `can find all checkups`(){
        val checkups = checkupRepo.findAll()
        assertThat(checkups.size).isEqualTo(4)
    }

    @Test
    fun `can find all checkups for a car paged`(){
        val car = carRepo.findById(car_example_id) ?:throw CarNotFoundException(car_example_id)
        val pageable = PageRequest.of(0, 2)
        val checkups = checkupRepo.findAllByCar(car,pageable)
        assertThat(checkups.size).isEqualTo(2)
        assertThat(checkups.content[0].workerName).isEqualTo("Josip")
    }

    @Test
    fun `can find car by id only if exists`(){
        val car =  carRepo.findById(car_example_id)
        assertThat(car).isNotNull
        assertThat(car?.productionYear).isEqualTo(2004)

        val car2 =  carRepo.findById(-2)
        assertThat(car2).isNull()
    }

    @Test
    fun `can find all checkups by car id`(){
        val checkups =  checkupRepo.findAllCheckupsForDetails(car_example_id)
        assertThat(checkups.size).isEqualTo(3)
        assertThat(checkups[1].workerName).isEqualTo("Mirko")
    }

    @Test
    fun `can find all checkups by car paged`(){
        val car = carRepo.findById(car_example_id) ?: throw CarNotFoundException(car_example_id)
        val pageable = PageRequest.of(0, 2)
        val checkups =  checkupRepo.findAllByCar(car,pageable)
        assertThat(checkups.size).isEqualTo(2)
        assertThat(checkups.totalPages).isEqualTo(2)
    }

}