package com.cbcds.polishpal.feature.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

internal class AndroidAppActionsHandler(private val context: Context) : AppActionsHandler {

    override fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$EMAIL_FOR_FEEDBACK")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(EMAIL_FOR_FEEDBACK))
        }

        intent.launchOrShowError(R.string.no_email_app_found)
    }

    override fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_message))
        }.let {
            Intent.createChooser(it, null)
        }

        intent.launchOrShowError(R.string.no_messaging_app_found)
    }

    override fun rateApp() {
        // TODO
    }

    private fun Intent.launchOrShowError(@StringRes errorMessageResId: Int) {
        if (resolveActivity(context.packageManager) != null) {
            context.startActivity(this)
        } else {
            Toast.makeText(context, errorMessageResId, Toast.LENGTH_SHORT).show()
        }
    }

    private companion object {

        const val EMAIL_FOR_FEEDBACK = "stub"
    }
}

@Composable
internal actual fun rememberAppActionsHandler(): AppActionsHandler {
    val context = LocalContext.current
    return remember { AndroidAppActionsHandler(context) }
}
