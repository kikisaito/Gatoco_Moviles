package com.example.proyecto_moviles

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.proyecto_moviles.model.GatocoDatabase
import com.example.proyecto_moviles.model.Usuario
import com.example.proyecto_moviles.view.PantallaLogin
import com.example.proyecto_moviles.view.PantallaMascotas
import com.example.proyecto_moviles.viewmodel.MascotaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val db = Room.databaseBuilder(applicationContext, GatocoDatabase::class.java, "gatoco_db")
            .fallbackToDestructiveMigration()
            .build()

        val usuarioDao = db.usuarioDao()
        val mascotaViewModel = ViewModelProvider(this)[MascotaViewModel::class.java]

        setContent {
            var usuarioLogueado by remember { mutableStateOf(false) }

            if (usuarioLogueado) {

                PantallaMascotas(
                    viewModel = mascotaViewModel,
                    onCerrarSesion = {
                        usuarioLogueado = false
                    }
                )
            } else {

                PantallaLogin(
                    onLoginSuccess = { email, password, nombre, esVeterinario, esRegistro ->

                        lifecycleScope.launch {
                            if (esRegistro) {

                                val existe = withContext(Dispatchers.IO) { usuarioDao.buscarPorEmail(email) }
                                if (existe == null) {
                                    val nuevoUsuario = Usuario(
                                        nombre = nombre, email = email, password = password, esVeterinario = esVeterinario
                                    )
                                    withContext(Dispatchers.IO) { usuarioDao.registrar(nuevoUsuario) }
                                    Toast.makeText(this@MainActivity, "Registro exitoso. ¡Bienvenido!", Toast.LENGTH_SHORT).show()
                                    usuarioLogueado = true
                                } else {
                                    Toast.makeText(this@MainActivity, "Ese correo ya está registrado", Toast.LENGTH_SHORT).show()
                                }
                            } else {

                                val usuario = withContext(Dispatchers.IO) { usuarioDao.login(email, password) }
                                if (usuario != null) {
                                    Toast.makeText(this@MainActivity, "Hola de nuevo, ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                                    usuarioLogueado = true
                                } else {
                                    Toast.makeText(this@MainActivity, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}