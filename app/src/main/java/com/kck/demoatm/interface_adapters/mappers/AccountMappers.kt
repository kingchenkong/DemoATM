package com.kck.demoatm.interface_adapters.mappers

import com.kck.demoatm.application.ACC_DISPLAY_NAME_DEF
import com.kck.demoatm.entities.Account
import com.kck.demoatm.frameworks_devices.database.models.AccountDB


/**
 * DB to Entity Mappers
 */
fun AccountDB.toEntity(): Account {
    return Account(
        id = id,
        serialNumber = serialNumber,
        password = password,
        balance = balance,
    )
}

/**
 * Entity to DB Mappers
 */
fun Account.toDB(): AccountDB {
    return AccountDB(
        id = id,
        serialNumber = serialNumber,
        password = password,
        balance = queryBalance(),
        displayName = ACC_DISPLAY_NAME_DEF,
        creditCardId = 0,
        periodDeductionContractId = 0,
        foreignCurrencyAccountId = 0
    )
}