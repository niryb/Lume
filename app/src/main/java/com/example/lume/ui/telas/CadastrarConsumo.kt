package com.example.lume.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


data class Consumo(
    val nome: String,
    val dataConsumo: String,
    val genero: String,
    val descricao: String,
    val avaliacao: Float,
    val id: Int? = null // Caso você precise de um identificador único
)

@Composable
fun CadastroConsumoScreen(onSave: (Consumo) -> Unit) {
    var nome by remember { mutableStateOf("") }
    var dataConsumo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var avaliacao by remember { mutableStateOf(0f) }
    var feedbackMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5DEB3))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dataConsumo,
            onValueChange = { dataConsumo = it },
            label = { Text("Data do Consumo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text("Gênero") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Avaliação: ${avaliacao.toInt()} estrelas")
        Slider(
            value = avaliacao,
            onValueChange = { avaliacao = it },
            valueRange = 0f..5f,
            steps = 4,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(
            onClick = {
                if (nome.isBlank() || dataConsumo.isBlank() || genero.isBlank() || descricao.isBlank()) {
                    feedbackMessage = "Todos os campos devem ser preenchidos."
                    return@Button
                }

                val novoConsumo = Consumo(nome, dataConsumo, genero, descricao, avaliacao, null)
                onSave(novoConsumo) // Agora passando corretamente o Consumo criado

                // Limpa os campos após salvar
                nome = ""
                dataConsumo = ""
                genero = ""
                descricao = ""
                avaliacao = 0f
                feedbackMessage = "Consumo salvo com sucesso!"
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Salvar")
        }

        Text(
            text = feedbackMessage,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CadastroConsumoPreview() {
    CadastroConsumoScreen(onSave = {})
}