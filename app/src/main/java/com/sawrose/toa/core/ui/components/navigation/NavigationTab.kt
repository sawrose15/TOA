package com.sawrose.toa.core.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.sawrose.toa.R
import com.sawrose.toa.destinations.TaskListScreenDestination

sealed class NavigationTab(
    val labelTextRes: Int,
    val icon: ImageVector,
    val screenRoute: String,
) {

    object Home: NavigationTab(
        labelTextRes = R.string.home,
        icon = Icons.Default.Home,
        screenRoute = TaskListScreenDestination.route
    )

    object Settings: NavigationTab(
        labelTextRes = R.string.settings,
        icon = Icons.Default.Settings,
        screenRoute = TaskListScreenDestination.route
    )
}