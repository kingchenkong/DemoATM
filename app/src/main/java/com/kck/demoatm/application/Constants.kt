package com.kck.demoatm.application

const val ACC_SN_DEF: String = "001-1234-5678"
const val ACC_PWD_DEF: String = "asdf1234"
const val ACC_BALANCE_DEF: Int = 0

const val ACC_DISPLAY_NAME_DEF: String = "default_name"

enum class SourceType { LOCAL, REMOTE }

enum class OperateError {
    LOGIN_FAIL, // 登入失敗
    BALANCE_NOT_ENOUGH, // 餘額不足
    UPDATE_FAIL, // 更新 AccountDb 失敗

    REMOTE_SOURCE_NOT_FOUND, // 沒有 remote data source
}

const val ERROR_MSG_LOGIN: String = "Login fail."
const val ERROR_MSG_ACC_NOT_FOUND: String = "Login fail."
const val ERROR_MSG_REMOTE_NOT_FOUND: String = "Remote source not found."
const val ERROR_MSG_BALANCE: String = "Balance not enough."
const val ERROR_MSG_UPDATE: String = "Update fail."
const val ERROR_MSG_SAME_ACC: String = "Transfer to Same Account."
