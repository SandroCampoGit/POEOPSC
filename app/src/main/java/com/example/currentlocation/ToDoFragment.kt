package com.example.currentlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.util.ArrayList

class ToDoFragment : Fragment() {

    private lateinit var tasksListView: ListView
    private lateinit var addTaskButton: Button
    private lateinit var clearTasksButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var completionTextView: TextView

    private val tasks = ArrayList<String>()
    private val tasksCompleted = ArrayList<Boolean>()
    private var tasksAdapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.todo, container, false) // Make sure to use the correct layout

        tasksListView = view.findViewById(R.id.tasksListView)
        addTaskButton = view.findViewById(R.id.addTaskButton)
        clearTasksButton = view.findViewById(R.id.clearTasksButton)
        progressBar = view.findViewById(R.id.progressBar)
        completionTextView = view.findViewById(R.id.completionTextView)

        // Adding default task
        tasks.add("Spot a bird within five km/miles")
        tasksCompleted.add(false)
        updateProgress()

        tasksAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, tasks)
        tasksListView.adapter = tasksAdapter
        tasksListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        addTaskButton.setOnClickListener {
            // Create an AlertDialog builder with the context of the parent activity
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Add New Task")

            // Set up the input with the context of the parent activity
            val input = EditText(requireActivity())
            input.hint = "Enter task name"
            builder.setView(input)

            // Set up the buttons
            builder.setPositiveButton("OK") { dialog, _ ->
                val taskName = input.text.toString()
                if (taskName.isNotEmpty()) {
                    tasks.add(taskName)
                    tasksCompleted.add(false)
                    tasksAdapter?.notifyDataSetChanged()
                    updateProgress()
                }
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            // Show the AlertDialog
            builder.show()
        }

        clearTasksButton.setOnClickListener {
            tasks.clear()
            tasksCompleted.clear()
            tasksAdapter?.notifyDataSetChanged()

            // Reset the checked states for all items in the ListView
            for (i in 0 until tasksListView.count) {
                tasksListView.setItemChecked(i, false)
            }

            updateProgress()
        }

        tasksListView.setOnItemClickListener { _, _, position, _ ->
            tasksCompleted[position] = !tasksCompleted[position]
            updateProgress()
        }

        return view
    }

    private fun updateProgress() {
        val totalTasks = tasks.size
        val completedTasks = tasksCompleted.count { it }
        progressBar.max = totalTasks
        progressBar.progress = completedTasks

        if (completedTasks == totalTasks && totalTasks > 0) {
            completionTextView.text = "Well done, you are up to date with your to-do list!"
        } else {
            completionTextView.text = ""
        }
    }
}
