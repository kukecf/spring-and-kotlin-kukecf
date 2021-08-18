package com.infinum.academy.cars.config

import com.infinum.academy.cars.services.CarInfoAdministrationService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration(proxyBeanMethods = false)
@EnableScheduling
@ConditionalOnProperty("scheduling.enabled", havingValue = "true")
class SchedulingConfig(
    private val service: CarInfoAdministrationService,
) {
    @Scheduled(fixedRateString = "\${car.info.schedule-interval}")
    fun fetchCarInfoIntoDB() {
        service.saveModelsFromServer()
    }
}
