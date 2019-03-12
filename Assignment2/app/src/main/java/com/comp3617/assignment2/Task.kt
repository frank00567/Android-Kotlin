package com.comp3617.assignment2

import android.util.Log
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.ArrayList

class Task(private val realm: Realm) {

    val allTask: ArrayList<TaskModel>
        get() {
            val tasks = ArrayList<TaskModel>()
            for (task in realm.where<TaskModel>().findAll()){
                if (task.taskName != null) {
                    tasks.add(task)
                }
            }
            return tasks
        }

    fun getTask(ID: Int): TaskModel? {
        return realm.where<TaskModel>().equalTo("id", ID).findFirst()
    }

    fun addNewTask(task: TaskModel) {
        realm.executeTransaction { realm ->
            val nextID = realm.where<TaskModel>().count().toInt()
            val newTask = realm.createObject<TaskModel>()
            newTask.taskName = task.taskName
            newTask.dueDate = task.dueDate
            newTask.isFinished = task.isFinished
            newTask.id = nextID
            Log.d("Frank", newTask.toString())
        }
    }

    fun editTask(task: TaskModel, ID: Int) {
        val updateTask = realm.where<TaskModel>().equalTo("id", ID).findFirst()

        realm.executeTransaction { updateTask!!.setTaskModel(task) }
    }

    fun setFinish(ID: Int, isfinish: Boolean) {
        val updateTask = realm.where<TaskModel>().equalTo("id", ID).findFirst()
        realm.executeTransaction { updateTask!!.isFinished = isfinish }
    }

    fun deleteTask(ID: Int) {
        realm.executeTransaction { realm ->
            realm.where<TaskModel>().equalTo("id", ID).findFirst()!!.deleteFromRealm()
        }
    }
}
