package com.example.proyecto_moviles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.proyecto_moviles.GatocoApp
import com.example.proyecto_moviles.model.Mascota
import com.example.proyecto_moviles.model.MascotaDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MascotaViewModel(private val mascotaDao: MascotaDao) : ViewModel() {

    val listaMascotas: Flow<List<Mascota>> = mascotaDao.obtenerTodas()

    fun guardarMascota(nombre: String, raza: String, edad: String, dueno: String, tel: String, fotoUrl: String?) {
        val nuevaMascota = Mascota(
            nombre = nombre,
            raza = raza,
            edad = edad,
            nombreDueno = dueno,
            telefonoDueno = tel,
            fotoUrl = fotoUrl
        )
        viewModelScope.launch {
            mascotaDao.guardar(nuevaMascota)
        }
    }


    fun eliminarMascota(mascota: Mascota) {
        viewModelScope.launch {
            mascotaDao.eliminar(mascota)
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GatocoApp)
                MascotaViewModel(application.database.mascotaDao())
            }
        }
    }
}