package com.cbcds.polishpal

import android.app.Application
import android.os.Build
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

        initKoin(applicationScope, androidAppModule) {
            androidContext(applicationContext)
        }

        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getKoin().get<NotificationChannelCreator>().createNotificationChannels()
        }
    }
}
