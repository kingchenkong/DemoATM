package com.kck.demoatm

import com.kck.demoatm.framworks_devies.database.models.AccountDB

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
const val MOCK_2_ACC_SN = "065-6665-4088" // 065 666 54088
const val MOCK_2_ACC_PWD = "zxcv9173"
const val MOCK_2_ACC_BALANCE = 200000 // 20w

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
const val MOCK_3_ACC_BALANCE = 156000 // 15.6w

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