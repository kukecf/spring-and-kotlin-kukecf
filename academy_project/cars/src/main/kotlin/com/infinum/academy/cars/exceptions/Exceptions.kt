package com.infinum.academy.cars.exceptions

open class NotFoundException(message:String) : RuntimeException(message)

class CarNotFoundException(id: Long) : NotFoundException("Car with ID $id does not exist!")

class CarCheckUpNotFoundException(id: Long) : NotFoundException("Checkup with ID $id does not exist!")

class CarInfoNotFoundException(man: String, model: String) : NotFoundException("Car info for $man $model does not exist!")

class NoModelsException() : NotFoundException("No models found on server!")