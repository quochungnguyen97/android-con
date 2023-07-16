package com.rooze.javacon.room

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

fun dumpData(accountDao: AccountDao, context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        repeat(10000) {
            accountDao.upsert(
                AccountEntity(
                    username = randomString(),
                    password = randomString(),
                )
            )
        }
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Dump done", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun randomString(): String {
    val rand = Random()
    val len = rand.nextInt(10)
    val str = StringBuilder()
    repeat(10) {
        str.append('a' + rand.nextInt(25))
    }
    repeat(len) {
        str.append('a' + rand.nextInt(25))
    }
    return str.toString()
}