package com.example.proyecto_moviles.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascotas")
data class Mascota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val raza: String,
    val edad: String,
    val nombreDueno: String,
    val telefonoDueno: String,
    val fotoUrl: String? = null
)