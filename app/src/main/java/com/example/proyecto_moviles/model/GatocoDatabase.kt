package com.example.proyecto_moviles.model

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Mascota::class], version = 1, exportSchema = false)
abstract class GatocoDatabase : RoomDatabase() {


    abstract fun mascotaDao(): MascotaDao
}