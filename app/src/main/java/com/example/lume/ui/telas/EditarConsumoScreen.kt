import android.R.attr.data
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
import android.util.Log
import com.example.lume.ui.theme.DeepBlue

@Composable
fun EditarConsumoScreen(navController: NavController, consumoNome: String?) {
    val consumoDAO = remember { ConsumoDAO() }
    var consumo by remember { mutableStateOf<Consumo?>(null) }

    var nome by remember { mutableStateOf("") }
    var dataConsumo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var avaliacao by remember { mutableStateOf("") }
    var comentarioPessoal by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    // Carregar o consumo a ser editado
    LaunchedEffect(consumoNome) {
        if (!consumoNome.isNullOrEmpty()) {
            Log.d("EditarConsumo", "Buscando consumo com nome: $consumoNome")
            consumoDAO.buscarPorNome(consumoNome) { consumoEncontrado ->
                if (consumoEncontrado != null) {
                    Log.d("EditarConsumo", "Consumo encontrado: $consumoEncontrado")
                    consumo = consumoEncontrado
                    nome = consumo?.nome ?: ""
                    dataConsumo = consumo?.dataConsumo ?: ""
                    tipo = consumo?.tipo ?: ""
                    genero = consumo?.genero ?: ""
                    descricao = consumo?.descricao ?: ""
                    avaliacao = consumo?.avaliacao ?: ""
                    comentarioPessoal = consumo?.comentarioPessoal ?: ""
                } else {
                    Log.e("EditarConsumo", "Consumo não encontrado para o nome: $consumoNome")
                }
            }
        }
    }


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
            //campo nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            //campo data
            OutlinedTextField(
                value = dataConsumo,
                onValueChange = { dataConsumo = it },
                label = { Text("Data do Consumo") },
                modifier = Modifier.fillMaxWidth()
            )

            //campo tipo
            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo") },
                modifier = Modifier.fillMaxWidth()
            )

            //campo genero
            OutlinedTextField(
                value = genero,
                onValueChange = { genero = it },
                label = { Text("Gênero") },
                modifier = Modifier.fillMaxWidth()
            )

            //campo descrição
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            //campo avaliação
            OutlinedTextField(
                value = avaliacao,
                onValueChange = { avaliacao = it },
                label = { Text("Avaliação") },
                modifier = Modifier.fillMaxWidth()
            )

            //campo comentario pessoal
            OutlinedTextField(
                value = comentarioPessoal,
                onValueChange = { comentarioPessoal = it },
                label = { Text("Comentário pessoal") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


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
                    Log.d(
                        "EditarConsumo",
                        "Tentando salvar consumo: nome=$nome, data=$dataConsumo, tipo=$tipo, genero=$genero, descricao=$descricao, avaliacao=$avaliacao"
                    )

                    if (nome.isNotBlank() && dataConsumo.isNotBlank() && tipo.isNotBlank() && genero.isNotBlank() && descricao.isNotBlank() && avaliacao.isNotBlank() && comentarioPessoal.isNotBlank()) {
                        val consumoAtualizado = consumo!!.copy(
                            nome = nome,
                            dataConsumo = dataConsumo,
                            tipo = tipo,
                            genero = genero,
                            descricao = descricao,
                            avaliacao = avaliacao,
                            comentarioPessoal = comentarioPessoal
                        )
                        Log.d("EditarConsumo", "Objeto consumo atualizado: $consumoAtualizado")

                        consumoDAO.atualizarPorNome(consumoAtualizado) { sucesso ->
                            if (sucesso) {
                                Log.d(
                                    "EditarConsumo",
                                    "Consumo atualizado com sucesso no banco de dados."
                                )
                                feedbackMessage = "Consumo salvo com sucesso!"
                                navController.popBackStack() // Voltar para a tela anterior
                            } else {
                                Log.e(
                                    "EditarConsumo",
                                    "Erro ao atualizar consumo no banco de dados."
                                )
                                feedbackMessage = "Erro ao salvar. Tente novamente."
                            }
                        }
                    } else {
                        Log.e("EditarConsumo", "Campos obrigatórios estão vazios.")
                        feedbackMessage = "Todos os campos devem ser preenchidos!"
                    }
                }
            ) {
                Text("Salvar Alterações")
            }
        }

    }
}




