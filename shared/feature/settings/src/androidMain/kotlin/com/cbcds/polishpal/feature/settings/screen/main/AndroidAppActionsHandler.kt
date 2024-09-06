package com.cbcds.polishpal.feature.settings.screen.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.cbcds.polishpal.feature.settings.R
import com.cbcds.polishpal.feature.settings.model.AppInfo

internal class AndroidAppActionsHandler(
    private val context: Context,
    private val appInfo: AppInfo,
) : AppActionsHandler {

    override fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${appInfo.contactEmail}")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(appInfo.contactEmail))
        }

        intent.launchOrShowError(R.string.no_email_app_found)
    }

    override fun shareApp() {
        val message = """
            ${context.getString(R.string.share_app_message)}
            
            ${getAppLinkInGooglePlay()}
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }.let {
            Intent.createChooser(it, null)
        }

        intent.launchOrShowError(R.string.no_messaging_app_found)
    }

    override fun rateApp() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(getAppLinkInGooglePlay())
            setPackage("com.android.vending")
        }

        context.startActivity(intent)
    }

    private fun Intent.launchOrShowError(@StringRes errorMessageResId: Int) {
        if (resolveActivity(context.packageManager) != null) {
            context.startActivity(this)
        } else {
            Toast.makeText(context, errorMessageResId, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAppLinkInGooglePlay(): String {
        return "https://play.google.com/store/apps/details?id=${appInfo.applicationId}"
    }
}

@Composable
internal actual fun rememberAppActionsHandler(appInfo: AppInfo): AppActionsHandler {
    val context = LocalContext.current
    return remember { AndroidAppActionsHandler(context, appInfo) }
}
