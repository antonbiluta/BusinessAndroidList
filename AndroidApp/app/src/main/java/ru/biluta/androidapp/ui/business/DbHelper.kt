package ru.biluta.androidapp.ui.business

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    private val table1Name = "COMPANIES"
    private val col11 = "id"
    private val col12 = "name"
    private val createTable1 = "CREATE TABLE IF NOT EXISTS $table1Name" +
            "($col11 INTEGER PRIMARY KEY AUTOINCREMENT, $col12 TEXT);"
    private val dropTable1 = "DROP TABLE IF EXISTS $table1Name"

    private val table2Name = "DEPARTMENTS"
    private val col21 = "id"
    private val col22 = "company_id"
    private val col23 = "name"
    private val col24 = "manager_fio"
    private val col25 = "manager_startdate"
    private val col26 = "street_address"
    private val col27 = "postal_code"
    private val col28 = "city"
    private val col29 = "region"
    private val col210 = "count_employee"
    private val createTable2 = "CREATE TABLE IF NOT EXISTS $table2Name " +
            "($col21 INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$col22 INTEGER, $col23 TEXT, $col24 TEXT, $col25 TEXT," +
            "$col26 TEXT, $col27 INTEGER, $col28 TEXT, $col29 TEXT, $col210 INTEGER);"
    private val dropTable2 = "DROP TABLE IF EXISTS $table2Name"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTable1)
        db?.execSQL(createTable2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(dropTable1)
        db?.execSQL(dropTable2)
        onCreate(db)
    }

    fun insertCompany(company: Company): Int {
        var forReturn = 0
        val db = this.writableDatabase
        val cv1 = ContentValues()
        cv1.put(col12, company.name)
        var result = db.insert(table1Name, null, cv1)
        if (result != -1L) {
            forReturn = 1000
        }
        val namec: String = company.name
        val selection: String = "name =?"
        val selectionArgs: Array<String> = arrayOf(company.name)
        val cursor: Cursor = db.query(table1Name, arrayOf(col11), selection,
            selectionArgs, null, null, null)

        cursor.moveToFirst()
        val index = cursor.getColumnIndex(col11)
        var companyId: Int = -10
        if (index >= 0) {
            companyId = cursor.getInt(index)
        }
        cursor.close()
        for (i in company.listOfDepartments) {
            val cv2 = ContentValues()
            cv2.put(col22, companyId)
            cv2.put(col23, i.nameOfDepartment)
            cv2.put(col24, i.fioManager)
            cv2.put(col25, i.managerStartDate)
            cv2.put(col26, i.streetAddress)
            cv2.put(col27, i.postalCode)
            cv2.put(col28, i.city)
            cv2.put(col29, i.region)
            cv2.put(col210, i.countEmployee)
            result = db.insert(table2Name, null, cv2)
            if (result != -1L) {
                forReturn++
            }
        }
        return forReturn
    }

    @SuppressLint("Range")
    fun getAllData(): ArrayList<Company> {
        val arrayListForReturn: ArrayList<Company> = ArrayList()
        val db = this.writableDatabase
        val cursor1: Cursor = db.query(table1Name, null, null,
            null, null, null, null)
        cursor1.moveToFirst()
        while (!cursor1.isAfterLast) {
            val tempArrayListForDepartment: ArrayList<Department> = ArrayList()
            val index = cursor1.getColumnIndex(col11)
            var companyId: Int = -10
            if (index >= 0) {
                companyId = cursor1.getInt(index)
            }
            val cursor2: Cursor = db.query(table2Name, arrayOf(col22, col23, col24, col25, col26,
                col27, col28, col29, col210), "$col22 = $companyId",
            null, null, null, null)
            cursor2.moveToFirst()
            while (!cursor2.isAfterLast) {
                tempArrayListForDepartment.add(Department(
                    cursor2.getString(cursor2.getColumnIndex(col23)),
                    cursor2.getString(cursor2.getColumnIndex(col24)),
                    cursor2.getString(cursor2.getColumnIndex(col25)),
                    cursor2.getString(cursor2.getColumnIndex(col26)),
                    cursor2.getInt(cursor2.getColumnIndex(col27)),
                    cursor2.getString(cursor2.getColumnIndex(col28)),
                    cursor2.getString(cursor2.getColumnIndex(col29)),
                    cursor2.getInt(cursor2.getColumnIndex(col210))
                ))
                cursor2.moveToNext()
            }
            arrayListForReturn.add(Company(cursor1.getString(cursor1.getColumnIndex(col12)),
                tempArrayListForDepartment
            ))
            cursor2.close()
            cursor1.moveToNext()
        }
        cursor1.close()
        return arrayListForReturn
    }

    fun removeAllData() {
        val db = this.writableDatabase
        db.delete(table1Name, null, null)
        db.delete(table2Name, null, null)
    }

}