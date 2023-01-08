package ru.biluta.androidapp.ui.business

import java.util.*
import kotlin.collections.ArrayList

class CompanyOperator {
    private var companies: ArrayList<Company> = ArrayList()

    fun getCompanies(): ArrayList<Company> {
        return companies
    }

    fun setCompanies(newCompanies: ArrayList<Company>) {
        companies = newCompanies
    }

    fun getDepartmentNames(indexCompany: Int): ArrayList<String> {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in companies[indexCompany].listOfDepartments) {
            arrayListForReturn.add(i.nameOfDepartment)
        }
        return arrayListForReturn
    }

    fun getStreetAddress(indexCompany: Int): ArrayList<String> {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in companies[indexCompany].listOfDepartments) {
            arrayListForReturn.add(i.streetAddress)
        }
        return arrayListForReturn
    }

    fun getDepartment(indexCompany: Int, indexDepartment: Int): Department {
        return companies[indexCompany].listOfDepartments[indexDepartment]
    }

    fun sortDepartments(indexCompany: Int, sortIndex: Int) {
        val tempArrayListOfDepartment: ArrayList<Department> = ArrayList()
        val tempArrayListOfColumnString: ArrayList<String> = ArrayList()
        val tempArrayListOfColumnInt: ArrayList<Int> = ArrayList()
        val tempArrayListOfDate: ArrayList<GregorianCalendar> = ArrayList()

        for (i in companies[indexCompany].listOfDepartments) {
            when (sortIndex) {
                0 -> tempArrayListOfColumnString.add(i.nameOfDepartment.lowercase(Locale.ROOT))
                1 -> tempArrayListOfColumnString.add(i.fioManager.lowercase(Locale.ROOT))
                2 -> {
                    val d: List<String> = i.managerStartDate.split(".")
                    tempArrayListOfDate.add(GregorianCalendar(d[2].toInt(), d[1].toInt(), d[0].toInt()))
                }
                3 -> tempArrayListOfColumnString.add(i.streetAddress.lowercase(Locale.ROOT))
                4 -> tempArrayListOfColumnInt.add(i.postalCode)
                5 -> tempArrayListOfColumnString.add(i.city.lowercase(Locale.ROOT))
                6 -> tempArrayListOfColumnString.add(i.region.lowercase(Locale.ROOT))
                7 -> tempArrayListOfColumnInt.add(i.countEmployee)
            }
        }
        if (sortIndex != 2 && sortIndex != 4 && sortIndex != 7) {
            tempArrayListOfColumnString.sort()

            for (i in tempArrayListOfColumnString) {
                for (j in companies[indexCompany].listOfDepartments) {
                    var tempField: String = ""

                    when (sortIndex) {
                        0 -> tempField = j.nameOfDepartment.lowercase(Locale.ROOT)
                        1 -> tempField = j.fioManager.lowercase(Locale.ROOT)
                        2 -> tempField = j.managerStartDate.lowercase(Locale.ROOT)
                        3 -> tempField = j.streetAddress.lowercase(Locale.ROOT)
                        5 -> tempField = j.city.lowercase(Locale.ROOT)
                        6 -> tempField = j.region.lowercase(Locale.ROOT)
                    }

                    if (i == tempField && !tempArrayListOfDepartment.contains(j)) {
                        tempArrayListOfDepartment.add(j)
                        break
                    }
                }
            }
            companies[indexCompany].listOfDepartments = tempArrayListOfDepartment
        } else if (sortIndex == 4 || sortIndex == 7) {
            tempArrayListOfColumnInt.sort()

            for (i in tempArrayListOfColumnInt) {
                for (j in companies[indexCompany].listOfDepartments) {
                    var tempField: Int = -10

                    when (sortIndex) {
                        4 -> tempField = j.postalCode
                        7 -> tempField = j.countEmployee
                    }

                    if (i == tempField && !tempArrayListOfDepartment.contains(j)) {
                        tempArrayListOfDepartment.add(j)
                        break
                    }
                }
            }
            companies[indexCompany].listOfDepartments = tempArrayListOfDepartment
        } else {
            tempArrayListOfDate.sort()

            for (i in tempArrayListOfDate) {
                for (j in companies[indexCompany].listOfDepartments) {
                    val d: List<String> = j.managerStartDate.split(".")
                    val tempGregorianCalendar = GregorianCalendar(d[2].toInt(), d[1].toInt(), d[0].toInt())
                    if (tempGregorianCalendar == i && !tempArrayListOfDepartment.contains(j)) {
                        tempArrayListOfDepartment.add(j)
                        break
                    }
                }
            }
            companies[indexCompany].listOfDepartments = tempArrayListOfDepartment
        }
    }
}