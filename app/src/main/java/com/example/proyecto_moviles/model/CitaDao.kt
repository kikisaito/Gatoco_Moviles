package com.example.proyecto_moviles.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {
    @Insert
    suspend fun agendar(cita: Cita)

    @Query("SELECT * FROM citas ORDER BY fecha ASC, hora ASC")
    fun obtenerTodas(): Flow<List<Cita>>

    @Query("SELECT COUNT(*) FROM citas WHERE estado = 'Pendiente'")
    fun contarPendientes(): Flow<Int>

    @Query("SELECT COUNT(*) FROM citas WHERE estado = 'Completada'")
    fun contarCompletadas(): Flow<Int>
}