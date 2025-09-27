package com.codemakers.aquaplus.ui.modules.signin.features.recovery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
fun RecoveryScreen(
    viewModel: RecoveryViewModel = koinViewModel(),
    onNavigateToBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    RecoveryContent(
        state = state,
        onEmailValueChange = viewModel::onEmailChange,
        onContinueClick = viewModel::onContinueClick
    )

    LoadingWidget(isLoading = state.isLoading)

    if (state.emailSuccess != null) {
        ConfirmationDialog(
            title = state.emailSuccess ?: stringResource(R.string.copy_generic_error),
            buttonPrimaryText = stringResource(R.string.copy_accept),
            dialogType = DialogType.SUCCESS,
            onConfirmLogout = { onNavigateToBack() },
        )
    }
}

@Composable
fun RecoveryContent(
    state: RecoveryUiState, onEmailValueChange: (String) -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryDarkColor) // Dark background from image
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = secondaryDarkColor
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_monochrome),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(120.dp)
                    )

                    Text(
                        text = stringResource(R.string.copy_recover_password),
                        fontSize = 28.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(R.string.copy_recovery_message),
                        fontSize = 16.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )

                    var emailController by remember { mutableStateOf(TextFieldValue(state.email)) }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = emailController,
                        onValueChange = { text ->
                            emailController = text
                            onEmailValueChange(text.text)
                        },
                        label = { Text(stringResource(R.string.copy_email)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon",
                                tint = Color.LightGray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            errorBorderColor = Color.Red,
                            errorCursorColor = Color.Red,
                            errorLabelColor = Color.Red,
                            errorSupportingTextColor = Color.Red,
                            focusedLabelColor = Color.LightGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedContainerColor = Color(0xFF3A3A4C),
                            unfocusedContainerColor = Color(0xFF3A3A4C),
                        ),
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(8.dp),
                        isError = state.isEmailInvalid || state.emailRequired
                    )

                    if (state.emailError != null) {
                        Text(
                            text = state.emailError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    if (state.isEmailInvalid) {
                        Text(
                            text = stringResource(R.string.copy_email_invalid),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    if (state.emailRequired) {
                        Text(
                            text = stringResource(R.string.copy_email_required),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    Button(
                        onClick = onContinueClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF42A5F5), // Blue button color from image
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            stringResource(R.string.copy_recover),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecoveryContentPreview() {
    AquaPlusTheme {
        Surface(color = primaryDarkColor) { // Simulates the Box background for preview
            RecoveryContent(
                state = RecoveryUiState(email = "test@example.com"),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecoveryContentErrorPreview() {
    AquaPlusTheme {
        Surface(color = primaryDarkColor) {
            RecoveryContent(
                state = RecoveryUiState(email = "test", isEmailInvalid = true),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecoveryContentEmailRequiredPreview() {
    AquaPlusTheme {
        Surface(color = primaryDarkColor) {
            RecoveryContent(
                state = RecoveryUiState(email = "", emailRequired = true),
            )
        }
    }
}
