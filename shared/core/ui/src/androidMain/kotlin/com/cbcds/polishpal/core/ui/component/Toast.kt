package com.cbcds.polishpal.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast as AndroidToast

@Composable
fun Toast(text: String) {
    val context = LocalContext.current
    LaunchedEffect(text) {
        AndroidToast.makeText(context, text, android.widget.Toast.LENGTH_SHORT).show()
    }
}
