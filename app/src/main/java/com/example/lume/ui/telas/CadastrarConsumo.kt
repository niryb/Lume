package com.example.lume.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lume.R
import com.example.lume.model.dados.Consumo
import com.example.lume.model.dados.ConsumoDAO
import com.example.lume.ui.theme.DeepBlue

@Composable
fun CadastroConsumoScreen(onSave: () -> Unit) {
    val consumoDAO = remember { ConsumoDAO() } 

    var nome by remember { mutableStateOf("") }
    var dataConsumo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var avaliacao by remember { mutableStateOf("") }
    var comentarioPessoal by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5DEB3))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Cadastro Consumo",
            fontSize = 32.sp,
            color = Color(0xFFDAA520),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        //campo nome
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth())

        //campo data
        OutlinedTextField(
            value = dataConsumo,
            onValueChange = { dataConsumo = it },
            label = { Text("Data do Consumo") },
            modifier = Modifier.fillMaxWidth())

        //campo tipo
        OutlinedTextField(
            value = tipo,
            onValueChange = { tipo = it },
            label = { Text("Tipo") },
            modifier = Modifier.fillMaxWidth())

        //campo genero
        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text("Gênero") },
            modifier = Modifier.fillMaxWidth())

        //campo descrição
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth())

        //campo avaliação
        OutlinedTextField(
            value = avaliacao,
            onValueChange = { input ->
                val num = input.toIntOrNull() 
                if (num != null && num in 0..10) {
                    avaliacao = input 
                }
            },
            label = { Text("Avaliação (0 a 10)") },
            modifier = Modifier.fillMaxWidth()
            )

        //campo comentario pessoal
        OutlinedTextField(
            value = comentarioPessoal,
            onValueChange = { comentarioPessoal = it },
            label = { Text("Comentário pessoal") },
            modifier = Modifier.fillMaxWidth())

       // Text("Avaliação: ${avaliacao.toInt()} estrelas")
        //Slider(value = avaliacao, onValueChange = { avaliacao = it }, valueRange = 0f..5f, steps = 4, modifier = Modifier.padding(vertical = 16.dp))

        //botao de cadastrar
        Button(onClick = {
            if (nome.isBlank() || dataConsumo.isBlank() || tipo.isBlank() || genero.isBlank() || descricao.isBlank() || avaliacao.isBlank() || comentarioPessoal.isBlank()) {
                feedbackMessage = "Todos os campos devem ser preenchidos."
                return@Button
            }

            val novoConsumo = Consumo(
                nome = nome,
                dataConsumo = dataConsumo,
                tipo = tipo,
                genero = genero,
                descricao = descricao,
                avaliacao = avaliacao,
                comentarioPessoal = comentarioPessoal
            )

            consumoDAO.adicionar(novoConsumo) { sucesso ->
                if (sucesso) {
                    feedbackMessage = "Consumo salvo com sucesso!"
                    onSave() // Navega para a próxima tela
                } else {
                    feedbackMessage = "Erro ao salvar. Tente novamente."
                }

            }
        }) {
            Text("Salvar")
        }

        Text(text = feedbackMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
    }
}

