package com.cbcds.polishpal.app.di

import com.cbcds.polishpal.feature.settings.model.AppInfo
import kotlinx.coroutines.CoroutineScope
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    appInfo: AppInfo,
    globalCoroutineScope: CoroutineScope,
    nativeAppModule: Module? = null,
    appDeclaration: KoinAppDeclaration,
) {
    startKoin {
        appDeclaration()

        modules(
            listOfNotNull(
                buildCommonAppModule(
                    appInfo,
                    globalCoroutineScope,
                ),
                nativeAppModule,
            )
        )
    }
}
