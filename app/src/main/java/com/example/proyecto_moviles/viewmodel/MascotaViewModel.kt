package com.example.proyecto_moviles.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.proyecto_moviles.GatocoApp
import com.example.proyecto_moviles.model.Cita
import com.example.proyecto_moviles.model.CitaDao
import com.example.proyecto_moviles.model.Mascota
import com.example.proyecto_moviles.model.MascotaDao
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class MascotaViewModel(
    private val mascotaDao: MascotaDao,
    private val citaDao: CitaDao
) : ViewModel() {


    val listaMascotas: Flow<List<Mascota>> = mascotaDao.obtenerTodas()
    private val storageRef = Firebase.storage.reference

    fun registrarMascotaConFoto(nombre: String, raza: String, dueno: String, fotoUri: Uri?) {
        viewModelScope.launch {
            if (fotoUri != null) {
                val ref = storageRef.child("mascotas/${UUID.randomUUID()}.jpg")
                ref.putFile(fotoUri).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        guardarEnRoom(nombre, raza, dueno, uri.toString())
                    }
                }.addOnFailureListener { guardarEnRoom(nombre, raza, dueno, null) }
            } else {
                guardarEnRoom(nombre, raza, dueno, null)
            }
        }
    }

    private fun guardarEnRoom(nombre: String, raza: String, dueno: String, url: String?) {
        viewModelScope.launch {
            mascotaDao.guardar(Mascota(nombre = nombre, raza = raza, edad = "0", nombreDueno = dueno, telefonoDueno = "000", fotoUrl = url))
        }
    }

    fun eliminarMascota(mascota: Mascota) = viewModelScope.launch { mascotaDao.eliminar(mascota) }


    val listaCitas: Flow<List<Cita>> = citaDao.obtenerTodas()
    val totalMascotas: Flow<Int> = listaMascotas.map { it.size }
    val totalPendientes: Flow<Int> = citaDao.contarPendientes()
    val totalCompletadas: Flow<Int> = citaDao.contarCompletadas()

    val proximaCita: Flow<Cita?> = listaCitas.map { citas ->
        citas.firstOrNull { it.estado == "Pendiente" }
    }

    fun agendarCita(mascota: String, servicio: String, fecha: String, hora: String) {
        viewModelScope.launch {
            citaDao.agendar(Cita(mascotaNombre = mascota, servicio = servicio, fecha = fecha, hora = hora))
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GatocoApp)
                MascotaViewModel(app.database.mascotaDao(), app.database.citaDao())
            }
        }
    }
}