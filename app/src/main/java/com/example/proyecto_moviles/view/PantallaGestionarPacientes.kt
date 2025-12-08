package com.example.proyecto_moviles.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.proyecto_moviles.viewmodel.MascotaViewModel

@Composable
fun PantallaGestionarPacientes(viewModel: MascotaViewModel) {

    val listaMascotas by viewModel.listaMascotas.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestionar Pacientes", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Información de las mascotas de tus clientes", color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        // Encabezados de Tabla
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("Paciente", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Raza/Especie", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Propietario", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Acciones", fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp))
        }
        HorizontalDivider()

        LazyColumn {
            items(listaMascotas) { mascota ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        if (mascota.fotoUrl != null) {
                            AsyncImage(
                                model = mascota.fotoUrl, contentDescription = null,
                                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Pets, null, modifier = Modifier.size(40.dp).padding(4.dp))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(mascota.nombre, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(mascota.raza, modifier = Modifier.weight(1f), fontSize = 12.sp)
                        Text(mascota.nombreDueno, modifier = Modifier.weight(1f), fontSize = 12.sp)

                        IconButton(onClick = { viewModel.eliminarMascota(mascota) }, modifier = Modifier.width(40.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}