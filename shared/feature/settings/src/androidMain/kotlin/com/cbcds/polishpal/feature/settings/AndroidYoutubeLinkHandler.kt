package com.cbcds.polishpal.feature.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private class AndroidYoutubeLinkHandler(private val context: Context) : YoutubeLinkHandler {

    override fun handle(id: String) {
        val appIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("vnd.youtube:$id"),
        )
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        if (appIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(appIntent)
        } else {
            context.startActivity(webIntent)
        }
    }
}

@Composable
internal actual fun rememberYoutubeLinkHandler(): YoutubeLinkHandler {
    val context = LocalContext.current
    return remember { AndroidYoutubeLinkHandler(context) }
}
