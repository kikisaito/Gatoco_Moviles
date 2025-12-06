package com.example.proyecto_moviles

import android.app.Application
import androidx.room.Room
import com.example.proyecto_moviles.model.GatocoDatabase

class GatocoApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            this,
            GatocoDatabase::class.java,
            "gatoco_db"
        )   .fallbackToDestructiveMigration()
            .build()
    }
}