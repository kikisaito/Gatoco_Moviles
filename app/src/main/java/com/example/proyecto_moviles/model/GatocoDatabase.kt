package com.example.proyecto_moviles.model

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Mascota::class, Usuario::class], version = 2)
abstract class GatocoDatabase : RoomDatabase() {
    abstract fun mascotaDao(): MascotaDao
    abstract fun usuarioDao(): UsuarioDao
}