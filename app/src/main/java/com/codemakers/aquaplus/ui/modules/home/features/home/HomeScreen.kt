package com.codemakers.aquaplus.ui.modules.home.features.home

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codemakers.aquaplus.BuildConfig
import com.codemakers.aquaplus.R
import com.codemakers.aquaplus.ui.modules.home.features.profile.ProfileScreen
import com.codemakers.aquaplus.ui.modules.home.features.route.RouteScreen
import com.codemakers.aquaplus.ui.theme.primaryDarkColor
// Imports del menú lateral (drawer) - deshabilitado temporalmente
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.material3.DrawerValue
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalNavigationDrawer
//import androidx.compose.material3.NavigationDrawerItem
//import androidx.compose.material3.rememberDrawerState
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import com.codemakers.aquaplus.BuildConfig
//import kotlinx.coroutines.launch

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {

    object Route : BottomNavItem("home", Icons.Default.Home, "Ruta")

    object Profile : BottomNavItem("profile", Icons.Default.Person, "Perfil")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToForm: (employeeRouteId: Int) -> Unit = {},
    onNavigateToInvoice: (employeeRouteId: Int) -> Unit,
) {
    val internalNavController = rememberNavController()
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    val context = LocalContext.current

    // Menú lateral (drawer) - deshabilitado temporalmente
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(0.7f)
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.surface)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(180.dp)
//                        .background(MaterialTheme.colorScheme.primary)
//                        .padding(16.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_launcher_monochrome),
//                            contentDescription = stringResource(R.string.app_name),
//                            modifier = Modifier.size(80.dp)
//                        )
//                        Spacer(Modifier.height(8.dp))
//                        Text(
//                            text = stringResource(R.string.app_name),
//                            style = MaterialTheme.typography.headlineSmall,
//                            color = Color.White
//                        )
//                    }
//                }
//                NavigationDrawerItem(
//                    label = { Text("Configuración") },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        Toast.makeText(context, "Configuración Clicked", Toast.LENGTH_SHORT).show()
//                    },
//                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Configuración") }
//                )
//                Spacer(modifier = Modifier.weight(1f))
//                Text(
//                    text = "Versión: ${BuildConfig.VERSION_NAME}",
//                    modifier = Modifier.padding(16.dp),
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray
//                )
//            }
//        }
//    ) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryDarkColor,
                    titleContentColor = Color.White,
                ),
                title = { Text(stringResource(R.string.app_name), color = Color.White) },
                actions = {
                    Text(
                        text = "v${BuildConfig.VERSION_NAME}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
            )
        },
        bottomBar = { MyBottomNavBar(navController = internalNavController) }
    ) { innerPadding ->
        NavHost(
            navController = internalNavController,
            startDestination = BottomNavItem.Route.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Route.route) {
                RouteScreen(
                    onNavigateToForm = onNavigateToForm,
                    onNavigateToInvoice = onNavigateToInvoice,
                )
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onNavigateToLogin = onNavigateToLogin,
                )
            }
        }
    }
//    } // Cierre del ModalNavigationDrawer
}

@Composable
fun MyBottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(modifier = Modifier.height(56.dp)) {
        listOf(BottomNavItem.Route, BottomNavItem.Profile).forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}