package ru.biluta.androidapp;

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi

class DepartmentDetailsDialogFragment: android.app.DialogFragment() {
    private val exceptionTag = "DepDetailsDialogFrag"

    interface OnInputListenerSortId {
        fun sendInputSortId(sortId: Int)
    }

    lateinit var onInputListenerSortId: OnInputListenerSortId

    private lateinit var textViewDepartmentName: TextView
    private lateinit var textViewManagerFioName: TextView
    private lateinit var textViewManagerStartDate: TextView
    private lateinit var textViewStreetAddress: TextView
    private lateinit var textViewPostalCode: TextView
    private lateinit var textViewCity: TextView
    private lateinit var textViewRegion: TextView
    private lateinit var textViewCountEmployee: TextView
    private lateinit var buttonDel: Button
    private lateinit var buttonEdit: Button
    private lateinit var buttonOk: Button

    private var currentIdForSort: Int = -1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
        val view: View = inflater!!.inflate(R.layout.department_details, container, false)
        textViewDepartmentName = view.findViewById(R.id.textViewDepartmentName)
        textViewManagerFioName = view.findViewById(R.id.textViewManagerFioName)
        textViewManagerStartDate = view.findViewById(R.id.textViewManagerStartDate)
        textViewStreetAddress = view.findViewById(R.id.textViewStreetAddress)
        textViewPostalCode = view.findViewById(R.id.textViewPostalCode)
        textViewCity = view.findViewById(R.id.textViewCity)
        textViewRegion = view.findViewById(R.id.textViewRegion)
        textViewCountEmployee = view.findViewById(R.id.textViewCountEmployee)
        buttonDel = view.findViewById(R.id.button_details_delete)
        buttonEdit = view.findViewById(R.id.button_details_edit)
        buttonOk = view.findViewById(R.id.button_details_ok)

        textViewDepartmentName.setOnLongClickListener { setSortId(0) }
        textViewManagerFioName.setOnLongClickListener { setSortId(1) }
        textViewManagerStartDate.setOnLongClickListener { setSortId(2) }
        textViewStreetAddress.setOnLongClickListener { setSortId(3) }
        textViewPostalCode.setOnLongClickListener { setSortId(4) }
        textViewCity.setOnLongClickListener { setSortId(5) }
        textViewRegion.setOnLongClickListener { setSortId(6) }
        textViewCountEmployee.setOnLongClickListener { setSortId(7) }

        buttonDel.setOnClickListener { returnDel() }
        buttonEdit.setOnClickListener { returnEdit() }
        buttonOk.setOnClickListener { returnIdForSort() }

        val arguments: Bundle = getArguments()
        textViewDepartmentName.text = arguments.getString("departmentName")
        textViewManagerFioName.text = arguments.getString("managerFio")
        textViewManagerStartDate.text = arguments.getString("manageStartDate")
        textViewStreetAddress.text = arguments.getString("streetAddress")
        textViewPostalCode.text = arguments.getString("postalCode")
        textViewCity.text = arguments.getString("city")
        textViewRegion.text = arguments.getString("region")
        textViewCountEmployee.text = arguments.getString("countEmployee")

        if (arguments.getString("connection") != "1") {
            buttonDel.isEnabled = false
            buttonEdit.isEnabled = false
        }

        return view
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            onInputListenerSortId = getActivity() as OnInputListenerSortId
        } catch (e: ClassCastException) {
            Log.e(exceptionTag, "onAttach: ClassCastException: " + e.message)
        }
    }

    private fun setSortId(id: Int): Boolean {
        currentIdForSort = id
        return true
    }

    private fun returnIdForSort() {
        onInputListenerSortId.sendInputSortId(currentIdForSort)
        dialog.dismiss()
    }

    private fun returnDel() {
        currentIdForSort = 8
        returnIdForSort()
    }

    private fun returnEdit() {
        currentIdForSort = 9
        returnIdForSort()
    }
}
