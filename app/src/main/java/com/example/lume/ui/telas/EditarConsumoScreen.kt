import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lume.model.dados.Consumo
import com.example.lume.model.dados.ConsumoDAO
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.lume.ui.theme.DeepBlue

@Composable
fun EditarConsumoScreen(navController: NavController, consumoNome: String?) {
    val consumoDAO = remember { ConsumoDAO() }
    var consumo by remember { mutableStateOf<Consumo?>(null) }
    var nome by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    // Carregar o consumo a ser editado
    LaunchedEffect(consumoNome) {
        if (!consumoNome.isNullOrEmpty()) {
            consumoDAO.buscarPorNome(consumoNome) { consumoEncontrado ->
                consumo = consumoEncontrado
                nome = consumo?.nome ?: ""
                tipo = consumo?.tipo ?: ""
                descricao = consumo?.descricao ?: ""
            }
        }
    }

    // Caso o consumo ainda não tenha sido carregado
    consumo?.let {
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
                text = "Editar Consumo",
                fontSize = 32.sp,
                color = Color(0xFFDAA520),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campos para editar
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exibe a mensagem de feedback (sucesso ou erro)
            if (feedbackMessage.isNotEmpty()) {
                Text(
                    text = feedbackMessage,
                    color = if (feedbackMessage.contains("sucesso")) Color.Green else Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botão de salvar alterações
            Button(
                onClick = {
                    if (nome.isNotBlank() && tipo.isNotBlank() && descricao.isNotBlank()) {
                        val consumoAtualizado = it.copy(nome = nome, tipo = tipo, descricao = descricao)
                        consumoDAO.atualizarPorNome(consumoAtualizado) { sucesso ->
                            feedbackMessage = if (sucesso) {
                                navController.popBackStack() // Voltar para a tela anterior
                                "Consumo salvo com sucesso!"
                            } else {
                                "Erro ao salvar. Tente novamente."
                            }
                        }
                    } else {
                        feedbackMessage = "Todos os campos devem ser preenchidos!"
                    }
                }
            ) {
                Text("Salvar Alterações")
            }
        }
    } ?: run {
        Text("Carregando ou consumo não encontrado...", modifier = Modifier.padding(16.dp))
    }
}