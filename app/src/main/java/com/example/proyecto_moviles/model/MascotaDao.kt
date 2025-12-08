package com.example.proyecto_moviles.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface MascotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(mascota: Mascota)

    @Delete
    suspend fun eliminar(mascota: Mascota)


    @Query("SELECT * FROM mascotas")
    fun obtenerTodas(): Flow<List<Mascota>>

    @Query("SELECT * FROM mascotas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Mascota?

    @Update
    suspend fun actualizar(mascota: Mascota)

}