package com.example.proyecto_moviles.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_moviles.model.Cita
import com.example.proyecto_moviles.viewmodel.MascotaViewModel

@Composable
fun ContenidoDashboard(viewModel: MascotaViewModel) {
    // Estos "collectAsState" hacen que los números cambien solos si la base de datos cambia
    val mascotasCount by viewModel.totalMascotas.collectAsState(initial = 0)
    val pendientesCount by viewModel.totalPendientes.collectAsState(initial = 0)
    val completadasCount by viewModel.totalCompletadas.collectAsState(initial = 0)
    val proximaCita by viewModel.proximaCita.collectAsState(initial = null)
    val listaCitas by viewModel.listaCitas.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Dashboard", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C7A90))
        Text("Resumen de tu veterinaria", color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // --- FILA 1 DE TARJETAS ---
        Row(Modifier.fillMaxWidth()) {
            TarjetaDashboard(
                titulo = "Próxima Cita",
                dato = proximaCita?.fecha ?: "--/--", // Si es null, muestra --/--
                subdato = proximaCita?.hora ?: "Sin agenda",
                icono = Icons.Default.AccessTime,
                colorIcono = Color(0xFF03A9F4), // Azul
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TarjetaDashboard(
                titulo = "Pendientes",
                dato = pendientesCount.toString(),
                subdato = "Citas",
                icono = Icons.Default.CalendarToday,
                colorIcono = Color(0xFFE91E63), // Rosa
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- FILA 2 DE TARJETAS ---
        Row(Modifier.fillMaxWidth()) {
            TarjetaDashboard(
                titulo = "Mascotas",
                dato = mascotasCount.toString(),
                subdato = "Registradas",
                icono = Icons.Default.Pets,
                colorIcono = Color(0xFF673AB7), // Morado
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TarjetaDashboard(
                titulo = "Completadas",
                dato = completadasCount.toString(),
                subdato = "Histórico",
                icono = Icons.Default.CheckCircle,
                colorIcono = Color(0xFF4CAF50), // Verde
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text("Agenda Pendiente", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(listaCitas) { cita ->
                ItemCita(cita)
            }
        }
    }
}

@Composable
fun TarjetaDashboard(titulo: String, dato: String, subdato: String, icono: ImageVector, colorIcono: Color, modifier: Modifier) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(colorIcono.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = colorIcono)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(titulo, fontSize = 12.sp, color = Color.Gray)
                Text(dato, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(subdato, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ItemCita(cita: Cita) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(cita.mascotaNombre, fontWeight = FontWeight.Bold)
                Text(cita.servicio, fontSize = 12.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(cita.fecha, fontWeight = FontWeight.Bold)
                Text(cita.hora, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}