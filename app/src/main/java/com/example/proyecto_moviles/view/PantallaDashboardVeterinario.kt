package com.example.proyecto_moviles.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_moviles.viewmodel.MascotaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDashboardVeterinario(
    nombreUsuario: String,
    viewModel: MascotaViewModel,
    onCerrarSesion: () -> Unit
) {

    LaunchedEffect(nombreUsuario) {

        viewModel.establecerUsuario(nombreUsuario, esVet = true)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var pantallaActual by remember { mutableStateOf(0) }

    val citas by viewModel.citas.collectAsState(initial = emptyList())
    val pendientes by viewModel.citasPendientes.collectAsState()
    val completadas by viewModel.citasCompletadas.collectAsState()

    val colorMenu = Color(0xFF1A237E)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = colorMenu,
                drawerContentColor = Color.White
            ) {
                Column(modifier = Modifier.fillMaxHeight().padding(16.dp)) {
                    Text(text = nombreUsuario, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Veterinario", fontSize = 14.sp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                    BotonMenuVet("Dashboard", Icons.Default.Dashboard, pantallaActual == 0) {
                        pantallaActual = 0
                        scope.launch { drawerState.close() }
                    }
                    BotonMenuVet("Gestionar Pacientes", Icons.Default.FolderShared, pantallaActual == 1) {
                        pantallaActual = 1
                        scope.launch { drawerState.close() }
                    }
                    BotonMenuVet("Historial de Citas", Icons.Default.History, pantallaActual == 2) {
                        pantallaActual = 2
                        scope.launch { drawerState.close() }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = onCerrarSesion,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Panel Veterinario", color = colorMenu, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = colorMenu)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (pantallaActual) {
                    0 -> {
                        ContenidoDashboard(
                            nombreUsuario = nombreUsuario,
                            citas = citas,
                            pendientes = pendientes,
                            completadas = completadas,
                            onDeleteCita = { cita -> viewModel.eliminarCita(cita) },
                            onEditCita = { },
                            puedeAtender = true,
                            onAtenderCita = { c, d, t -> viewModel.atenderCita(c, d, t) }
                        )
                    }
                    1 -> PantallaGestionarPacientes(viewModel)
                    2 -> Text("Historial Avanzado (En construcción)", modifier = Modifier.align(
                        Alignment.Center))
                }
            }
        }
    }
}


@Composable
fun BotonMenuVet(texto: String, icono: ImageVector, seleccionado: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(texto) },
        icon = { Icon(icono, contentDescription = null) },
        selected = seleccionado,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            unselectedTextColor = Color.White,
            unselectedIconColor = Color.White,
            selectedContainerColor = Color.White.copy(alpha = 0.2f),
            selectedTextColor = Color.White,
            selectedIconColor = Color.White
        ),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}