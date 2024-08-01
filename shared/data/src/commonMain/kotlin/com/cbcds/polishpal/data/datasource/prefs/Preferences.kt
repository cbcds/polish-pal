package com.cbcds.polishpal.data.datasource.prefs

import kotlinx.coroutines.flow.Flow

internal interface Preferences {

    fun readBoolean(key: String): Flow<Boolean?>

    fun readInt(key: String): Flow<Int?>

    fun readString(key: String): Flow<String?>

    fun writeBoolean(key: String, value: Boolean)

    fun writeInt(key: String, value: Int)

    fun writeString(key: String, value: String)
}
