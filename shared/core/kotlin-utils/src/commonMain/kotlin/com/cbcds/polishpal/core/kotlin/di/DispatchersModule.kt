package com.cbcds.polishpal.core.kotlin.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val MAIN_DISPATCHER = "MAIN_DISPATCHER"
const val IO_DISPATCHER = "IO_DISPATCHER"
const val DEFAULT_DISPATCHER = "DEFAULT_DISPATCHER"

val dispatchersModule = module {
    factory(named(MAIN_DISPATCHER)) { Dispatchers.Main }
    factory(named(IO_DISPATCHER)) { Dispatchers.IO }
    factory(named(DEFAULT_DISPATCHER)) { Dispatchers.Default }
}
