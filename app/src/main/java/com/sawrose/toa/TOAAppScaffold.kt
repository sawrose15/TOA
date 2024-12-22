package com.sawrose.toa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sawrose.toa.core.ui.components.navigation.NavigationType

@Composable
fun TOAAppScaffold(
    navigationType: NavigationType,
    navigationContent: @Composable () -> Unit,
    appContent: @Composable () -> Unit,
) {
    when (navigationType) {
        NavigationType.BOTTOM_NAVIGATION -> {
            VerticalAppScaffold(
                navigationContent = navigationContent,
                appContent = appContent,
            )
        }

        NavigationType.NAVIGATION_RAIL -> {
            HorizontalAppScaffold(
                navigationContent = navigationContent,
                appContent = appContent,
            )
        }

        NavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            PermanentNavigationScaffold(
                navigationContent = navigationContent,
                appContent = appContent,
            )
        }
    }
}

@Composable
fun HorizontalAppScaffold(
    navigationContent: @Composable () -> Unit,
    appContent: @Composable () -> Unit,
) {
    Row {
        navigationContent()

        Box(
            modifier = Modifier
                .weight(1f),
        ) {
            appContent()
        }
    }
}

@Composable
fun VerticalAppScaffold(
    navigationContent: @Composable () -> Unit,
    appContent: @Composable () -> Unit,
) {
    Column {
        Box(
            modifier = Modifier
                .weight(1f),
        ) {
            appContent()
        }

        navigationContent()
    }
}

@Composable
fun PermanentNavigationScaffold(
    navigationContent: @Composable () -> Unit,
    appContent: @Composable () -> Unit,
) {
    PermanentNavigationDrawer(
        drawerContent = {
            navigationContent()
        },
        content = {
            appContent()
        },
    )
}
