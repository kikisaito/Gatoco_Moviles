package com.example.proyecto_moviles.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Mascota::class, Usuario::class, Cita::class], version = 3)
abstract class GatocoDatabase : RoomDatabase() {

    abstract fun mascotaDao(): MascotaDao
    abstract fun usuarioDao(): UsuarioDao


    abstract fun citaDao(): CitaDao
}