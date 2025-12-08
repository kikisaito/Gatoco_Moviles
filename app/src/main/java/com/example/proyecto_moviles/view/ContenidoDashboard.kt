package com.example.proyecto_moviles.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_moviles.model.Cita

@Composable
fun ContenidoDashboard(
    nombreUsuario: String,
    citas: List<Cita>,
    pendientes: Int, // <--- NUEVO
    completadas: Int, // <--- NUEVO
    onDeleteCita: (Cita) -> Unit,
    onEditCita: (Cita) -> Unit,
    puedeAtender: Boolean = false,
    onAtenderCita: (Cita, String, String) -> Unit = { _, _, _ -> }
) {

    var mostrarDialogoAtender by remember { mutableStateOf(false) }
    var citaPorAtender by remember { mutableStateOf<Cita?>(null) }
    var diagnostico by remember { mutableStateOf("") }
    var tratamiento by remember { mutableStateOf("") }

    if (mostrarDialogoAtender && citaPorAtender != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoAtender = false },
            title = { Text("Atender: ${citaPorAtender?.mascotaNombre}") },
            text = { Column {
                OutlinedTextField(value = diagnostico, onValueChange = { diagnostico = it }, label = { Text("Diagnóstico") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = tratamiento, onValueChange = { tratamiento = it }, label = { Text("Tratamiento") })
            }},
            confirmButton = { Button(onClick = { onAtenderCita(citaPorAtender!!, diagnostico, tratamiento); mostrarDialogoAtender = false; diagnostico=""; tratamiento="" }) { Text("Finalizar") } },
            dismissButton = { TextButton(onClick = { mostrarDialogoAtender = false }) { Text("Cancelar") } }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Hola, $nombreUsuario", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // --- TARJETAS DE ESTADÍSTICAS (LAS QUE FALTABAN) ---
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CardEstadistica("Pendientes", pendientes, Icons.Default.Schedule, Color(0xFFFFA000), Modifier.weight(1f))
            CardEstadistica("Atendidas", completadas, Icons.Default.TaskAlt, Color(0xFF4CAF50), Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text("Citas Programadas", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(citas) { cita ->
                ItemCita(
                    cita = cita,
                    onDelete = onDeleteCita,
                    onEdit = onEditCita,
                    esVeterinario = puedeAtender,
                    onAtenderClick = { citaPorAtender = cita; mostrarDialogoAtender = true }
                )
            }
        }
    }
}

@Composable
fun CardEstadistica(titulo: String, cantidad: Int, icono: ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icono, null, tint = color, modifier = Modifier.size(32.dp))
            Text(cantidad.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            Text(titulo, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ItemCita(cita: Cita, onDelete: (Cita) -> Unit, onEdit: (Cita) -> Unit, esVeterinario: Boolean, onAtenderClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), elevation = CardDefaults.cardElevation(4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(cita.mascotaNombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("${cita.servicio} - ${cita.fecha} ${cita.hora}")
                    Text("Dr. ${cita.veterinarioAsignado}", fontSize = 12.sp, color = Color.Gray)
                    if (cita.estado != "Pendiente") Text("Estado: ${cita.estado}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                }
                if(esVeterinario && cita.estado == "Pendiente") {
                    Button(onClick = onAtenderClick, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))) { Text("Atender") }
                } else {
                    IconButton(onClick = { onDelete(cita) }) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
                }
            }
        }
    }
}