package com.example.proyecto_moviles.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mascotas")
data class Mascota(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val dueno: String,
    val telefono: String,
    val enfermedad: String,
    val fechaRegistro: Long = System.currentTimeMillis()
)