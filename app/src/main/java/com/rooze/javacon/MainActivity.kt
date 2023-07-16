package com.rooze.javacon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.rooze.javacon.room.AccountDao
import com.rooze.javacon.room.AppDatabase
import com.rooze.javacon.room.dumpData

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var accountDao: AccountDao

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initDatabase()
        loadData()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.list)
        progressBar = findViewById(R.id.progress_circular)
        adapter = AppAdapter()
        recyclerView.adapter = adapter
    }

    private fun initDatabase() {
        accountDao = AppDatabase.getInstance(applicationContext).accountDao()
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        Thread {
            val list = accountDao.getAll()
            Handler(Looper.getMainLooper()).post {
                adapter.updateList(list)
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dump_data -> {
                dumpData(accountDao, this)
                true
            }
            R.id.refresh -> {
                loadData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}