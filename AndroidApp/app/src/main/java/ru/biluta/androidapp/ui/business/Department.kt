package ru.biluta.androidapp.ui.business

data class Department(
    val nameOfDepartment: String,
    val fioManager: String,
    val managerStartDate: String,
    val streetAddress: String,
    val postalCode: Int,
    val city: String,
    val region: String,
    val countEmployee: Int
)
