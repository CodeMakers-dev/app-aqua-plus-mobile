package com.codemakers.aquaplus.ui.modules.home

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.codemakers.aquaplus.ui.models.Invoice
import com.codemakers.aquaplus.ui.modules.home.features.form.ReadingFormScreen
import com.codemakers.aquaplus.ui.modules.home.features.home.HomeScreen
import com.codemakers.aquaplus.ui.modules.home.features.invoice.InvoiceScreen
import com.codemakers.aquaplus.ui.modules.signin.SignInActivity
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme
import com.codemakers.aquaplus.ui.theme.primaryDarkColor
import com.google.gson.Gson
import kotlinx.serialization.Serializable

class HomeActivity : ComponentActivity() {

    @Serializable
    sealed interface HomeRoutes {
        @Serializable
        object HomeRoute : HomeRoutes

        @Serializable
        data class FormRoute(val employeeRouteId: Int) : HomeRoutes

        @Serializable
        data class InvoiceRoute private constructor(
            private val data: String,
        ) : HomeRoutes {

            val invoice: Invoice
                get() = Gson().fromJson(data, Invoice::class.java)

            constructor(
                invoice: Invoice,
            ) : this(
                data = Gson().toJson(invoice),
            )
        }
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
                modifier = Modifier.background(primaryDarkColor), // Asegura fondo oscuro para Scaffold
                content = { paddingValues ->
                    val navGraph = remember(navController) {
                        navController.createGraph(startDestination = HomeRoutes.HomeRoute) {
                            composable<HomeRoutes.HomeRoute> {
                                HomeScreen(
                                    onNavigateToLogin = ::startSignInActivity,
                                    onNavigateToForm = { employeeRouteId ->
                                        navController.navigate(
                                            HomeRoutes.FormRoute(employeeRouteId = employeeRouteId)
                                        )
                                    },
                                    onNavigateToInvoice = { route, config, data ->
                                        val invoice = Invoice(
                                            route = route,
                                            config = config,
                                            data = data,
                                        )
                                        navController.navigate(
                                            route = HomeRoutes.InvoiceRoute(invoice = invoice)
                                        ) {
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
                                    onNavigateToInvoice = { route, config, data ->
                                        val invoice = Invoice(
                                            route = route,
                                            config = config,
                                            data = data,
                                        )
                                        navController.navigate(
                                            route = HomeRoutes.InvoiceRoute(invoice = invoice)
                                        ) {
                                            popUpTo<HomeRoutes.FormRoute> { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable<HomeRoutes.InvoiceRoute> { navBackStackEntry ->
                                val arguments =
                                    navBackStackEntry.toRoute<HomeRoutes.InvoiceRoute>()
                                InvoiceScreen(
                                    invoice = arguments.invoice,
                                )
                            }
                        }
                    }
                    NavHost(
                        navController = navController,
                        graph = navGraph,
                        modifier = Modifier.padding(paddingValues), // Aplicar paddingValues aquí
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(300))
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    )
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