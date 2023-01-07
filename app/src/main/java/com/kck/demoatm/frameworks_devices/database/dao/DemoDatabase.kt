package com.kck.demoatm.frameworks_devices.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var instance: DemoDatabase? = null
        private val LOCK = Any()
        const val name = "db_demo"

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDataBase(context).also { instance = it }
        }

        private fun buildDataBase(context: Context): DemoDatabase {
            val name = "db_demo"
            return Room.databaseBuilder(
                context, DemoDatabase::class.java, name
            ).build()
        }
    }
}