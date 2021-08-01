package com.infinum.academy.cars

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey
import com.infinum.academy.cars.dto.AddCarCheckUpDto
import com.infinum.academy.cars.dto.AddCarDto
import com.infinum.academy.cars.dto.toCar
import com.infinum.academy.cars.dto.toCarCheckUp
import com.infinum.academy.cars.exceptions.*
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarInfoRepository
import com.infinum.academy.cars.repository.CarRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(profiles=["test"])
class JPATests @Autowired constructor(
    val carRepo: CarRepository,
    val checkupRepo: CarCheckUpRepository,
    val carInfoRepository: CarInfoRepository
) {
    private var car_example_id: Long = 0

    @BeforeEach
    fun setUp() {
        val infoFetcher: (String, String) -> CarInfo = { man, model ->
            carInfoRepository.findByCarInfoPk(CarInfoPrimaryKey(man, model)) ?: throw CarInfoNotFoundException(
                man,
                model
            )
        }
        carRepo.deleteAll()
        checkupRepo.deleteAll()
        carInfoRepository.deleteAll()
        val fetcher = { id: Long -> carRepo.findById(id) ?: throw CarNotFoundException(id) }
        carInfoRepository.save(CarInfo(CarInfoPrimaryKey("Peugeot","305"),true))
        carInfoRepository.save(CarInfo(CarInfoPrimaryKey("Dacia","Sandero"),true))
        carInfoRepository.save(CarInfo(CarInfoPrimaryKey("Opel","Corsa"),true))


        val peugeot = AddCarDto(4, 2004, "72", "Peugeot", "305").toCar(infoFetcher)
        val dacia = AddCarDto(1, 2010, "4", "Dacia", "Sandero").toCar(infoFetcher)
        val renault = AddCarDto(4, 2012, "8", "Opel", "Corsa").toCar(infoFetcher)


        val cars = listOf(dacia, renault)

        car_example_id = carRepo.save(peugeot).id
        carRepo.saveAll(cars)

        checkupRepo.saveAll(
            listOf(
                AddCarCheckUpDto("Josip", 2f, peugeot.id).toCarCheckUp(fetcher),
                AddCarCheckUpDto("Mirko", 5f, peugeot.id).toCarCheckUp(fetcher),
                AddCarCheckUpDto("Josip", 10f, peugeot.id).toCarCheckUp(fetcher),
                AddCarCheckUpDto("Josip", 2f, dacia.id).toCarCheckUp(fetcher)
            )
        )

        //carInfoService.saveModelsFromServer()

    }

    @Test
    fun `can find all cars`() {
        val allCars = carRepo.findAll()
        assertThat(allCars.size).isEqualTo(3)
    }

    @Test
    fun `can find all cars paged`() {
        val pageable = PageRequest.of(0, 2)
        val allCars = carRepo.findAll(pageable)
        assertThat(allCars.size).isEqualTo(2)
        assertThat(allCars.content[0].serial_number).isEqualTo("72")
    }

    @Test
    fun `can add a car and find it`() {
        val car = Car(
            owner_id = 2,
            date_added = LocalDate.now(),
            info = carInfoRepository.findByCarInfoPk(CarInfoPrimaryKey("Peugeot", "305"))
                ?: throw CarInfoNotFoundException("Peugeot", "305"),
            production_year = 1989,
            serial_number = "800"
        )
        val id = carRepo.save(car).id
        assertThat(car).isEqualTo(carRepo.findById(id))
    }

    @Test
    fun `can add a checkup and find it`() {
        carInfoRepository.save(CarInfo(CarInfoPrimaryKey("Zastava","102"),true))
        val car = Car(
            owner_id = 2,
            date_added = LocalDate.now(),
            info = carInfoRepository.findByCarInfoPk(CarInfoPrimaryKey("Zastava", "102"))
                ?: throw CarInfoNotFoundException("Zastava", "102"),
            production_year = 1989,
            serial_number = "800"
        )
        val id = carRepo.save(car).id
        val checkup = CarCheckUp(
            datePerformed = LocalDateTime.now(),
            workerName = "Mario",
            price = 20f,
            car = car
        )
        val id_check = checkupRepo.save(checkup).id
        assertThat(checkup).isEqualTo(checkupRepo.findById(id_check))
    }

    @Test
    fun `can find all checkups`() {
        val checkups = checkupRepo.findAll()
        assertThat(checkups.size).isEqualTo(4)
    }

    @Test
    fun `can find all checkups for a car paged`() {
        val car = carRepo.findById(car_example_id) ?: throw CarNotFoundException(car_example_id)
        val pageable = PageRequest.of(0, 2)
        val checkups = checkupRepo.findAllByCar(car, pageable)
        assertThat(checkups.size).isEqualTo(2)
        assertThat(checkups.content[0].workerName).isEqualTo("Josip")
    }

    @Test
    fun `can find car by id only if exists`() {
        val car = carRepo.findById(car_example_id)
        assertThat(car).isNotNull
        assertThat(car?.production_year).isEqualTo(2004)

        val car2 = carRepo.findById(-2)
        assertThat(car2).isNull()
    }

    @Test
    fun `can find all checkups by car id`() {
        val checkups = checkupRepo.findAllCheckupsForDetails(car_example_id)
        assertThat(checkups.size).isEqualTo(3)
    }

    @Test
    fun `can find all checkups by car paged`() {
        val car = carRepo.findById(car_example_id) ?: throw CarNotFoundException(car_example_id)
        val pageable = PageRequest.of(0, 2)
        val checkups = checkupRepo.findAllByCar(car, pageable)
        assertThat(checkups.size).isEqualTo(2)
        assertThat(checkups.totalPages).isEqualTo(2)
    }


}