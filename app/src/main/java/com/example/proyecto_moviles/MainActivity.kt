package com.example.proyecto_moviles

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_moviles.model.Usuario
import com.example.proyecto_moviles.view.PantallaDashboard
import com.example.proyecto_moviles.view.PantallaDashboardVeterinario
import com.example.proyecto_moviles.view.PantallaLogin
import com.example.proyecto_moviles.viewmodel.MascotaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = (application as GatocoApp).database
        val usuarioDao = database.usuarioDao()
        val mascotaDao = database.mascotaDao()
        val citaDao = database.citaDao()

        setContent {
            var usuarioLogueado by remember { mutableStateOf(false) }
            var nombreUsuario by remember { mutableStateOf("") }
            var esVeterinarioLogueado by remember { mutableStateOf(false) }

            if (usuarioLogueado) {

                if (esVeterinarioLogueado) {
                    val viewModelVet: MascotaViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return MascotaViewModel(mascotaDao, citaDao, usuarioDao, nombreUsuario) as T
                            }
                        }
                    )

                    PantallaDashboardVeterinario(
                        nombreUsuario = nombreUsuario,
                        viewModel = viewModelVet,
                        onCerrarSesion = {
                            usuarioLogueado = false
                            nombreUsuario = ""
                            esVeterinarioLogueado = false
                        }
                    )

                } else {
                    val viewModelCliente: MascotaViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return MascotaViewModel(mascotaDao, citaDao, usuarioDao, null) as T
                            }
                        }
                    )

                    PantallaDashboard(
                        nombreUsuario = nombreUsuario,
                        viewModel = viewModelCliente,
                        onCerrarSesion = {
                            usuarioLogueado = false
                            nombreUsuario = ""
                            esVeterinarioLogueado = false
                        },
                        esVeterinario = false
                    )
                }

            } else {
                PantallaLogin(
                    onLoginSuccess = { email, password, nombre, esVeterinario, esRegistro ->
                        kotlinx.coroutines.GlobalScope.launch(Dispatchers.Main) {
                            if (esRegistro) {
                                val existe = withContext(Dispatchers.IO) { usuarioDao.buscarPorEmail(email) }
                                if (existe == null) {
                                    val nuevoUsuario = Usuario(email, nombre, password, esVeterinario)
                                    withContext(Dispatchers.IO) { usuarioDao.registrar(nuevoUsuario) }
                                    Toast.makeText(this@MainActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()

                                    nombreUsuario = nombre
                                    esVeterinarioLogueado = esVeterinario
                                    usuarioLogueado = true
                                } else {
                                    Toast.makeText(this@MainActivity, "Correo ya registrado", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val usuario = withContext(Dispatchers.IO) { usuarioDao.login(email, password) }
                                if (usuario != null) {
                                    Toast.makeText(this@MainActivity, "Hola ${usuario.nombre}", Toast.LENGTH_SHORT).show()

                                    nombreUsuario = usuario.nombre
                                    esVeterinarioLogueado = usuario.esVeterinario
                                    usuarioLogueado = true
                                } else {
                                    Toast.makeText(this@MainActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}