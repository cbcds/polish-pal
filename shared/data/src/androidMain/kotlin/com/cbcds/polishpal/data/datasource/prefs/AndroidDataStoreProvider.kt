package com.cbcds.polishpal.data.datasource.prefs

import android.content.Context

internal class AndroidDataStoreProvider(context: Context) : DataStoreProvider(
    getFilePath = { filename ->
        context.filesDir.resolve(filename).absolutePath
    }
)
