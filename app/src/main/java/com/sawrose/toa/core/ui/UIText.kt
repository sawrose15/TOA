package com.sawrose.toa.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * This is a sealed class that contains all the different way text can be presented to the UI.
 */
sealed class UIText {
    data class StringText(val value: String) : UIText()

    data class ResourceText(@StringRes val value: Int) : UIText()
}

/**
 * Evaluates the value of this [UIText] based on its type
 *
 * @param[context] if necessary, use this to evaluate the string resource.
 */
fun UIText.getString(context: Context): String {
    return when (this) {
        is UIText.StringText -> this.value
        is UIText.ResourceText -> context.getString(this.value)
    }
}

/**
 * A helper function that allows to get strings from a [Composable] context.
 */
@Composable
fun UIText.getString(): String {
    return this.getString(LocalContext.current)
}
