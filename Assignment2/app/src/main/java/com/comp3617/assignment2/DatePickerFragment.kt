package com.comp3617.assignment2

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private var dueDateLblID: Int = 0
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        dueDateLblID = arguments!!.getInt("dueDateLbl")
        return DatePickerDialog(activity!!, this, year, month, day)
    }

    override fun onDateSet(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(i, i1, i2, 8, 0, 0)
        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
        val dueDate = activity!!.findViewById<TextView>(dueDateLblID)
        dueDate.text = format.format(calendar.time)
    }
}