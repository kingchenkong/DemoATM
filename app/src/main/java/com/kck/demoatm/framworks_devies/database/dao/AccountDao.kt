package com.kck.demoatm.framworks_devies.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kck.demoatm.framworks_devies.database.models.AccountDB

@Dao
interface AccountDao {

    @Query("SELECT * FROM account")
    suspend fun getAll(): List<AccountDB>

    @Query("SELECT * FROM account WHERE serialNumber == :serialNumber AND password == :password")
    suspend fun getAccount(
        serialNumber: String,
        password: String,
    ): AccountDB

    @Query("SELECT * FROM account WHERE serialNumber == :serialNumber AND password == :password")
    suspend fun login(
        serialNumber: String,
        password: String,
    ): AccountDB

    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getAccountById(
        id: Int
    ): AccountDB

    @Insert
    suspend fun addAccount(account: AccountDB)

    @Update
    suspend fun updateAccount(account: AccountDB)
}