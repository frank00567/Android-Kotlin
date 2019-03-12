package com.comp3617.assignment2

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_main.*

class DisplayActivity : AppCompatActivity() {

    lateinit var taskListView : ListView
    lateinit var realm : Realm
    lateinit var task_adapter : TaskListAdapter
    lateinit var task : Task
    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        realm = Realm.getDefaultInstance()
        task = Task(realm)

        taskListView = findViewById(R.id.list_view)
        task_adapter = TaskListAdapter(this, task.allTask)
        taskListView.adapter = task_adapter

        var fab : FloatingActionButton = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            addTask()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if(id == R.id.add_new_task){
            addTask()
            return true
        }else if(id == R.id.action_settings){
            var intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            return true
        }

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        task_adapter = TaskListAdapter(this, task.allTask)
        taskListView.adapter = task_adapter
    }

    fun addTask(){
        var intent = Intent(this, AddTaskActivity::class.java)
        startActivity(intent)
    }
}
