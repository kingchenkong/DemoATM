package com.kck.demoatm.application

import com.kck.demoatm.frameworks_devices.database.models.AccountDB

const val needMockData: Boolean = true

// MOCK 1
const val MOCK_1_ACC_SN = "mock1"
const val MOCK_1_ACC_PWD = "1234"
const val MOCK_1_ACC_BALANCE = 1000

fun getAccountDBMock1(): AccountDB {
    return AccountDB(
        0,
        serialNumber = MOCK_1_ACC_SN,
        password = MOCK_1_ACC_PWD,
        balance = MOCK_1_ACC_BALANCE,
        displayName = "MOCK 1",
        creditCardId = 0,
        periodDeductionContractId = 0,
        foreignCurrencyAccountId = 0
    )
}

// MOCK 2
const val MOCK_2_ACC_SN = "mock2"
const val MOCK_2_ACC_PWD = "2468"
const val MOCK_2_ACC_BALANCE = 2000

fun getAccountDBMock2(): AccountDB {
    return AccountDB(
        0,
        serialNumber = MOCK_2_ACC_SN,
        password = MOCK_2_ACC_PWD,
        balance = MOCK_2_ACC_BALANCE,
        displayName = "MOCK 2",
        creditCardId = 0,
        periodDeductionContractId = 0,
        foreignCurrencyAccountId = 0
    )
}

// MOCK 3
const val MOCK_3_ACC_SN = "mock3"
const val MOCK_3_ACC_PWD = "37666"
const val MOCK_3_ACC_BALANCE = 3000

fun getAccountDBMock3(): AccountDB {
    return AccountDB(
        0,
        serialNumber = MOCK_3_ACC_SN,
        password = MOCK_3_ACC_PWD,
        balance = MOCK_3_ACC_BALANCE,
        displayName = "MOCK 3",
        creditCardId = 0,
        periodDeductionContractId = 0,
        foreignCurrencyAccountId = 0
    )
}

// MOCK 4
const val MOCK_4_ACC_SN = "mock4"
const val MOCK_4_ACC_PWD = "55667788"
const val MOCK_4_ACC_BALANCE = 4000

fun getAccountDBMock4(): AccountDB {
    return AccountDB(
        0,
        serialNumber = MOCK_4_ACC_SN,
        password = MOCK_4_ACC_PWD,
        balance = MOCK_4_ACC_BALANCE,
        displayName = "MOCK 4",
        creditCardId = 0,
        periodDeductionContractId = 0,
        foreignCurrencyAccountId = 0
    )
}