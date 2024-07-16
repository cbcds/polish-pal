package com.cbcds.polishpal.di

import androidx.lifecycle.ProcessLifecycleOwner
import org.koin.dsl.module

val androidAppModule = module {
    single { ProcessLifecycleOwner.get() }
}
