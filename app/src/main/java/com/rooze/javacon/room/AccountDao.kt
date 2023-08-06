package com.rooze.javacon.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AccountDao {
    @Upsert
    fun upsert(account: AccountEntity)

    @Upsert
    fun upsert(accounts: List<AccountEntity>)

    @Query("SELECT * FROM account")
    fun getAll(): List<AccountEntity>

    @Query("DELETE FROM account WHERE id = :id")
    fun delete(id: Long)
}