package com.example.proyecto_moviles.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_moviles.viewmodel.MascotaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgendarCita(viewModel: MascotaViewModel) {


    var mascotaNombre by remember { mutableStateOf("") }
    var servicio by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }


    var veterinarioSeleccionado by remember { mutableStateOf("") }
    var mostrarVeterinarioMenu by remember { mutableStateOf(false) }

    // Obtener la lista de nombres de veterinarios del ViewModel
    val listaNombresVeterinarios by viewModel.listaNombresVeterinarios.collectAsState(initial = emptyList())

    // Estados para los menús desplegables
    var mostrarServicioMenu by remember { mutableStateOf(false) }
    var mostrarMascotaMenu by remember { mutableStateOf(false) }

    // Obtener las mascotas para el selector
    val listaMascotas by viewModel.listaMascotas.collectAsState(initial = emptyList())
    val servicios = listOf("Consulta General", "Vacunación", "Cirugía Menor", "Estética")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Agendar Nueva Cita", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Selecciona los detalles de tu consulta", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))


        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = mascotaNombre,
                onValueChange = { },
                label = { Text("Mascota") },
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, Modifier.clickable { mostrarMascotaMenu = true }) },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(expanded = mostrarMascotaMenu, onDismissRequest = { mostrarMascotaMenu = false }) {
                listaMascotas.forEach { mascota ->
                    DropdownMenuItem(
                        text = { Text(mascota.nombre) },
                        onClick = {
                            mascotaNombre = mascota.nombre
                            mostrarMascotaMenu = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 2. Selector de Servicio
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = servicio,
                onValueChange = { },
                label = { Text("Servicio Requerido") },
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, Modifier.clickable { mostrarServicioMenu = true }) },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(expanded = mostrarServicioMenu, onDismissRequest = { mostrarServicioMenu = false }) {
                servicios.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            servicio = item
                            mostrarServicioMenu = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))


        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = veterinarioSeleccionado,
                onValueChange = { },
                label = { Text("Elegir Veterinario") },
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, Modifier.clickable { mostrarVeterinarioMenu = true }) },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(expanded = mostrarVeterinarioMenu, onDismissRequest = { mostrarVeterinarioMenu = false }) {
                listaNombresVeterinarios.forEach { nombre ->
                    DropdownMenuItem(
                        text = { Text(nombre) },
                        onClick = {
                            veterinarioSeleccionado = nombre
                            mostrarVeterinarioMenu = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // 4. Campos de Fecha y Hora
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (DD/MM/AAAA)") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (HH:MM)") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {
                if (mascotaNombre.isNotBlank() && servicio.isNotBlank() && fecha.isNotBlank() && hora.isNotBlank() && veterinarioSeleccionado.isNotBlank()) {
                    viewModel.agendarCita(mascotaNombre, servicio, fecha, hora, veterinarioSeleccionado)

                    mascotaNombre = ""; servicio = ""; fecha = ""; hora = ""; veterinarioSeleccionado = ""
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Confirmar Cita Médica")
        }
    }
}