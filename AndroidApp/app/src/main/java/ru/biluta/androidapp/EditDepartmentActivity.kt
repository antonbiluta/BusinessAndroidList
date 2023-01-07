package ru.biluta.androidapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat

class EditDepartmentActivity : AppCompatActivity() {
    private lateinit var editDepartmentName: EditText
    private lateinit var editManagerFio: EditText
    private lateinit var editManagerStartDate: EditText
    private lateinit var editStreetAddress: EditText
    private lateinit var editPostalCode: EditText
    private lateinit var editCity: EditText
    private lateinit var editRegion: EditText
    private lateinit var editCountEmployee: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_department)

        editDepartmentName = findViewById(R.id.editTextDepartmentName)
        editManagerFio = findViewById(R.id.editTextManagerFio)
        editManagerStartDate = findViewById(R.id.editTextManagerStartDate)
        editStreetAddress = findViewById(R.id.editTextStreetAddress)
        editPostalCode = findViewById(R.id.editTextPostalCode)
        editCity = findViewById(R.id.editTextCity)
        editRegion = findViewById(R.id.editTextRegion)
        editCountEmployee = findViewById(R.id.editTextCountEmployee)

        val action = intent.getSerializableExtra("action") as Int

        findViewById<Button>(R.id.button_confirm).setOnClickListener { confirmChanges(action) }

        if (action == 2) {
            editDepartmentName.setText(intent.getSerializableExtra("department") as String)
            editManagerFio.setText(intent.getSerializableExtra("manager") as String)
            editManagerStartDate.setText(intent.getSerializableExtra("managerstartdate") as String)
            editStreetAddress.setText(intent.getSerializableExtra("streetaddress") as String)
            editPostalCode.setText(intent.getSerializableExtra("postalcode") as String)
            editCity.setText(intent.getSerializableExtra("city") as String)
            editRegion.setText(intent.getSerializableExtra("region") as String)
            editCountEmployee.setText(intent.getSerializableExtra("countemployee") as String)
        }
    }

    private fun confirmChanges(action: Int) {
        if (editDepartmentName.text.toString() != "" && editManagerFio.text.toString() != ""
            && editManagerStartDate.text.toString() != "" && editStreetAddress.text.toString() != ""
            && editPostalCode.text.toString() != "" && editCity.text.toString() != ""
            && editRegion.text.toString() != "" && editCountEmployee.text.toString() != "")
        {
            if (isDateValid(editManagerStartDate.text.toString().trim())) {
                val intent = Intent(this@EditDepartmentActivity,
                MainActivity::class.java)
                intent.putExtra("action", action)
                intent.putExtra("department", editDepartmentName.text.toString().trim())
                intent.putExtra("manager", editManagerFio.text.toString().trim())
                intent.putExtra("managerstartdate", editManagerStartDate.text.toString().trim())
                intent.putExtra("streetaddress", editStreetAddress.text.toString().trim())
                intent.putExtra("postalcode", editPostalCode.text.toString().trim().toInt())
                intent.putExtra("city", editCity.text.toString().trim())
                intent.putExtra("region", editRegion.text.toString().trim())
                intent.putExtra("countemployee", editCountEmployee.text.toString().trim().toInt())
                setResult(RESULT_OK, intent)
                finish()
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Проверьте корректность даты.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        } else {
            val toast = Toast.makeText(
                applicationContext,
                "Заполните обязательные поля!",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun isDateValid(date: String?): Boolean {
        val myFormat = SimpleDateFormat("dd.MM.yyyy")
        myFormat.isLenient = false
        return try {
            if (date != null) {
                myFormat.parse(date)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}