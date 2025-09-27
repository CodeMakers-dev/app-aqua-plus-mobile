package com.codemakers.aquaplus.ui.modules.signin.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.codemakers.aquaplus.R
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme
import com.codemakers.aquaplus.ui.theme.primaryDarkColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    SplashContent(state)

    LaunchedEffect(state.navigateToLogin) {
        if (state.navigateToLogin) {
            viewModel.cleanNavigateToLogin()
            onNavigateToLogin()
        }
    }

    LaunchedEffect(state.navigateToHome) {
        if (state.navigateToHome) {
            viewModel.cleanNavigateToHome()
            onNavigateToHome()
        }
    }
}

@Composable
fun SplashContent(state: SplashUiState) {
    val lightPrimaryColor =
        primaryDarkColor.copy(alpha = 0.6f)

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(lightPrimaryColor, primaryDarkColor)
                    )
                )
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun SplashContentPreview() {
    AquaPlusTheme {
        SplashContent(
            state = SplashUiState()
        )
    }
}
