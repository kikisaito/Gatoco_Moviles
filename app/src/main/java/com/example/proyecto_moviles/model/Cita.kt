package com.example.proyecto_moviles.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mascotaNombre: String,
    val servicio: String,
    val fecha: String,
    val hora: String,
    val estado: String = "Pendiente",

    val diagnostico: String? = null,
    val tratamiento: String? = null,


    val veterinarioAsignado: String
)