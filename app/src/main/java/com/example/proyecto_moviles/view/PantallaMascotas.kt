package com.example.proyecto_moviles.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_moviles.viewmodel.MascotaViewModel

@Composable
fun PantallaMascotas(
    viewModel: MascotaViewModel,
    onCerrarSesion: () -> Unit // 1. Nueva "orden" que recibe esta pantalla
) {
    val listaMascotas by viewModel.listaMascotas.collectAsState()

    // Variables del formulario
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var dueno by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var enfermedad by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🐾 Gatoco", fontSize = 24.sp, color = Color(0xFF6200EE))

            // 2. El Botón de Cerrar Sesión
            IconButton(onClick = { onCerrarSesion() }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión", tint = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- FORMULARIO RÁPIDO ---
        Text("Registrar Nueva Mascota", fontSize = 16.sp, color = Color.Gray)
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Mascota") }, modifier = Modifier.fillMaxWidth())
        // (Por brevedad visual en el código, pondré los campos principales, tú ya tenías los otros)
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(value = raza, onValueChange = { raza = it }, label = { Text("Raza") }, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, modifier = Modifier.weight(1f))
        }
        OutlinedTextField(value = dueno, onValueChange = { dueno = it }, label = { Text("Dueño") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = enfermedad, onValueChange = { enfermedad = it }, label = { Text("Motivo") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.agregarMascota(nombre, raza, edad, dueno, telefono, enfermedad)
                nombre = ""; raza = ""; edad = ""; dueno = ""; telefono = ""; enfermedad = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Text("Guardar Paciente")
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // --- LISTA ---
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(listaMascotas) { mascota ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                    modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(mascota.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("${mascota.raza} - ${mascota.dueno}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { viewModel.borrarMascota(mascota) }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}