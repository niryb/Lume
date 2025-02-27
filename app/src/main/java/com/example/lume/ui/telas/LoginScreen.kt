package com.example.lume.ui.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lume.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    // Simulação de credenciais válidas
    val validUser = "admin"
    val validPassword = "1234"



    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5DEB3)) // Fundo bege
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagem da Logo
        Image(
            painter = painterResource(id = R.drawable.logo_remove),
            contentDescription = "Logo Lume",
            modifier = Modifier
                .size(300.dp)
                //.clip(CircleShape)
                //.background(Color.White)
                .padding(bottom = 5.dp)
        )

        // campo nome de usuário
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nome de usuário") },
            modifier = Modifier
                .fillMaxWidth()
                .size(57.dp)
                .background(Color.White),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // campo senha
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .size(57.dp)
                .background(Color.White),
           singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // botão "esqueceu a senha?"
        TextButton(onClick = { /* Implementar recuperação de senha, se necessário */ }) {
            Text("Esqueceu a senha?", color = Color(0xFF6D4C41))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // botão de login
        Button(
            onClick = {
                if (username == validUser && password == validPassword) {
                    feedbackMessage = "Login bem-sucedido!"
                    onLoginSuccess()
                } else {
                    feedbackMessage = "Usuário ou senha incorretos!"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC89F5D)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Text(text = "Login", color = Color.White, fontSize = 18.sp)
        }


        Text(
            text = feedbackMessage,
            color = if (feedbackMessage == "Login bem-sucedido!") Color.Green else Color.Red,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para ir para o cadastro
        TextButton(onClick = onRegisterClicked) {
            Text("Ainda não tem conta? Cadastre-se", color = Color(0xFF6D4C41))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginSuccess = {},
        onRegisterClicked = {}
    )
}
