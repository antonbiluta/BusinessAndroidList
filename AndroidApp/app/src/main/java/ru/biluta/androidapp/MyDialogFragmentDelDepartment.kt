package ru.biluta.androidapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialogFragmentDelDepartment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments: Bundle? = arguments
        val departmentName = arguments?.getString("department")
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Будет удалено подразделение компании: $departmentName")
            .setTitle("Внимание!")
            .setPositiveButton("Продолжить")
            { _, _ -> (activity as MainActivity?)?.delDepartment() }
            .setNegativeButton("Отмена") { _, _ -> }
        return builder.create()
    }
}