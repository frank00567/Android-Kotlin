package com.comp3617.assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import io.realm.Realm
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    var taskName: TextView? = null
    var dueDate: TextView? = null
    lateinit var saveBtn: Button
    lateinit var cancelBtn: Button
    lateinit var emailBtn: Button
    lateinit var calendarBtn: Button
    val task = Task(Realm.getDefaultInstance())

    internal var hide = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_task)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val calendar = prefs.getBoolean("enable_addTo_calendar", false)

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        val finishBox = findViewById<View>(R.id.finishBox) as CheckBox
        finishBox.visibility = View.GONE

        emailBtn = findViewById<View>(R.id.emailBtn) as Button
        emailBtn.visibility = View.GONE

        calendarBtn = findViewById<View>(R.id.reminderBtn) as Button
        calendarBtn.visibility = if (calendar) View.VISIBLE else View.GONE

        taskName = findViewById<View>(R.id.taskName) as TextView
        dueDate = findViewById<View>(R.id.dueDate) as TextView
        saveBtn = findViewById<View>(R.id.save) as Button
        cancelBtn = findViewById<View>(R.id.cancelAndDelete) as Button


        val layoutParams = taskName!!.layoutParams
        cancelBtn.setText(R.string.cancel)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        taskName!!.layoutParams = layoutParams

        taskName!!.setOnClickListener(onClickText())
        dueDate!!.setOnClickListener(onClickDate())
        saveBtn.setOnClickListener(onClickSave())
        cancelBtn.setOnClickListener(onClickCancel())


        if (calendar) {
            calendarBtn.setOnClickListener(onCLickCalendar())
        }
    }

    fun onClickText(): View.OnClickListener {
        return View.OnClickListener { view ->
            Log.d(getString(R.string.logTag), java.lang.Boolean.toString(hide))
            hideKeyboard(view)
            taskName!!.clearFocus()
        }
    }

    fun onClickSave(): View.OnClickListener {
        return View.OnClickListener {
            val dateText = dueDate!!.text as String
            val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            try {
                val date = format.parse(dateText)
                val newTask = TaskModel(taskName!!.text.toString(), date)
                task.addNewTask(newTask)
                finish()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, R.string.enter_date, Toast.LENGTH_LONG).show()
                Log.d(getString(R.string.logTag), e.message)
            }
        }
    }

    fun onClickDate(): View.OnClickListener {
        return View.OnClickListener {
            val bundle = Bundle()
            bundle.putInt("dueDateLbl", R.id.dueDate)
            val dialogFragment = DatePickerFragment()
            dialogFragment.arguments = bundle
            dialogFragment.show(supportFragmentManager, "datePicker")
        }
    }

    fun onClickCancel(): View.OnClickListener {
        return View.OnClickListener { finish() }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun onCLickCalendar(): View.OnClickListener {
        return View.OnClickListener {
            val dateText = dueDate!!.text as String
            val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            try {
                val date = format.parse(dateText)
                val calendar = Calendar.getInstance()
                calendar.time = date
                val intent = Intent(Intent.ACTION_EDIT)
                intent.type = "vnd.android.cursor.item/event"
                intent.putExtra("beginTime", calendar.timeInMillis)
                intent.putExtra("allDay", false)
                intent.putExtra("endTime", calendar.timeInMillis)
                intent.putExtra("title", taskName!!.text)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(applicationContext, R.string.enter_date, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
