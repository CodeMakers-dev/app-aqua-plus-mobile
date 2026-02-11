package com.codemakers.aquaplus.ui.modules.home.features.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codemakers.aquaplus.R
import com.codemakers.aquaplus.ui.composables.BarcodeScannerScreen
import com.codemakers.aquaplus.ui.composables.ConfirmationDialog
import com.codemakers.aquaplus.ui.composables.DialogType
import com.codemakers.aquaplus.ui.composables.LoadingWidget
import com.codemakers.aquaplus.ui.composables.SnackBarWidget
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme
import com.codemakers.aquaplus.ui.theme.primaryDarkColor
import com.codemakers.aquaplus.ui.theme.secondaryDarkColor
import com.codemakers.aquaplus.ui.theme.tertiaryDarkColor
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReadingFormScreen(
    employeeRouteId: Int,
    onBackAction: () -> Unit,
    onNavigateToInvoice: (employeeRouteId: Int) -> Unit,
) {
    val viewModel = koinViewModel<ReadingFormViewModel>(
        parameters = { parametersOf(employeeRouteId) }
    )
    val state by viewModel.state.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    ReadingFormContent(
        state = state,
        onBackAction = onBackAction,
        onSaveAction = { showConfirmDialog = true },
        onSerialChange = viewModel::onSerialChange,
        onMeterReadingChange = viewModel::onMeterReadingChange,
        onAbnormalConsumptionChange = viewModel::onAbnormalConsumptionChange,
        onObservationsChange = viewModel::onObservationsChange,
    )

    // Show loading only when dialog is active and saving
    if (showConfirmDialog && state.isLoading) {
        LoadingWidget(isLoading = true)
    }

    // Close dialog on error
    LaunchedEffect(state.error) {
        if (state.error != null) {
            showConfirmDialog = false
        }
    }

    if (state.error != null) {
        SnackBarWidget(
            message = state.error ?: stringResource(R.string.copy_generic_error),
            onDismissListener = viewModel::cleanError
        )
    }

    // Navigate only after successful save
    LaunchedEffect(state.isCreatedOrUpdatedSuccess) {
        if (state.isCreatedOrUpdatedSuccess) {
            viewModel.cleanSuccess()
            onNavigateToInvoice(state.employeeRouteId)
        }
    }

    if (showConfirmDialog && !state.isLoading) {
        ConfirmationDialog(
            title = stringResource(R.string.copy_generate_invoice),
            message = stringResource(R.string.copy_generate_invoice_description),
            buttonPrimaryText = stringResource(R.string.copy_continue),
            buttonSecondaryText = stringResource(R.string.copy_logout_cancel),
            dialogType = DialogType.INFO,
            onConfirmLogout = viewModel::onSave,
            onDismissDialog = {
                showConfirmDialog = false
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingFormContent(
    state: ReadingFormUiState,
    onBackAction: () -> Unit = {},
    onSaveAction: () -> Unit = {},
    onSerialChange: (String) -> Unit = {},
    onMeterReadingChange: (String) -> Unit = {},
    onAbnormalConsumptionChange: (Boolean) -> Unit = {},
    onObservationsChange: (String) -> Unit = {},
) {
    val focusRequest = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var showBarcodeScanner by remember { mutableStateOf(false) }
    var showScannerDisabledMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_form_title),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackAction) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = secondaryDarkColor
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(primaryDarkColor)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = secondaryDarkColor,
                    contentColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = state.serial,
                        onValueChange = onSerialChange,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.hasExistingReading,
                        label = {
                            Text(
                                text = stringResource(R.string.meter_reading_hint),
                                color = Color.LightGray
                            )
                        },
                        trailingIcon = {
                            val isScannerEnabled = state.serial.isEmpty()
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isScannerEnabled) tertiaryDarkColor else Color.Gray,
                                    contentColor = if (isScannerEnabled) Color.White else Color.DarkGray
                                ),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        if (isScannerEnabled) {
                                            showBarcodeScanner = true
                                        } else {
                                            showScannerDisabledMessage = true
                                        }
                                    },
                                    enabled = true
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCodeScanner,
                                        contentDescription = "Escanear código de barras",
                                        tint = if (isScannerEnabled) Color.White else Color.DarkGray
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.LightGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedContainerColor = Color(0xFF3A3A4C),
                            unfocusedContainerColor = Color(0xFF3A3A4C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            disabledTextColor = Color.White // Texto en blanco cuando deshabilitado
                        )
                    )

                    OutlinedTextField(
                        value = "${(state.route?.contador?.ultimaLectura ?: 0)} m³",
                        onValueChange = onSerialChange,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        label = {
                            Text(
                                text = stringResource(R.string.copy_reading_last),
                                color = Color.LightGray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.LightGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedContainerColor = Color(0xFF3A3A4C),
                            unfocusedContainerColor = Color(0xFF3A3A4C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            disabledTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = state.meterReading,
                        onValueChange = onMeterReadingChange,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequest),
                        enabled = !state.hasExistingReading,
                        label = {
                            Text(
                                text = stringResource(R.string.reading_input_label),
                                color = Color.LightGray
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.LightGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedContainerColor = Color(0xFF3A3A4C),
                            unfocusedContainerColor = Color(0xFF3A3A4C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            disabledTextColor = Color.White
                        )
                    )

                    AbnormalConsumptionDropdown(
                        abnormalConsumption = state.abnormalConsumption,
                        onAbnormalConsumptionChange = onAbnormalConsumptionChange,
                        enabled = !state.hasExistingReading,
                    )

                    OutlinedTextField(
                        value = state.observations,
                        onValueChange = onObservationsChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        enabled = !state.hasExistingReading,
                        label = {
                            Text(
                                text = stringResource(R.string.observations_optional),
                                color = Color.LightGray
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.DarkGray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.LightGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedContainerColor = Color(0xFF3A3A4C),
                            unfocusedContainerColor = Color(0xFF3A3A4C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            disabledTextColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = state.enableSave,
                onClick = onSaveAction,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = tertiaryDarkColor,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Save,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text(text = stringResource(R.string.copy_save))
            }
        }
    }

    LaunchedEffect(state.route) {
        if (state.route != null) {
            if (state.hasExistingReading) {
                focusManager.clearFocus()
            } else {
                delay(200)
                focusRequest.requestFocus()
            }
        }
    }

    // Cerrar teclado cuando se abre el escáner
    LaunchedEffect(showBarcodeScanner) {
        if (showBarcodeScanner) {
            focusManager.clearFocus()
        }
    }

    // Barcode Scanner Overlay
    if (showBarcodeScanner) {
        BarcodeScannerScreen(
            onBarcodeScanned = { scannedValue ->
                onSerialChange(scannedValue)
            },
            onClose = { showBarcodeScanner = false }
        )
    }

    // Mensaje cuando el escáner está deshabilitado
    if (showScannerDisabledMessage) {
        SnackBarWidget(
            message = "El escáner solo está disponible cuando no hay un serial registrado",
            onDismissListener = { showScannerDisabledMessage = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbnormalConsumptionDropdown(
    abnormalConsumption: Boolean?,
    enabled: Boolean,
    onAbnormalConsumptionChange: (Boolean) -> Unit = {},
) {
    val yesText = stringResource(R.string.abnormal_consumption_high)
    val noText = stringResource(R.string.abnormal_consumption_low)
    val text = when (abnormalConsumption) {
        true -> yesText
        false -> noText
        else -> ""
    }

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            value = text,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = {
                Text(
                    text = stringResource(R.string.abnormal_consumption_optional),
                    color = Color.LightGray
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.DarkGray,
                focusedContainerColor = Color(0xFF3A3A4C),
                unfocusedContainerColor = Color(0xFF3A3A4C),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.White,
                disabledTextColor = Color.White
            )
        )

        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF3A3A4C))
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = yesText,
                        color = Color.White
                    )
                },
                onClick = {
                    expanded = false
                    onAbnormalConsumptionChange(true)
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = noText,
                        color = Color.White
                    )
                },
                onClick = {
                    expanded = false
                    onAbnormalConsumptionChange(false)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReadingFormContentPreview() {
    AquaPlusTheme {
        ReadingFormContent(
            state = ReadingFormUiState(),
        )
    }
}
