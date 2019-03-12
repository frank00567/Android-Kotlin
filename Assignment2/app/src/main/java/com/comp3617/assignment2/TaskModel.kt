package com.comp3617.assignment2

import io.realm.RealmObject
import java.util.*

open class TaskModel : RealmObject {
    var isFinished = false
    var taskName: String? = null
    var dueDate: Date? = null
    var id: Int = 0

    constructor() {}

    constructor(taskName: String, dueDate: Date) {
        this.taskName = taskName
        this.dueDate = dueDate
    }

    fun setTaskModel(task: TaskModel) {
        taskName = task.taskName
        dueDate = task.dueDate
    }
}
