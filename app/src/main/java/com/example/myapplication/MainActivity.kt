package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Get `PackageManager`
        val packageManager = getPackageManager()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
       val apps = packageManager.queryIntentActivities(i, 0)

       val appsHaveBeenFiltered = apps.filter {
            it.activityInfo.packageName != "com.example.myapplication"
        }

       // Configuration for list
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ApplicationAdapter(appsHaveBeenFiltered, packageManager )

        // Floating button config
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Yang bener aja, Rugi dong", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
            // https://www.geeksforgeeks.org/floating-action-button-fab-in-android-with-example/  => next to implement more button
        }

    }

    override fun onBackPressed() {}

    class ApplicationAdapter(private val applications: List<ResolveInfo>, private val packageManager: PackageManager ) :
        RecyclerView.Adapter<ApplicationAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_application,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val application = applications[position]
            holder.tvName.text = application.loadLabel(packageManager)
            holder.ivIcon.setImageDrawable(application.loadIcon(packageManager))
            holder.itemView.setOnClickListener {
                val intent = packageManager.getLaunchIntentForPackage(application.activityInfo.packageName)
                it.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return applications.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        }

    }
}