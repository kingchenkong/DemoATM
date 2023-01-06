package com.kck.demoatm.framworks_devies.database.models

import androidx.room.Entity
import com.kck.demoatm.ACCOUNT_BALANCE_DEF
import com.kck.demoatm.ACCOUNT_DISPLAY_NAME_DEF
import com.kck.demoatm.ACCOUNT_PASSWORD_DEF
import com.kck.demoatm.ACCOUNT_SERIAL_NUM_DEF

@Entity(tableName = "account", primaryKeys = ["id"])
data class AccountDB(
    val id: Int,
    val serialNumber: String,
    val password: String,
    val balance: Int,
    val displayName: String,
) {
    override fun toString(): String {
        return "AccountDB (id=$id, serialNumber='$serialNumber', password='$password', balance=$balance, displayName='$displayName' )"
    }

    companion object {
        val defaultAccountDB = AccountDB(
            id = 0,
            serialNumber = ACCOUNT_SERIAL_NUM_DEF,
            password = ACCOUNT_PASSWORD_DEF,
            balance = ACCOUNT_BALANCE_DEF,
            displayName = ACCOUNT_DISPLAY_NAME_DEF,
        )
    }
}
