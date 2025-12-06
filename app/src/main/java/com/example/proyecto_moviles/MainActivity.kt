package com.example.proyecto_moviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.* // Importante para el "remember" y "mutableStateOf"
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_moviles.view.PantallaLogin
import com.example.proyecto_moviles.view.PantallaMascotas
import com.example.proyecto_moviles.viewmodel.MascotaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val viewModel = ViewModelProvider(this)[MascotaViewModel::class.java]

        setContent {


            var usuarioLogueado by remember { mutableStateOf(false) }

            if (usuarioLogueado) {

                PantallaMascotas(viewModel = viewModel)
            } else {

                PantallaLogin(
                    onLoginSuccess = {

                        usuarioLogueado = true
                    }
                )
            }
        }
    }
}