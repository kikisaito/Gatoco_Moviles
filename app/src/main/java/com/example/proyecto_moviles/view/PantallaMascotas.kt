package com.example.proyecto_moviles.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.proyecto_moviles.viewmodel.MascotaViewModel
import com.example.proyecto_moviles.model.Mascota

@Composable
fun PantallaMascotas(
    viewModel: MascotaViewModel,
    onCerrarSesion: () -> Unit = {}
) {
    val listaMascotas by viewModel.listaMascotas.collectAsState(initial = emptyList())

    // Estados del formulario
    var idMascotaEditar by remember { mutableStateOf(0) }
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var nombreDueno by remember { mutableStateOf("") }
    var telefonoDueno by remember { mutableStateOf("") }
    var fotoUrlExistente by remember { mutableStateOf<String?>(null) }
    var fotoSeleccionadaUri by remember { mutableStateOf<Uri?>(null) }

    val launcherFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> fotoSeleccionadaUri = uri }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            if (idMascotaEditar == 0) "Registro de Pacientes" else "Editando a $nombre",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))


        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                onClick = { launcherFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (fotoSeleccionadaUri != null || fotoUrlExistente != null) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (fotoSeleccionadaUri != null) "Nueva Foto Seleccionada" else if (fotoUrlExistente != null) "Mantener Foto Actual" else "Elegir Foto")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Formulario
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Mascota") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = raza, onValueChange = { raza = it }, label = { Text("Raza") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nombreDueno, onValueChange = { nombreDueno = it }, label = { Text("Nombre Dueño") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefonoDueno, onValueChange = { telefonoDueno = it }, label = { Text("Teléfono Dueño") }, modifier = Modifier.fillMaxWidth())

        // Botón Guardar / Actualizar
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (idMascotaEditar != 0) {
                Button(
                    onClick = {
                        // CANCELAR EDICIÓN
                        idMascotaEditar = 0
                        nombre = ""; raza = ""; edad = ""; nombreDueno = ""; telefonoDueno = ""; fotoSeleccionadaUri = null; fotoUrlExistente = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
            }

            Button(
                onClick = {
                    if (nombre.isNotBlank() && nombreDueno.isNotBlank()) {
                        viewModel.registrarOActualizarMascota(
                            id = idMascotaEditar,
                            nombre = nombre,
                            raza = raza,
                            edad = edad,
                            nombreDueno = nombreDueno,
                            telefonoDueno = telefonoDueno,
                            fotoUri = fotoSeleccionadaUri,
                            fotoUrlExistente = fotoUrlExistente
                        )
                        idMascotaEditar = 0
                        nombre = ""; raza = ""; edad = ""; nombreDueno = ""; telefonoDueno = ""; fotoSeleccionadaUri = null; fotoUrlExistente = null
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (idMascotaEditar == 0) "Guardar Paciente" else "Actualizar Datos")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(listaMascotas) { mascota ->
                Card(
                    modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (mascota.fotoUrl != null) {
                            AsyncImage(
                                model = mascota.fotoUrl,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.LightGray),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Pets, contentDescription = null, modifier = Modifier.size(60.dp))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(mascota.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("${mascota.raza} - ${mascota.edad}")
                        }


                        IconButton(onClick = {
                            // Al hacer clic, subimos los datos al formulario
                            idMascotaEditar = mascota.id
                            nombre = mascota.nombre
                            raza = mascota.raza
                            edad = mascota.edad
                            nombreDueno = mascota.nombreDueno
                            telefonoDueno = mascota.telefonoDueno
                            fotoUrlExistente = mascota.fotoUrl
                            fotoSeleccionadaUri = null // Reiniciamos selección nueva
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Blue)
                        }

                        // BOTÓN BORRAR
                        IconButton(onClick = { viewModel.eliminarMascota(mascota) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}