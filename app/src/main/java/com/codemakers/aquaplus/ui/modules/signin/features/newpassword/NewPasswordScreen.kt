package com.codemakers.aquaplus.ui.modules.signin.features.newpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codemakers.aquaplus.R
import com.codemakers.aquaplus.ui.composables.ConfirmationDialog
import com.codemakers.aquaplus.ui.composables.DialogType
import com.codemakers.aquaplus.ui.composables.LoadingWidget
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme
import com.codemakers.aquaplus.ui.theme.primaryDarkColor
import com.codemakers.aquaplus.ui.theme.secondaryDarkColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewPasswordScreen(
    viewModel: NewPasswordViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    NewPasswordContent(
        state = state,
        onNewPassword1Change = viewModel::onNewPassword1Change,
        onNewPassword2Change = viewModel::onNewPassword2Change,
        onChangePasswordClick = viewModel::onChangePasswordClick
    )

    LoadingWidget(isLoading = state.isLoading)

    if (state.navigateToLogin) {
        ConfirmationDialog(
            title = stringResource(R.string.copy_new_password_update),
            buttonPrimaryText = stringResource(R.string.copy_accept),
            dialogType = DialogType.SUCCESS,
            onConfirmLogout = {
                onNavigateToLogin()
                viewModel.cleanNavigateToLogin()
            },
        )
    }
}

@Composable
fun NewPasswordContent(
    state: NewPasswordUiState,
    onNewPassword1Change: (String) -> Unit,
    onNewPassword2Change: (String) -> Unit,
    onChangePasswordClick: () -> Unit
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryDarkColor)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 20.dp),
                shape = RoundedCornerShape(16.dp),
                color = secondaryDarkColor,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_monochrome),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(96.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.copy_new_password_create),
                        fontSize = 28.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(32.dp))

                    var newPassword1Controller by remember { mutableStateOf(TextFieldValue(state.newPassword1)) }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = newPassword1Controller,
                        onValueChange = { text ->
                            newPassword1Controller = text
                            onNewPassword1Change(text.text)
                        },
                        label = { Text(stringResource(R.string.copy_new_password)) },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password Icon",
                                tint = Color.LightGray
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        isError = state.passwordError != null || state.passwordRequired,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.LightGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedContainerColor = Color(0xFF3A3A4C),
                            unfocusedContainerColor = Color(0xFF3A3A4C),
                        )
                    )

                    if (state.passwordRequired) {
                        Text(
                            text = stringResource(R.string.copy_new_password_required),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    var newPassword2Controller by remember { mutableStateOf(TextFieldValue(state.newPassword2)) }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = newPassword2Controller,
                        onValueChange = { text ->
                            newPassword2Controller = text
                            onNewPassword2Change(text.text)
                        },
                        label = { Text(stringResource(R.string.copy_new_password_confirm)) },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password Icon",
                                tint = Color.LightGray
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        isError = state.passwordError != null || state.passwordNotMatch,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.LightGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedContainerColor = Color(0xFF3A3A4C),
                            unfocusedContainerColor = Color(0xFF3A3A4C),
                        )
                    )

                    if (state.passwordError != null) {
                        Text(
                            text = state.passwordError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    if (state.passwordNotMatch) {
                        Text(
                            text = stringResource(R.string.copy_new_password_not_match),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = onChangePasswordClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF42A5F5), // Blue button color from image
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text(
                                stringResource(R.string.copy_change_password),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewPasswordContentPreview() {
    AquaPlusTheme {
        Surface(color = primaryDarkColor) { // Simulates the Box background for preview
            NewPasswordContent(
                state = NewPasswordUiState(),
                onNewPassword1Change = { },
                onNewPassword2Change = { },
                onChangePasswordClick = { }
            )
        }
    }
}

@Preview
@Composable
fun NewPasswordContentErrorPreview() {
    AquaPlusTheme {
        Surface(color = primaryDarkColor) {
            NewPasswordContent(
                state = NewPasswordUiState(passwordError = "Las contraseñas no coinciden."),
                onNewPassword1Change = { },
                onNewPassword2Change = { },
                onChangePasswordClick = { }
            )
        }
    }
}
