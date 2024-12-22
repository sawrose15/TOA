package com.sawrose.toa.core.ui.components.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TOANavigationDrawerContent(
    navigationConfig: TOANavigationConfig,
    modifier: Modifier = Modifier,
) {
    PermanentDrawerSheet(
        modifier = modifier.sizeIn(
            minWidth = 200.dp,
            maxWidth = 200.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
        ) {
            navigationConfig.tabs.forEach { tab ->
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(id = tab.labelTextRes),
                        )
                    },
                    selected = (tab == navigationConfig.selectedTab),
                    onClick = {
                        navigationConfig.onTabClicked.invoke(tab)
                    },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = stringResource(id = tab.labelTextRes),
                        )
                    },
                )
            }
        }
    }
}
