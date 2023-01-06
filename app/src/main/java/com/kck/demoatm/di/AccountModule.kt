package com.kck.demoatm.di

import com.kck.demoatm.framworks_devies.database.data_provider.DatabaseProviderImpl
import com.kck.demoatm.framworks_devies.database.data_provider.IDatabaseProvider
import org.koin.dsl.bind
import org.koin.dsl.module

val AccountModule = module {
    single { DatabaseProviderImpl(get()) } bind IDatabaseProvider::class

}