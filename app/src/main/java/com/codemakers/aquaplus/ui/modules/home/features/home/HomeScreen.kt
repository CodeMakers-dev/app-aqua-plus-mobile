package com.codemakers.aquaplus.ui.modules.home.features.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codemakers.aquaplus.BuildConfig
import com.codemakers.aquaplus.R
import com.codemakers.aquaplus.ui.modules.home.features.profile.ProfileScreen
import com.codemakers.aquaplus.ui.modules.home.features.route.RouteScreen
import com.codemakers.aquaplus.ui.theme.primaryDarkColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToForm: (employeeRouteId: Int) -> Unit = {},
    onNavigateToInvoice: (employeeRouteId: Int) -> Unit,
) {
    val internalNavController = rememberNavController()
    val navBackStackEntry by internalNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryDarkColor,
                    titleContentColor = Color.White,
                ),
                title = {
                    val appName = stringResource(R.string.app_name)
                    Text(
                        text = buildAnnotatedString {
                            append("$appName ")
                            withStyle(SpanStyle(fontSize = 10.sp)) {
                                append("v${BuildConfig.VERSION_NAME}")
                            }
                        },
                        color = Color.White,
                    )
                },
                actions = {
                    if (currentRoute != "profile") {
                        IconButton(onClick = { internalNavController.navigate("profile") }) {
                            Icon(
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = "Perfil",
                                tint = Color.White,
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = internalNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            popEnterTransition = { fadeIn(animationSpec = tween(100)) },
            popExitTransition = { fadeOut(animationSpec = tween(100)) },
        ) {
            composable("home") {
                RouteScreen(
                    onNavigateToForm = onNavigateToForm,
                    onNavigateToInvoice = onNavigateToInvoice,
                )
            }
            composable("profile") {
                ProfileScreen(
                    onNavigateToLogin = onNavigateToLogin,
                )
            }
        }
    }
}