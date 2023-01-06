package com.kck.demoatm

import android.app.Application
import android.util.Log
import com.kck.demoatm.di.RepositoryModule
import com.kck.demoatm.di.DataProviderModule
import com.kck.demoatm.di.DataSourceModule
import com.kck.demoatm.di.UseCaseModule
import com.kck.demoatm.framworks_devies.database.data_provider.DatabaseProviderImpl
import com.kck.demoatm.framworks_devies.database.data_provider.IDatabaseProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.e("MyApplication", "onCreate: ")
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)

            modules(
                DataProviderModule,
                DataSourceModule,
                RepositoryModule,
                UseCaseModule,
            )
        }

        GlobalScope.launch {
            // db init
            val db: IDatabaseProvider = DatabaseProviderImpl(this@MyApplication)
            db.initialize()
        }
    }
}