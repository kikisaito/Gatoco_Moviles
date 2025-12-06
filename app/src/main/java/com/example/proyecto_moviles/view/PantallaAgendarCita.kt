package com.example.proyecto_moviles.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_moviles.viewmodel.MascotaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgendarCita(viewModel: MascotaViewModel) {
    val context = LocalContext.current
    val listaMascotas by viewModel.listaMascotas.collectAsState(initial = emptyList())


    var mascotaSeleccionada by remember { mutableStateOf("") }
    var servicioSeleccionado by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }


    val servicios = listOf("Consulta General", "Vacunación", "Desparasitación", "Cirugía", "Estética/Baño")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Nueva Cita", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C7A90))
        Text("Completa los datos para agendar", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        // 1. SELECCIONAR MASCOTA
        Text("1. ¿Quién es el paciente?", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (listaMascotas.isEmpty()) {
            Text("No tienes mascotas registradas. Ve a la sección 'Mascotas' primero.", color = Color.Red)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listaMascotas) { mascota ->
                    FilterChip(
                        selected = mascotaSeleccionada == mascota.nombre,
                        onClick = { mascotaSeleccionada = mascota.nombre },
                        label = { Text(mascota.nombre) },
                        leadingIcon = if (mascotaSeleccionada == mascota.nombre) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. SELECCIONAR SERVICIO
        Text("2. Tipo de Servicio", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Usamos un RadioButton group simple
        servicios.forEach { servicio ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .selectable(
                        selected = (servicioSeleccionado == servicio),
                        onClick = { servicioSeleccionado = servicio }
                    )
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (servicioSeleccionado == servicio),
                    onClick = { servicioSeleccionado = servicio }
                )
                Text(text = servicio, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("3. ¿Cuándo?", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (Ej: 15/12/2025)") },
            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (Ej: 10:30 AM)") },
            leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = {
                if (mascotaSeleccionada.isNotEmpty() && servicioSeleccionado.isNotEmpty() && fecha.isNotEmpty() && hora.isNotEmpty()) {
                    viewModel.agendarCita(mascotaSeleccionada, servicioSeleccionado, fecha, hora)
                    Toast.makeText(context, "¡Cita Agendada con Éxito!", Toast.LENGTH_LONG).show()


                    mascotaSeleccionada = ""
                    servicioSeleccionado = ""
                    fecha = ""
                    hora = ""
                } else {
                    Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7A90))
        ) {
            Text("Confirmar Cita", fontSize = 18.sp)
        }
    }
}