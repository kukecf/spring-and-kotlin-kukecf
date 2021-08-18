package com.infinum.academy.cars

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey
import com.infinum.academy.cars.dto.AddCarCheckUpDto
import com.infinum.academy.cars.dto.AddCarDto
import com.infinum.academy.cars.dto.toCar
import com.infinum.academy.cars.dto.toCarCheckUp
import com.infinum.academy.cars.exceptions.CarInfoNotFoundException
import com.infinum.academy.cars.exceptions.CarNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarInfoRepository
import com.infinum.academy.cars.repository.CarRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import javax.transaction.Transactional

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback
class JPATests @Autowired constructor(
    val carRepo: CarRepository,
    val checkupRepo: CarCheckUpRepository,
    val carInfoRepository: CarInfoRepository
) {
    private var carExampleId: Long = 0

    @BeforeAll
    fun setUp() {
        val infoFetcher: (String, String) -> CarInfo = { man, model ->
            carInfoRepository.findByCarInfoPk(CarInfoPrimaryKey(man, model)) ?: throw CarInfoNotFoundException(
                man,
                model
            )
        }
        val fetcher = { id: Long -> carRepo.findById(id) ?: throw CarNotFoundException(id) }
        carInfoRepository.save(CarInfo(CarInfoPrimaryKey("Peugeot", "305"), true))
        carInfoRepository.save(CarInfo(CarInfoPrimaryKey("Dacia", "Sandero"), true))
        carInfoRepository.save(CarInfo(CarInfoPrimaryKey("Opel", "Corsa"), true))
        val peugeot = AddCarDto(4, 2004, "72", "Peugeot", "305").toCar(infoFetcher)
        val dacia = AddCarDto(1, 2010, "4", "Dacia", "Sandero").toCar(infoFetcher)
        val renault = AddCarDto(4, 2012, "8", "Opel", "Corsa").toCar(infoFetcher)
        val cars = listOf(dacia, renault)
        carExampleId = carRepo.save(peugeot).id
        carRepo.saveAll(cars)
        checkupRepo.saveAll(
            listOf(
                AddCarCheckUpDto("Josip", 2f, peugeot.id, LocalDateTime.now()).toCarCheckUp(fetcher),
                AddCarCheckUpDto("Mirko", 5f, peugeot.id, LocalDateTime.now()).toCarCheckUp(fetcher),
                AddCarCheckUpDto("Josip", 10f, peugeot.id, LocalDateTime.now()).toCarCheckUp(fetcher),
                AddCarCheckUpDto("Josip", 2f, dacia.id, LocalDateTime.now()).toCarCheckUp(fetcher)
            )
        )
        checkupRepo.saveAll(
            listOf(
                AddCarCheckUpDto("Josip", 2f, peugeot.id, LocalDateTime.now().plus(Period.ofMonths(1))).toCarCheckUp(
                    fetcher
                ),
                AddCarCheckUpDto("Mirko", 5f, renault.id, LocalDateTime.now().plus(Period.ofMonths(1))).toCarCheckUp(
                    fetcher
                ),
                AddCarCheckUpDto("Josip", 10f, renault.id, LocalDateTime.now().plus(Period.ofMonths(1))).toCarCheckUp(
                    fetcher
                ),
                AddCarCheckUpDto("Josip", 2f, dacia.id, LocalDateTime.now().plus(Period.ofMonths(1))).toCarCheckUp(
                    fetcher
                )
            )
        )
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
        assertThat(allCars.content[0].serialNumber).isEqualTo("72")
    }

    @Test
    @Transactional
    fun `can add a car and find it`() {
        val car = Car(
            ownerId = 2,
            dateAdded = LocalDate.now(),
            info = carInfoRepository.findByCarInfoPk(CarInfoPrimaryKey("Peugeot", "305"))
                ?: throw CarInfoNotFoundException("Peugeot", "305"),
            productionYear = 1989,
            serialNumber = "800"
        )
        val id = carRepo.save(car).id
        assertThat(car).isEqualTo(carRepo.findById(id))
    }

    @Test
    @Transactional
    fun `can add a checkup and find it`() {
        carInfoRepository.save(CarInfo(CarInfoPrimaryKey("Zastava", "102"), true))
        val car = Car(
            ownerId = 2,
            dateAdded = LocalDate.now(),
            info = carInfoRepository.findByCarInfoPk(CarInfoPrimaryKey("Zastava", "102"))
                ?: throw CarInfoNotFoundException("Zastava", "102"),
            productionYear = 1989,
            serialNumber = "800"
        )
        carRepo.save(car).id
        val checkup = CarCheckUp(
            datePerformed = LocalDateTime.now(),
            workerName = "Mario",
            price = 20f,
            car = car
        )
        val idCheck = checkupRepo.save(checkup).id
        assertThat(checkup).isEqualTo(checkupRepo.findById(idCheck))
    }

    @Test
    fun `can find all checkups`() {
        val checkups = checkupRepo.findAll()
        assertThat(checkups.size).isEqualTo(8)
    }

    @Test
    fun `can find all checkups for a car paged`() {
        val pageable = PageRequest.of(0, 2)
        val checkups = checkupRepo.findAllByCarId(carExampleId, pageable)
        assertThat(checkups.size).isEqualTo(2)
        assertThat(checkups.content[0].workerName).isEqualTo("Josip")
    }

    @Test
    fun `can find car by id only if exists`() {
        val car = carRepo.findById(carExampleId)
        assertThat(car).isNotNull
        assertThat(car?.productionYear).isEqualTo(2004)
        val car2 = carRepo.findById(-2)
        assertThat(car2).isNull()
    }

    @Test
    fun `can find all checkups by car id`() {
        val checkups = checkupRepo.findAllCheckupsForDetails(carExampleId)
        assertThat(checkups.size).isEqualTo(4)
    }
}
