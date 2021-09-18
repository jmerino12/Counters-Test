package com.cornershop.counterstest

import android.app.Application
import com.cornershop.counterstest.data.database.CounterDatabase
import com.cornershop.counterstest.data.database.RoomDataSource
import com.cornershop.counterstest.data.server.TheCounterDb
import com.cornershop.counterstest.data.server.TheCounterDbDataSource
import com.cornershop.counterstest.ui.addcounter.AddCounter
import com.cornershop.counterstest.ui.addcounter.AddCounterViewModel
import com.cornershop.counterstest.ui.main.MainScreen
import com.cornershop.counterstest.ui.main.MainViewModel
import com.jmb.data.repository.CounterRepository
import com.jmb.data.source.LocalDataSource
import com.jmb.data.source.RemoteDataSource
import com.jmb.usecases.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, scopesModule))
    }
}

private val appModule = module {
    single { CounterDatabase.build(get()) }
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> { TheCounterDbDataSource(get()) }
    single(named("baseUrl")) { "http://192.168.0.16:3000/api/v1/" }
    single { TheCounterDb(get(named("baseUrl"))) }
}

val dataModule = module {
    factory { CounterRepository(get(), get()) }
}

private val scopesModule = module {
    scope(named<MainScreen>()) {
        viewModel { MainViewModel(get(), get(), get(), get()) }
        scoped { GetCounters(get()) }
        scoped { IncreseCounter(get()) }
        scoped { DecreseCounter(get()) }
        scoped { DeleteCounter(get()) }
    }

    scope(named<AddCounter>()) {
        viewModel { AddCounterViewModel(get()) }
        scoped { AddProduct(get()) }
    }
}