package com.example.proyecto_moviles.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_moviles.viewmodel.MascotaViewModel

@Composable
fun PantallaMascotas(
    // Inyectamos el ViewModel automáticamente usando la Fábrica que creamos antes
    viewModel: MascotaViewModel = viewModel(factory = MascotaViewModel.Factory)
) {
    val listaMascotas by viewModel.listaMascotas.collectAsState(initial = emptyList())

    // 2. Variables para los campos de texto del formulario
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var nombreDueno by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(text = "Registro de Pacientes", style = MaterialTheme.typography.headlineMedium)

        // --- FORMULARIO ---
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre Mascota") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = raza,
            onValueChange = { raza = it },
            label = { Text("Raza") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nombreDueno,
            onValueChange = { nombreDueno = it },
            label = { Text("Nombre Dueño") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // Guardamos en la Base de Datos Local
                viewModel.guardarMascota(
                    nombre = nombre,
                    raza = raza,
                    edad = "0",
                    dueno = nombreDueno,
                    tel = "000",
                    fotoUrl = null
                )
                // Limpiamos los campos
                nombre = ""
                raza = ""
                nombreDueno = ""
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Icon(Icons.Default.Pets, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registrar Mascota")
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Pacientes Registrados (${listaMascotas.size})", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(listaMascotas) { mascota ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = mascota.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(text = "${mascota.raza} - Dueño: ${mascota.nombreDueno}")
                        }
                        IconButton(onClick = { viewModel.eliminarMascota(mascota) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}