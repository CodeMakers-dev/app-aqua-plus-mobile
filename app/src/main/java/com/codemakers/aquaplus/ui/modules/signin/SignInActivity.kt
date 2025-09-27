package com.codemakers.aquaplus.ui.modules.signin

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.codemakers.aquaplus.BuildConfig
import com.codemakers.aquaplus.ui.modules.home.HomeActivity
import com.codemakers.aquaplus.ui.modules.signin.features.login.LoginScreen
import com.codemakers.aquaplus.ui.modules.signin.features.newpassword.NewPasswordScreen
import com.codemakers.aquaplus.ui.modules.signin.features.recovery.RecoveryScreen
import com.codemakers.aquaplus.ui.modules.signin.features.splash.SplashScreen
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme
import kotlinx.serialization.Serializable

class SignInActivity : ComponentActivity() {

    @Serializable
    sealed interface SignInRoutes {
        @Serializable
        object SplashRoute : SignInRoutes

        @Serializable
        object LoginRoute : SignInRoutes

        @Serializable
        object RecoveryRoute : SignInRoutes

        @Serializable
        data class NewPasswordRoute(val token: String? = null) : SignInRoutes
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignInContent()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    @Composable
    fun SignInContent() {
        AquaPlusTheme {
            val navController = rememberNavController()
            val currentIntentDataString = this.intent?.dataString

            val startDestinationRoute = remember(currentIntentDataString) {
                navigationDeeplink()
            }

            val navGraph =
                remember(navController, startDestinationRoute) {
                    navController.createGraph(startDestination = startDestinationRoute) {
                        composable<SignInRoutes.SplashRoute> {
                            SplashScreen(
                                onNavigateToHome = ::startHomeActivity,
                                onNavigateToLogin = {
                                    navController.navigate(route = SignInRoutes.LoginRoute) {
                                        popUpTo<SignInRoutes.SplashRoute> { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                            )
                        }
                        composable<SignInRoutes.LoginRoute> {
                            LoginScreen(
                                onNavigateToHome = ::startHomeActivity,
                                onNavigateToRecovery = { navController.navigate(route = SignInRoutes.RecoveryRoute) }
                            )
                        }
                        composable<SignInRoutes.RecoveryRoute> {
                            RecoveryScreen(
                                onNavigateToBack = {
                                    navController.navigate(route = SignInRoutes.LoginRoute) {
                                        popUpTo<SignInRoutes.RecoveryRoute> { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        composable<SignInRoutes.NewPasswordRoute> {
                            NewPasswordScreen(
                                onNavigateToLogin = {
                                    navController.navigate(route = SignInRoutes.LoginRoute) {
                                        popUpTo<SignInRoutes.NewPasswordRoute> { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            NavHost(
                navController = navController,
                graph = navGraph,
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
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        finish()
    }

    private fun navigationDeeplink(): SignInRoutes {
        return this.intent?.data?.let { uri ->
            val uriString = uri.toString()
            if (uriString.startsWith(HTTPS_NEW_PASSWORD)) {
                val token = uri.getQueryParameter(TOKEN_KEY)
                SignInRoutes.NewPasswordRoute(token = token)
            } else {
                SignInRoutes.SplashRoute
            }
        } ?: SignInRoutes.SplashRoute
    }

    companion object {
        const val TOKEN_KEY = "token"
        const val HTTPS_NEW_PASSWORD = "${BuildConfig.BASE_URL}/auth/recover-password"
    }
}
