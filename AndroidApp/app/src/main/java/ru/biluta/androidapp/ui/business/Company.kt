package ru.biluta.androidapp.ui.business

data class Company(
    val name: String,
    var listOfDepartments: ArrayList<Department> = ArrayList()
)