package com.kck.demoatm.frameworks_devices.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kck.demoatm.frameworks_devices.database.models.AccountDB

@Database(
    entities = [
        AccountDB::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class DemoDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}