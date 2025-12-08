package com.example.proyecto_moviles.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun registrar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :pass LIMIT 1")
    suspend fun login(email: String, pass: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Query("SELECT nombre FROM usuarios WHERE esVeterinario = 1")
    fun obtenerNombresVeterinarios(): Flow<List<String>>
}