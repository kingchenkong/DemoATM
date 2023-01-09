package com.kck.demoatm.use_cases

class CheckDuplicateUseCase {
    fun invoke(
        sourceSN: String,
        destinationSN: String
    ): Boolean {
        return sourceSN == destinationSN
    }
}