package com.codemakers.aquaplus.ui.modules.home

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.codemakers.aquaplus.ui.modules.home.features.form.ReadingFormScreen
import com.codemakers.aquaplus.ui.modules.home.features.home.HomeScreen
import com.codemakers.aquaplus.ui.modules.home.features.invoice.InvoiceScreen
import com.codemakers.aquaplus.ui.modules.signin.SignInActivity
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme
import com.codemakers.aquaplus.ui.theme.primaryDarkColor
import kotlinx.serialization.Serializable

class HomeActivity : ComponentActivity() {

    @Serializable
    sealed interface HomeRoutes {
        @Serializable
        object HomeRoute : HomeRoutes

        @Serializable
        data class FormRoute(val employeeRouteId: Int) : HomeRoutes

        @Serializable
        data class InvoiceRoute(val employeeRouteId: Int) : HomeRoutes
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeContent()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeContent() {
        AquaPlusTheme {
            val navController = rememberNavController()

            Scaffold(
                modifier = Modifier.background(primaryDarkColor),
                content = { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = HomeRoutes.HomeRoute,
                        modifier = Modifier.padding(paddingValues),
                        enterTransition = {
                            fadeIn(animationSpec = tween(150)) +
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                        animationSpec = tween(200)
                                    )
                        },
                        exitTransition = {
                            fadeOut(animationSpec = tween(150)) +
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                        animationSpec = tween(200)
                                    )
                        },
                        popEnterTransition = {
                            fadeIn(animationSpec = tween(150)) +
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                                        animationSpec = tween(200)
                                    )
                        },
                        popExitTransition = {
                            fadeOut(animationSpec = tween(150)) +
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                                        animationSpec = tween(200)
                                    )
                        }
                    ) {
                        composable<HomeRoutes.HomeRoute> {
                            HomeScreen(
                                onNavigateToLogin = ::startSignInActivity,
                                onNavigateToForm = { employeeRouteId ->
                                    navController.navigate(
                                        HomeRoutes.FormRoute(employeeRouteId = employeeRouteId)
                                    ) {
                                        launchSingleTop = true
                                    }
                                },
                                onNavigateToInvoice = { employeeRouteId ->
                                    navController.navigate(
                                        route = HomeRoutes.InvoiceRoute(employeeRouteId = employeeRouteId)
                                    ) {
                                        launchSingleTop = true
                                        popUpTo<HomeRoutes.FormRoute> { inclusive = true }
                                    }
                                },
                            )
                        }
                        composable<HomeRoutes.FormRoute> { navBackStackEntry ->
                            val arguments =
                                navBackStackEntry.toRoute<HomeRoutes.FormRoute>()
                            ReadingFormScreen(
                                employeeRouteId = arguments.employeeRouteId,
                                onBackAction = navController::popBackStack,
                                onNavigateToInvoice = { employeeRouteId ->
                                    navController.navigate(
                                        route = HomeRoutes.InvoiceRoute(employeeRouteId = employeeRouteId)
                                    ) {
                                        launchSingleTop = true
                                        popUpTo<HomeRoutes.FormRoute> { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable<HomeRoutes.InvoiceRoute> { navBackStackEntry ->
                            val arguments =
                                navBackStackEntry.toRoute<HomeRoutes.InvoiceRoute>()
                            InvoiceScreen(
                                employeeRouteId = arguments.employeeRouteId
                            )
                        }
                    }
                }
            )
        }
    }

    private fun startSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        finish()
    }
}