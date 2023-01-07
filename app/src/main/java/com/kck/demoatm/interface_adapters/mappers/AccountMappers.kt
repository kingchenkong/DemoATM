package com.kck.demoatm.interface_adapters.mappers

import com.kck.demoatm.entities.Account
import com.kck.demoatm.frameworks_devices.database.models.AccountDB


/**
 * Account Mappers
 */
fun AccountDB.toEntity(): Account {
    return Account(
        id = id,
        serialNumber = serialNumber,
        password = password,
        balance = balance,
    )
}