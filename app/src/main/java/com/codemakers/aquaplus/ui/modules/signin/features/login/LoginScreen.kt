package com.codemakers.aquaplus.ui.modules.signin.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codemakers.aquaplus.R
import com.codemakers.aquaplus.ui.composables.LoadingWidget
import com.codemakers.aquaplus.ui.composables.SnackBarWidget
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme
import com.codemakers.aquaplus.ui.theme.primaryDarkColor
import com.codemakers.aquaplus.ui.theme.secondaryDarkColor
import com.codemakers.aquaplus.ui.theme.tertiaryDarkColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToRecovery: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    LoginContent(
        state = state,
        onLogin = viewModel::onLogin,
        onRecovery = onNavigateToRecovery,
        onUserValueChange = viewModel::onUserValueChange,
        onPassValueChange = viewModel::onPassValueChange,
        onChangePassVisibility = viewModel::onChangePassVisibility,
    )

    LoadingWidget(isLoading = state.isLoading)

    if (state.errorLogin != null) {
        SnackBarWidget(
            message = state.errorLogin ?: stringResource(R.string.copy_generic_error),
            onDismissListener = viewModel::cleanLoginError
        )
    }

    LaunchedEffect(state.navigateToHome) {
        if (state.navigateToHome) {
            onNavigateToHome()
        }
    }
}

@Composable
fun LoginContent(
    state: LoginUiState,
    onLogin: () -> Unit = {},
    onRecovery: () -> Unit = {},
    onUserValueChange: (String) -> Unit = {},
    onPassValueChange: (String) -> Unit = {},
    onChangePassVisibility: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryDarkColor) // Dark background from image
                .padding(innerPadding),
            contentAlignment = Alignment.Center // Center the card
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 500.dp) // Max width for the card, good for various screen sizes
                    .fillMaxWidth(0.9f) // Use 90% of screen width on smaller screens
                    .padding(vertical = 20.dp), // Some vertical padding if screen is too short
                shape = RoundedCornerShape(16.dp),
                color = secondaryDarkColor, // Darker card background from image
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_monochrome), // Placeholder, assuming this is the closest logo
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(120.dp)
                    )

                    Text(
                        text = "Bienvenido usuario!",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "¡Vamos a iniciar sesión!",
                        fontSize = 18.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(32.dp))

                    var userController by remember { mutableStateOf(TextFieldValue(text = state.username)) }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = userController,
                        onValueChange = { text ->
                            userController = text
                            onUserValueChange(text.text)
                        },
                        label = { Text(stringResource(R.string.copy_username)) },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon",
                                tint = Color.LightGray
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        isError = state.usernameRequired,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedContainerColor = Color(0xFF3A3A4C), // Darker input field background
                            unfocusedContainerColor = Color(0xFF3A3A4C), // Darker input field background
                        )
                    )

                    if (state.usernameRequired) {
                        Text(
                            text = stringResource(R.string.copy_username_required),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    var passController by remember { mutableStateOf(TextFieldValue(text = state.password)) }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = passController,
                        onValueChange = { text ->
                            passController = text
                            onPassValueChange(text.text)
                        },
                        label = { Text(stringResource(R.string.copy_password)) },
                        textStyle = TextStyle(color = Color.White),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        visualTransformation = if (state.showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password Icon",
                                tint = Color.LightGray
                            )
                        },
                        trailingIcon = {
                            val imageVector =
                                if (state.showPass) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            val description =
                                if (state.showPass) stringResource(R.string.copy_hidde) else stringResource(
                                    R.string.copy_show
                                )
                            Icon(
                                imageVector = imageVector,
                                contentDescription = description,
                                tint = Color.LightGray,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable { onChangePassVisibility() }
                            )
                        },
                        isError = state.passwordRequired,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedContainerColor = Color(0xFF3A3A4C),
                            unfocusedContainerColor = Color(0xFF3A3A4C),
                        )
                    )

                    if (state.passwordRequired) {
                        Text(
                            text = stringResource(R.string.copy_password_required),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = onLogin,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D5D5D), // Grey button color from image
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp) // Standard button height
                    ) {
                        Text(
                            stringResource(R.string.copy_sign_in),
                            fontSize = 18.sp, // Adjusted button text size
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(Modifier.height(16.dp)) // Added spacer after button

                    Text(
                        stringResource(R.string.copy_forgot_your_password),
                        color = tertiaryDarkColor, // Blue color from image
                        modifier = Modifier.clickable { onRecovery() },
                        fontSize = 14.sp // Slightly smaller
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginContentPreview() {
    AquaPlusTheme {
        LoginContent(
            state = LoginUiState(username = "test", password = "test"),
        )
    }
}

@Preview
@Composable
fun LoginContentErrorPreview() {
    AquaPlusTheme {
        LoginContent(
            state = LoginUiState(
                username = "test",
                usernameRequired = true,
                passwordRequired = true,
            ),
        )
    }
}
