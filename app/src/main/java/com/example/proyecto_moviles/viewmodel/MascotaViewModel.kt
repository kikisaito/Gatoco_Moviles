package com.example.proyecto_moviles.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.proyecto_moviles.model.GatocoDatabase
import com.example.proyecto_moviles.model.Mascota
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MascotaViewModel(application: Application) : AndroidViewModel(application) {


    private val db = Room.databaseBuilder(
        application,
        GatocoDatabase::class.java,
        "gatoco_db"
    ).build()

    private val dao = db.mascotaDao()


    val listaMascotas: StateFlow<List<Mascota>> = dao.obtenerTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun agregarMascota(
        nombre: String,
        raza: String,
        edad: String,
        dueno: String,
        telefono: String,
        enfermedad: String
    ) {
        viewModelScope.launch {
            val nuevaMascota = Mascota(
                nombre = nombre,
                raza = raza,
                edad = edad.toIntOrNull() ?: 0,
                dueno = dueno,
                telefono = telefono,
                enfermedad = enfermedad
            )
            dao.insertar(nuevaMascota)
        }
    }

    fun borrarMascota(mascota: Mascota) {
        viewModelScope.launch {
            dao.eliminar(mascota)
        }
    }
}