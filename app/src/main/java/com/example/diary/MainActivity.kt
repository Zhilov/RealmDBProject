package com.example.diary

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.internal.async.RealmThreadPoolExecutor

class MainActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var editT: EditText
    lateinit var config: RealmConfiguration
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        config = RealmConfiguration.Builder()
            .name("diary.db")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()

        recyclerView = findViewById(R.id.recyclerview)
        button = findViewById(R.id.button)
        editT = findViewById(R.id.editT)

        taskAdapter = TaskAdapter(this, retrieveTasks())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = taskAdapter

        button.setOnClickListener {
            if (!editT.text.isEmpty()) {
                Realm.getInstance(config).executeTransaction() { realmTransaction ->
                    val task = Task(
                        date_start = "1",
                        date_finish = "2",
                        name = editT.text.toString(),
                        description = "This is first"
                    )
                    realmTransaction.insert(task)
                }

                taskAdapter = TaskAdapter(this, retrieveTasks())
                recyclerView.adapter = taskAdapter
                editT.text.clear()
            }
        }
    }

    private fun mapTask(task: Task): Task {
        return Task(
           id = task.id,
           date_start = task.date_start,
           date_finish = task.date_finish,
           name = task.name,
           description = task.description
        )
    }

    fun retrieveTasks(): ArrayList<Task> {

        val taskList = mutableListOf<Task>()

        Realm.getInstance(config).executeTransaction { realmTransaction ->
            taskList.addAll(realmTransaction
                .where(Task::class.java)
                .findAll()
                .map {
                    mapTask(it)
                }
            )
        }
        return ArrayList(taskList)
    }
}