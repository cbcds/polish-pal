package com.cbcds.polishpal.feature.settings.notifications

import android.app.NotificationManager
import androidx.annotation.StringRes
import com.cbcds.polishpal.feature.settings.R

internal enum class NotificationChannel(
    val id: String,
    @StringRes val nameResId: Int,
    val importance: Int,
) {

    LEARNING(
        id = "learning",
        nameResId = R.string.notification_learning_channel_name,
        importance = NotificationManager.IMPORTANCE_HIGH,
    )
}
