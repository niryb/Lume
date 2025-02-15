package com.example.lume.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lume.R

@Composable
fun HomeScreen(onNavigateToLogin: () -> Unit, onNavigateToRegister: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Imagem de fundo
        Image(
            painter = painterResource(id = R.drawable.tela_logo),
            contentDescription = "Fundo da tela principal",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f)) // Empurra os botões para baixo

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onNavigateToLogin,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F223B), // Fundo azul-escuro
                        contentColor = Color(0xFFB89130)    // Texto dourado
                    ),
                    border = BorderStroke(2.dp, Color(0xFFB89130)) // Borda dourada
                ) {
                    Text("Entrar")
                }

                Button(
                    onClick = onNavigateToRegister,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F223B), // Fundo azul-escuro
                        contentColor = Color(0xFFB89130)    // Texto dourado
                    ),
                    border = BorderStroke(2.dp, Color(0xFFB89130)) // Borda dourada
                ) {
                    Text("Cadastrar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen({}, {})
}
