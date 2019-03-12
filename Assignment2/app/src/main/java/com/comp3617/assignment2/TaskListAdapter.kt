package com.comp3617.assignment2

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import io.realm.Realm

class TaskListAdapter(private val ctx: Context, private val taskModelList: List<TaskModel>) :
    ArrayAdapter<TaskModel>(ctx, 0, taskModelList) {
    private val task = Task(Realm.getDefaultInstance())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView: View
        val taskModel = taskModelList[position]
        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.list_prototype_layout, parent, false)
        } else {
            rowView = convertView
        }
        val taskName = rowView.findViewById<View>(R.id.list_nameLbl) as TextView
        val startDate = rowView.findViewById<View>(R.id.list_dateLbl) as TextView
        val checkBox = rowView.findViewById<View>(R.id.list_checkBox) as CheckBox
        taskName.text = taskModel.taskName
        startDate.text = taskModel.dueDate!!.toString()
        checkBox.isChecked = taskModel.isFinished

        checkBox.setOnClickListener { task.setFinish(taskModel.id, !taskModel.isFinished) }

        rowView.setOnClickListener { view ->
            val intent = Intent(view.context, EditTaskActivity::class.java)
            intent.putExtra("taskDetailID", taskModel.id)
            ctx.startActivity(intent)
        }

        rowView.setOnLongClickListener{view ->
            val fragment = DeleteDialogFragment()
            val fg = (view.context as AppCompatActivity).supportFragmentManager
            val bundle = Bundle()
            bundle.putInt("ID", taskModel.id)
            fragment.arguments = bundle
            fragment.show(fg, "delete")
            true
        }

        return rowView
    }

    class DeleteDialogFragment : DialogFragment() {
        private val task = Task(Realm.getDefaultInstance())
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val ID = arguments!!.getInt("ID")
            val builder = AlertDialog.Builder(activity!!)

            builder.setTitle(R.string.Confirm)
            builder.setMessage(R.string.are_u_sure)

            builder.setPositiveButton(R.string.Yes) { dialog, _ ->
                task.deleteTask(ID)
                dialog.dismiss()
                activity!!.recreate()
            }
            builder.setNegativeButton(R.string.No) { dialog, _ -> dialog.dismiss() }
            return builder.create()
        }
    }
}

