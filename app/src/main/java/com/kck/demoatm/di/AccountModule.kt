package com.kck.demoatm.di

import com.kck.demoatm.framworks_devies.data_source.local.AccountLocalDataSourceImpl
import com.kck.demoatm.framworks_devies.data_source.local.IAccountLocalDataSource
import com.kck.demoatm.framworks_devies.database.data_provider.DatabaseProviderImpl
import com.kck.demoatm.framworks_devies.database.data_provider.IDatabaseProvider
import com.kck.demoatm.interface_adapters.repositories.AccountRepositoryImpl
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository
import com.kck.demoatm.use_cases.*
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.math.sin

val DataProviderModule = module {
    single { DatabaseProviderImpl(get()) } bind IDatabaseProvider::class

}

val DataSourceModule = module {
    single { AccountLocalDataSourceImpl() } bind IAccountLocalDataSource::class
}

val RepositoryModule = module {
    single { AccountRepositoryImpl() } bind IAccountRepository::class
}

val UseCaseModule = module {
    single { DepositUseCase() } bind DepositUseCase::class
    single { GetAccountUseCase() } bind GetAccountUseCase::class
    single { LoginUseCase() } bind LoginUseCase::class
    single { QueryBalanceUseCase() } bind QueryBalanceUseCase::class
    single { TransferUseCase() } bind TransferUseCase::class
    single { WithdrawUseCase() } bind WithdrawUseCase::class
}