package com.infinum.academy.cars.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CarNotFoundException(id: Long) : ResponseStatusException(HttpStatus.NOT_FOUND, "Car with ID $id does not exist!")
class CarCheckUpNotFoundException(id: Long) : ResponseStatusException(HttpStatus.NOT_FOUND, "Checkup with ID $id does not exist!")
class CarInfoNotFoundException(man:String,model:String) : ResponseStatusException(HttpStatus.NOT_FOUND, "Car info for $man $model does not exist!")
class NoModelsException() : ResponseStatusException(HttpStatus.NOT_FOUND, "No models found on server!")