package com.rooze.javacon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.rooze.javacon.room.AccountDao
import com.rooze.javacon.room.AccountEntity
import com.rooze.javacon.room.AppDatabase
import com.rooze.javacon.room.dumpData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "AppMainActivity"
    }

    private lateinit var accountDao: AccountDao

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: AppAdapter

    private val compositeDisposable = CompositeDisposable()

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
        val disposable = Observable.zip(
            Observable.create<List<AccountEntity>> { emitter ->
                emitter.onNext(accountDao.getAll())
                emitter.onComplete()
            }, Observable.create<List<AccountEntity>> { emitter ->
                emitter.onNext(accountDao.getAll())
                emitter.onComplete()
            }
        ) { list1, list2 ->
            if (list1.size > list2.size) {
                list1
            } else {
                list2
            }
        }.flatMap { list ->
            Observable.create { emitter ->
                emitter.onNext(list.take(5))
                SystemClock.sleep(3000)
                emitter.onNext(list)
            }
        }.filter { list ->
            list.size > 10
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                Log.i(TAG, "loadData: ${list.size}")
                adapter.updateList(list)
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }, { throwable -> Log.e(TAG, "loadData: ", throwable) })
        compositeDisposable.add(disposable)
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}