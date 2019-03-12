package com.comp3617.assignment2

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import io.realm.Realm
import java.text.SimpleDateFormat
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    var taskName: TextView? = null
    lateinit var dueDate: TextView
    lateinit var cancelBtn: Button
    lateinit var saveBtn: Button
    lateinit var emailBtn: Button
    lateinit var calendarBtn: Button
    lateinit var finishBox: CheckBox
    var taskModel: TaskModel? = null
    private val task = Task(Realm.getDefaultInstance())

    private var ID: Int = 0

    private var hide = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_task)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        val intent = intent
        ID = intent.getIntExtra("taskDetailID", 0)

        taskModel = task.getTask(ID)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val email = prefs.getBoolean("enable_share_task", false)

        emailBtn = findViewById<View>(R.id.emailBtn) as Button
        emailBtn.visibility = if (email) View.VISIBLE else View.GONE

        calendarBtn = findViewById<View>(R.id.reminderBtn) as Button
        calendarBtn.visibility = View.GONE

        taskName = findViewById<View>(R.id.taskName) as TextView
        dueDate = findViewById<View>(R.id.dueDate) as TextView
        cancelBtn = findViewById<View>(R.id.cancelAndDelete) as Button
        saveBtn = findViewById<View>(R.id.save) as Button
        finishBox = findViewById<View>(R.id.finishBox) as CheckBox

        taskName!!.text = taskModel!!.taskName
        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
        dueDate.text = format.format(taskModel!!.dueDate)
        cancelBtn.setText(R.string.cancel)
        finishBox.isChecked = taskModel!!.isFinished

        taskName!!.setOnClickListener(onClickText())
        dueDate.setOnClickListener(onClickDate())
        cancelBtn.setOnClickListener(onCancel())
        saveBtn.setOnClickListener(onSave())
        if (email) {
            emailBtn.setOnClickListener(onCLickEmail())
        }
    }

    fun onClickText(): View.OnClickListener {
        return View.OnClickListener { view ->
            Log.d(getString(R.string.logTag), java.lang.Boolean.toString(hide))
            hideKeyboard(view)
            taskName!!.clearFocus()
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

    fun onCancel(): View.OnClickListener {
        return View.OnClickListener {
            finish()
        }
    }

    fun onSave(): View.OnClickListener {
        return View.OnClickListener {
            val dateText = dueDate.text as String
            val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            try {
                val date = format.parse(dateText)
                val newTask = TaskModel(taskName!!.text.toString(), date)
                task.editTask(newTask, ID)
                task.setFinish(ID, finishBox.isChecked)
                finish()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, R.string.enter_date, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun onCLickEmail(): View.OnClickListener {
        return View.OnClickListener {
            val message = StringBuilder()
            message.append(String.format("%-16s %s%n", "Name:", taskModel!!.taskName))
            message.append(String.format("%-16s %s%n", "Due Date:", taskModel!!.dueDate!!.toString()))
            message.append(String.format("%-16s %s%n", "Completed:", if (taskModel!!.isFinished) "Yes" else "No"))
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Task: " + taskModel!!.taskName!!)
            intent.putExtra(Intent.EXTRA_TEXT, message.toString())
            startActivity(intent)
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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
