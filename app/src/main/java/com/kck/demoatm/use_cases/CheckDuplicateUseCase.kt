package com.kck.demoatm.use_cases

import android.util.Log

class CheckDuplicateUseCase {
    private val TAG: String = CheckDuplicateUseCase::class.java.simpleName
    fun invoke(
        sourceSN: String,
        destinationSN: String
    ): Boolean {
        Log.d(TAG, "invoke: source: $sourceSN, dest: $destinationSN")
        return sourceSN == destinationSN
    }
}