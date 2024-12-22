package com.sawrose.toa.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class AndroidPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : Preferences {
    /**
     * If a caller passes a null [value], then we will just remove this key from our preferences.
     */
    override suspend fun storeInt(
        key: String,
        value: Int?,
    ) {
        if (value != null) {
            sharedPreferences.edit {
                putInt(key, value)
            }
        } else {
            sharedPreferences.edit {
                remove(key)
            }
        }
    }

    override suspend fun getInt(
        key: String,
        defaultValue: Int?,
    ): Int? {
        return if (sharedPreferences.contains(key)) {
            sharedPreferences.getInt(key, 0)
        } else {
            defaultValue
        }
    }

    override suspend fun storeBoolean(
        key: String,
        value: Boolean,
    ) {
        sharedPreferences
            .edit {
                putBoolean(key, value)
            }
    }

    override suspend fun getBoolean(
        key: String,
        defaultValue: Boolean,
    ): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}
