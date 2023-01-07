package com.kck.demoatm.framworks_devies.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kck.demoatm.ACC_BALANCE_DEF
import com.kck.demoatm.ACC_DISPLAY_NAME_DEF
import com.kck.demoatm.ACC_PWD_DEF
import com.kck.demoatm.ACC_SN_DEF

const val TABLE_ACCOUNT_NAME = "account"

@Entity(
    tableName = TABLE_ACCOUNT_NAME
)
data class AccountDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val serialNumber: String,
    val password: String,
    var balance: Int,

    val displayName: String,
    val creditCardId: Int, // 信用卡 ID
    val periodDeductionContractId: Int, // 固定扣款 合約 ID
    val foreignCurrencyAccountId: Int, // 外幣帳戶 ID
) {

    companion object {
        val defaultAccountDB = AccountDB(
            id = 0,
            serialNumber = ACC_SN_DEF,
            password = ACC_PWD_DEF,
            balance = ACC_BALANCE_DEF,

            displayName = ACC_DISPLAY_NAME_DEF,
            creditCardId = 0,
            periodDeductionContractId = 0,
            foreignCurrencyAccountId = 0
        )
    }
}
