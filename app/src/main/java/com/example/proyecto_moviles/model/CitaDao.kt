package com.example.proyecto_moviles.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {
    @Insert
    suspend fun agendar(cita: Cita)

    @Delete
    suspend fun eliminar(cita: Cita)

    @Query("SELECT * FROM citas ORDER BY fecha ASC, hora ASC")
    fun obtenerTodas(): Flow<List<Cita>>

    @Query("SELECT COUNT(*) FROM citas WHERE estado = 'Pendiente'")
    fun contarPendientes(): Flow<Int>

    @Query("SELECT COUNT(*) FROM citas WHERE estado = 'Completada'")
    fun contarCompletadas(): Flow<Int>

    @Query("UPDATE citas SET estado = 'Completada', diagnostico = :diag, tratamiento = :trat WHERE id = :id")
    suspend fun atenderCita(id: Int, diag: String, trat: String)

    @Query("SELECT * FROM citas WHERE veterinarioAsignado = :vetNombre ORDER BY fecha ASC, hora ASC")
    fun obtenerCitasPorVeterinario(vetNombre: String): Flow<List<Cita>>

    @Query("SELECT * FROM citas WHERE estado = 'Completada' ORDER BY fecha DESC, hora DESC")
    fun obtenerCitasCompletadas(): Flow<List<Cita>>

    @Query("SELECT * FROM usuarios WHERE esVeterinario = 1")
    suspend fun obtenerVeterinarios(): List<Usuario>


}