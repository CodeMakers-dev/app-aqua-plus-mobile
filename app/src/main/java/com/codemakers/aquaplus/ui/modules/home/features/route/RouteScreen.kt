package com.codemakers.aquaplus.ui.modules.home.features.route

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Cookie
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codemakers.aquaplus.R
import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.ui.composables.ClickableTabs
import com.codemakers.aquaplus.ui.composables.ConfirmationDialog
import com.codemakers.aquaplus.ui.composables.DialogType
import com.codemakers.aquaplus.ui.composables.LoadingWidget
import com.codemakers.aquaplus.ui.composables.SnackBarWidget
import com.codemakers.aquaplus.ui.extensions.toCapitalCase
import com.codemakers.aquaplus.ui.theme.primaryDarkColor
import com.codemakers.aquaplus.ui.theme.secondaryDarkColor
import com.codemakers.aquaplus.ui.theme.tertiaryDarkColor
import org.koin.androidx.compose.koinViewModel

private val routeTabs = listOf("Pendientes", "Completadas")

@Composable
fun RouteScreen(
    viewModel: RouteViewModel = koinViewModel(),
    onNavigateToForm: (employeeRouteId: Int) -> Unit,
    onNavigateToInvoice: (employeeRouteId: Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    RouteContent(
        state = state,
        onSearchChange = viewModel::onSearchChange,
        loadRoutes = { showLogoutDialog = true },
        onNavigateToForm = onNavigateToForm,
        onNavigateToInvoice = onNavigateToInvoice,
    )

    LoadingWidget(isLoading = state.isLoading)

    if (state.error != null) {
        SnackBarWidget(
            message = state.error ?: stringResource(R.string.copy_generic_error),
            onDismissListener = viewModel::cleanError
        )
    }

    if (showLogoutDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.copy_download_route_title),
            message = stringResource(R.string.copy_download_route_message),
            buttonPrimaryText = stringResource(R.string.copy_sync),
            buttonSecondaryText = stringResource(R.string.copy_logout_cancel),
            dialogType = DialogType.WARNING,
            onConfirmLogout = {
                showLogoutDialog = false
                viewModel.loadAllRoutes()
            },
            onDismissDialog = {
                showLogoutDialog = false
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteContent(
    state: RouteUiState,
    onSearchChange: (String) -> Unit = {},
    loadRoutes: () -> Unit = {},
    onNavigateToForm: (employeeRouteId: Int) -> Unit = {},
    onNavigateToInvoice: (employeeRouteId: Int) -> Unit = {},
) {
    Scaffold { paddingValues ->
        if (state.allRoutes == null) return@Scaffold

        if (state.allRoutes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(primaryDarkColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Cookie,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
                Text(
                    text = stringResource(R.string.copy_no_routes_found),
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = loadRoutes,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tertiaryDarkColor,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(50.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.copy_download_route),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        } else {
            val pagerState = rememberPagerState(pageCount = { routeTabs.size })
            val coroutineScope = rememberCoroutineScope()
            Column(
                modifier = Modifier
                    .background(primaryDarkColor)
                    .padding(paddingValues) // Mantener paddingValues aquí
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    OutlinedTextField(
                        value = state.search,
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.copy_input_search),
                                color = Color.LightGray
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = Color(0xFF3A3A4C),
                            focusedContainerColor = Color(0xFF3A3A4C),
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White
                        ),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = Color.LightGray
                            )
                        },
                        trailingIcon = {
                            if (state.search.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = null,
                                        tint = Color.LightGray
                                    )
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el TextField y el botón

                    IconButton(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF3A3A4C),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .size(56.dp),
                        onClick = loadRoutes
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                }
                ClickableTabs(
                    selectedItem = pagerState.currentPage,
                    tabsList = routeTabs,
                    onClick = { index ->
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    }
                )
                val stableIsInvoiceAvailable = remember(state.allData) { state::isInvoiceAvailable }
                val stableIsSynced = remember(state.allData) { state::isSynced }
                val stableGetContadorSerial = remember(state.allData) { state::getContadorSerial }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    RouteListContent(
                        routes = if (page == 0) state.pendingRoutes else state.completedRoutes,
                        isInvoiceAvailable = stableIsInvoiceAvailable,
                        isSynced = stableIsSynced,
                        getContadorSerial = stableGetContadorSerial,
                        showSyncStatus = page == 1, // Show sync status only in completed routes
                        onNavigateToForm = onNavigateToForm,
                        onNavigateToInvoice = onNavigateToInvoice
                    )
                }
            }
        }
    }
}

@Composable
fun RouteListContent(
    routes: List<EmployeeRoute>,
    isInvoiceAvailable: (employeeRouteId: Int) -> Boolean,
    isSynced: (employeeRouteId: Int) -> Boolean = { false },
    getContadorSerial: (employeeRouteId: Int) -> String? = { null },
    showSyncStatus: Boolean = false,
    onNavigateToForm: (employeeRouteId: Int) -> Unit,
    onNavigateToInvoice: (employeeRouteId: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = routes.size,
            key = { index -> routes[index].id },
            contentType = { "RouteCard" }
        ) { index ->
            val item = routes[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = secondaryDarkColor
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                onClick = { onNavigateToForm(item.id) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    // Sync status indicator (only for completed routes)
                    if (showSyncStatus) {
                        val synced = isSynced(item.id)
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (synced) Color(0xFF4CAF50) else Color(0xFFFF9800),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (synced) Icons.Outlined.CheckCircle else Icons.Outlined.Schedule,
                                contentDescription = if (synced) "Sincronizado" else "Pendiente de sincronizar",
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f),
                    ) {
                        val addressText = remember(item.id) {
                            "${item.personaCliente?.direccion?.descripcion?.uppercase()}\n${item.personaCliente?.direccion?.corregimiento}, ${item.personaCliente?.direccion?.ciudad}, ${item.personaCliente?.direccion?.departamento}"
                        }
                        val clientText = remember(item.id) {
                            "${item.personaCliente?.primerNombre} ${item.personaCliente?.primerApellido}".toCapitalCase() + " - ${item.personaCliente?.numeroCedula}"
                        }
                        Text(
                            text = addressText,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = clientText,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        val serialLabel = stringResource(R.string.copy_serial)
                        val serial = getContadorSerial(item.id)
                        val serialText = remember(item.id, serial) {
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.LightGray
                                    )
                                ) {
                                    append("$serialLabel: ")
                                }
                                withStyle(style = SpanStyle(color = Color.White)) {
                                    append("${serial ?: item.contador?.serial ?: "N/A"} - ${item.contador?.nombreTipoContador}")
                                }
                            }
                        }
                        Text(
                            text = serialText,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }

                    if (isInvoiceAvailable(item.id)) {
                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(
                            modifier = Modifier
                                .background(
                                    color = Color.DarkGray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .size(32.dp),
                            onClick = { onNavigateToInvoice(item.id) },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Receipt,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}