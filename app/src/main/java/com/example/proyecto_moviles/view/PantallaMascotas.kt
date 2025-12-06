package com.example.proyecto_moviles.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_moviles.viewmodel.MascotaViewModel

@Composable
fun PantallaMascotas(viewModel: MascotaViewModel) {

    val listaMascotas by viewModel.listaMascotas.collectAsState()


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
        Text(text = "🐾 Veterinaria Gatoco", fontSize = 24.sp, color = Color(0xFF6200EE))

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Mascota") })
        OutlinedTextField(value = raza, onValueChange = { raza = it }, label = { Text("Raza") })
        OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad (Años)") })
        OutlinedTextField(value = dueno, onValueChange = { dueno = it }, label = { Text("Nombre Dueño") })
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
        OutlinedTextField(value = enfermedad, onValueChange = { enfermedad = it }, label = { Text("Motivo Consulta") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                viewModel.agregarMascota(nombre, raza, edad, dueno, telefono, enfermedad)


                nombre = ""; raza = ""; edad = ""; dueno = ""; telefono = ""; enfermedad = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registrar Paciente")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Divider()
        Text(text = "Pacientes Recientes", fontSize = 18.sp, modifier = Modifier.padding(8.dp))


        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(listaMascotas) { mascota ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = mascota.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(text = "${mascota.raza} - ${mascota.dueno}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Motivo: ${mascota.enfermedad}", style = MaterialTheme.typography.bodySmall, color = Color.Red)
                        }
                        // Botón de borrar
                        IconButton(onClick = { viewModel.borrarMascota(mascota) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}