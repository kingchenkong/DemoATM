package com.kck.demoatm.framworks_devies.data_source.local

import com.kck.demoatm.entities.Account
import com.kck.demoatm.framworks_devies.data_source.IAccountDataSource

interface IAccountLocalDataSource : IAccountDataSource {
    suspend fun getAllAccount(): List<Account>
}