package com.example.proyecto_moviles.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_moviles.model.Cita
import com.example.proyecto_moviles.model.CitaDao
import com.example.proyecto_moviles.model.Mascota
import com.example.proyecto_moviles.model.MascotaDao
import com.example.proyecto_moviles.model.UsuarioDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MascotaViewModel(
    private val mascotaDao: MascotaDao,
    private val citaDao: CitaDao,
    private val usuarioDao: UsuarioDao,
    private val nombreVeterinarioInicial: String? // Solo sirve para la primera carga
) : ViewModel() {

    // --- CONTROL DE USUARIO ACTUAL ---
    private val _nombreUsuarioActual = MutableStateFlow(nombreVeterinarioInicial)
    private val _esVeterinario = MutableStateFlow(nombreVeterinarioInicial != null)

    fun establecerUsuario(nombre: String, esVet: Boolean) {
        _nombreUsuarioActual.value = nombre
        _esVeterinario.value = esVet
    }

    // --- 1. LISTAS REACTIVAS ---

    val listaNombresVeterinarios: StateFlow<List<String>> = usuarioDao.obtenerNombresVeterinarios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val listaMascotas: StateFlow<List<Mascota>> = mascotaDao.obtenerTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ¡MAGIA! Si cambia el usuario, cambia la lista de citas automáticamente
    val citas: StateFlow<List<Cita>> = _nombreUsuarioActual.flatMapLatest { nombre ->
        // Necesitamos leer el valor actual de _esVeterinario dentro del flatMapLatest
        val esVet = _esVeterinario.value

        if (esVet && nombre != null) {
            // Este método DEBE existir en CitaDao
            citaDao.obtenerCitasPorVeterinario(nombre)
        } else {
            // Si es cliente, solo ve sus citas (asumiendo que obtenerCitasPorDueno existe)
            // Si el cliente no tiene nombre de dueño, ve todas.
            citaDao.obtenerTodas()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- 2. ESTADÍSTICAS PARA EL DASHBOARD (TARJETAS) ---
    val citasPendientes: StateFlow<Int> = citaDao.contarPendientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val citasCompletadas: StateFlow<Int> = citaDao.contarCompletadas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


    // --- 3. ACCIONES DE MASCOTAS (CON EDITAR) ---

    fun registrarOActualizarMascota(
        id: Int = 0, // Si es 0 es nuevo, si tiene ID es actualizar
        nombre: String, raza: String, edad: String,
        nombreDueno: String, telefonoDueno: String, fotoUri: Uri?, fotoUrlExistente: String?
    ) {
        viewModelScope.launch {
            // Decidimos qué foto usar: la nueva (Uri), la vieja (Url) o null
            val fotoFinal = fotoUri?.toString() ?: fotoUrlExistente

            val mascota = Mascota(
                id = id, // Importante para saber si editamos
                nombre = nombre,
                raza = raza,
                edad = edad,
                nombreDueno = nombreDueno,
                telefonoDueno = telefonoDueno,
                fotoUrl = fotoFinal
            )

            if (id == 0) {
                mascotaDao.guardar(mascota) // Nuevo
            } else {
                mascotaDao.actualizar(mascota) // Editar
            }
        }
    }

    fun eliminarMascota(mascota: Mascota) {
        viewModelScope.launch { mascotaDao.eliminar(mascota) }
    }

    // --- 4. ACCIONES DE CITAS ---

    fun agendarCita(mascota: String, servicio: String, fecha: String, hora: String, veterinario: String) {
        viewModelScope.launch {
            val nuevaCita = Cita(
                mascotaNombre = mascota,
                servicio = servicio,
                fecha = fecha,
                hora = hora,
                veterinarioAsignado = veterinario,
                estado = "Pendiente"
            )
            citaDao.agendar(nuevaCita)
        }
    }

    fun eliminarCita(cita: Cita) {
        // Asegúrate de que CitaDao tiene el método @Delete fun eliminar(cita: Cita)
        viewModelScope.launch { citaDao.eliminar(cita) }
    }

    fun atenderCita(cita: Cita, diagnostico: String, tratamiento: String) {
        viewModelScope.launch {
            // Asegúrate de que CitaDao tiene una función @Query para actualizar
            citaDao.atenderCita(cita.id, diagnostico, tratamiento)
        }
    }
}