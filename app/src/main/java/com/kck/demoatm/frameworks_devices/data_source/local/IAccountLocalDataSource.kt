package com.kck.demoatm.frameworks_devices.data_source.local

import com.kck.demoatm.entities.Account
import com.kck.demoatm.frameworks_devices.data_source.IAccountDataSource

interface IAccountLocalDataSource : IAccountDataSource {
    suspend fun getAllAccount(): List<Account>
}