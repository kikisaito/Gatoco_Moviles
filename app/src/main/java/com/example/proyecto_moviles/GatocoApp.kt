package com.example.proyecto_moviles

import android.app.Application
import androidx.room.Room
import com.example.proyecto_moviles.model.GatocoDatabase // Importación de tu clase de base de datos

class GatocoApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            // El contexto de la aplicación
            this,
            // La clase abstracta de tu base de datos
            GatocoDatabase::class.java,
            // El nombre del archivo de la base de datos
            "gatoco_db"
        )
            // Permite que Room destruya y recree las tablas si subes la versión sin migraciones explícitas.
            // Esto es crucial porque subimos la versión a 6 (para añadir el veterinarioAsignado).
            .fallbackToDestructiveMigration()
            .build()
    }
}