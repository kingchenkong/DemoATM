package com.kck.demoatm.application

import com.kck.demoatm.frameworks_devices.database.models.AccountDB

const val needMockData: Boolean = true

// MOCK 1
const val MOCK_1_ACC_SN = "001-9876-5432"
const val MOCK_1_ACC_PWD = "qwer6543"
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
const val MOCK_2_ACC_SN = "065-6665-4088"
const val MOCK_2_ACC_PWD = "zxcv9173"
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
const val MOCK_3_ACC_SN = "014-3525-9891"
const val MOCK_3_ACC_PWD = "qazwer55"
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
const val MOCK_4_ACC_SN = "087-5656-5566"
const val MOCK_4_ACC_PWD = "5566nerverdie"
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