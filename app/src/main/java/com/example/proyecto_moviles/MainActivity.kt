package com.example.proyecto_moviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.proyecto_moviles.ui.theme.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto_MovilesTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LandingScreen()
                }
            }
        }
    }
}

@Composable
fun LandingScreen() {
    ConstraintLayout(modifier = Modifier.fillMaxSize().background(Color.White)) {

        val (blueBg, title, iconBar, loginBtn, registerBtn, imageCard) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(180.dp) // Define un ancho fijo para la franja azul
                .background(AppBlue)
                .constrainAs(blueBg) {
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = "CUIDAMOS A TUS MEJORES AMIGOS CON AMOR Y PROFESIONALISMO",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .width(230.dp) // Limita el ancho para que el texto se divida en varias líneas
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 64.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                }
        )

        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 24.dp)
                .background(AppLightGray, shape = RoundedCornerShape(16.dp))
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .constrainAs(iconBar) {
                    top.linkTo(title.bottom)
                    start.linkTo(title.start)
                }
        ) {
            val iconModifier = Modifier.size(32.dp)
            Icon(painterResource(id = R.drawable.ic_home), contentDescription = "Home", tint = AppIconTint, modifier = iconModifier)
            Spacer(modifier = Modifier.height(24.dp))
            Icon(painterResource(id = R.drawable.ic_add), contentDescription = "Add", tint = AppIconTint, modifier = iconModifier)
            Spacer(modifier = Modifier.height(24.dp))
            Icon(painterResource(id = R.drawable.ic_person_add), contentDescription = "Person add", tint = AppIconTint, modifier = iconModifier)
            Spacer(modifier = Modifier.height(24.dp))
            Icon(painterResource(id = R.drawable.ic_add_call), contentDescription = "Add call", tint = AppIconTint, modifier = iconModifier)
        }

        Button(
            onClick = { /* TODO: Acción para iniciar sesión */ },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppBlue),
            modifier = Modifier
                .constrainAs(loginBtn) {
                    centerVerticallyTo(iconBar) // Centra verticalmente con la barra de iconos
                    start.linkTo(iconBar.end, margin = 16.dp)
                }
        ) {
            Text("Iniciar sesión", color = Color.White)
        }

        Button(
            onClick = { /* TODO: Acción para registrarse */ },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppGreen),
            modifier = Modifier
                .constrainAs(registerBtn) {
                    centerVerticallyTo(loginBtn) // Centra verticalmente con el otro botón
                    start.linkTo(blueBg.start, margin = 16.dp)
                }
        ) {
            Text("Registrarse", color = Color.White)
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.1f)
                .padding(horizontal = 32.dp)
                .constrainAs(imageCard) {
                    top.linkTo(iconBar.bottom, margin = 32.dp)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.dog_photo),
                contentDescription = "Perro recibiendo un premio",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun LandingScreenPreview() {
    Proyecto_MovilesTheme {
        LandingScreen()
    }
}