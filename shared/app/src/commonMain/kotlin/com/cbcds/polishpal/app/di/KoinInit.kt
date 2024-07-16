package com.cbcds.polishpal.app.di

import kotlinx.coroutines.CoroutineScope
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    globalCoroutineScope: CoroutineScope,
    nativeAppModule: Module? = null,
    appDeclaration: KoinAppDeclaration,
) {
    startKoin {
        appDeclaration()

        modules(
            listOfNotNull(
                buildCommonAppModule(globalCoroutineScope),
                nativeAppModule,
            )
        )
    }
}
