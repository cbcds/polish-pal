package com.cbcds.polishpal

import android.app.Application
import com.cbcds.polishpal.app.di.initKoin
import com.cbcds.polishpal.di.androidAppModule
import com.cbcds.polishpal.feature.settings.notifications.NotificationChannelCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext

class PolishPalApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        initKoin()

        createNotificationChannels()
    }

    private fun initKoin() {
        initKoin(
            PolishPalAppInfo(),
            applicationScope,
            androidAppModule,
        ) {
            androidContext(applicationContext)
        }
    }

    private fun createNotificationChannels() {
        getKoin().get<NotificationChannelCreator>().createNotificationChannels()
    }
}
