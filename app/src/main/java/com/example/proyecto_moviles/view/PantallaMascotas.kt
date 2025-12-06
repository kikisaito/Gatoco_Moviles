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
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // importanteeeeeeeee
import coil.compose.AsyncImage
import com.example.proyecto_moviles.viewmodel.MascotaViewModel

@Composable
fun PantallaMascotas(
    viewModel: MascotaViewModel = viewModel(factory = MascotaViewModel.Factory),
    onCerrarSesion: () -> Unit = {}
) {
    val listaMascotas by viewModel.listaMascotas.collectAsState(initial = emptyList())

    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var nombreDueno by remember { mutableStateOf("") }


    var fotoSeleccionadaUri by remember { mutableStateOf<Uri?>(null) }

    val launcherFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> fotoSeleccionadaUri = uri }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Registro de Pacientes", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))


        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                onClick = { launcherFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (fotoSeleccionadaUri != null) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (fotoSeleccionadaUri != null) "Foto Lista" else "Elegir Foto")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Mascota") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = raza, onValueChange = { raza = it }, label = { Text("Raza") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nombreDueno, onValueChange = { nombreDueno = it }, label = { Text("Nombre Dueño") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {

                viewModel.registrarMascotaConFoto(
                    nombre = nombre,
                    raza = raza,
                    dueno = nombreDueno,
                    fotoUri = fotoSeleccionadaUri
                )

                nombre = ""; raza = ""; nombreDueno = ""; fotoSeleccionadaUri = null
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Guardar Paciente")
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
                            Text("${mascota.raza} - ${mascota.nombreDueno}")
                        }

                        IconButton(onClick = { viewModel.eliminarMascota(mascota) }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}