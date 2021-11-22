package com.example.diary
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults


class TaskAdapter (private val context: Context?, private val taskList: ArrayList<Task>):RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {

    lateinit var config: RealmConfiguration

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val textDesc: TextView = itemView.findViewById(R.id.text_desc)
        val textDStart: TextView = itemView.findViewById(R.id.date_start)
        val textDFinish: TextView = itemView.findViewById(R.id.date_finish)
        val btnDelete: ImageView = itemView.findViewById(R.id.image_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.task_view, parent, false)
        Realm.init(context)
        config = RealmConfiguration.Builder()
            .name("diary.db")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.textName.text = taskList[position].name
        holder.textDesc.text = taskList[position].description
        holder.textDStart.text = taskList[position].date_start

        holder.btnDelete.setOnClickListener {
            Realm.getInstance(config).executeTransaction(Realm.Transaction { realm ->
                val result: RealmResults<Task> =
                    realm.where(Task::class.java).equalTo("id", taskList[position].id).findAll()
                result.deleteAllFromRealm()
            })
            taskList.removeAt(position)
            notifyDataSetChanged()
        }
//        MainActivity().runOnUiThread(Runnable {
//            notifyDataSetChanged()
//        })
    }

}
