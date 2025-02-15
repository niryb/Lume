package com.example.lume.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var newUsername by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    // Simulação de um banco de dados local de usuários
    val registeredUsers = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5DEB3)) // Fundo bege
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Cadastro",
            fontSize = 32.sp,
            color = Color(0xFFDAA520),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo para novo usuário
        OutlinedTextField(
            value = newUsername,
            onValueChange = { newUsername = it },
            label = { Text("Novo usuário") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para nova senha
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para confirmar a senha
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar senha") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para cadastrar
        Button(
            onClick = {
                if (newUsername.isBlank()) {
                    feedbackMessage = "Por favor, insira um nome de usuário."
                    return@Button
                }
                if (newPassword.isBlank()) {
                    feedbackMessage = "Por favor, insira uma senha."
                    return@Button
                }
                if (newPassword != confirmPassword) {
                    feedbackMessage = "As senhas não coincidem."
                    return@Button
                }
                if (registeredUsers.contains(newUsername)) {
                    feedbackMessage = "Usuário já existe!"
                } else {
                    registeredUsers.add(newUsername)
                    feedbackMessage = "Usuário cadastrado com sucesso!"
                    onRegisterSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC89F5D)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Text(text = "Cadastrar", color = Color.White, fontSize = 18.sp)
        }

        // Feedback do cadastro
        Text(
            text = feedbackMessage,
            color = if (feedbackMessage.contains("sucesso")) Color.Green else Color.Red,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onRegisterSuccess = {})
}
