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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
//import com.example.lume.ui.theme.DeepBlue
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun EditarConsumoScreen(navController: NavController, consumoNome: String?) {
    val consumoDAO = remember { ConsumoDAO() }
    var consumo by remember { mutableStateOf<Consumo?>(null) }

    var nome by remember { mutableStateOf("") }
    var dataConsumo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var avaliacao by remember { mutableStateOf(0f) } // Alterado para Float
    var comentarioPessoal by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    // Lógica para o DatePickerDialog
    val context = LocalContext.current
    val now = Clock.System.now()
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val dayStr = dayOfMonth.toString().padStart(2, '0')
            val monthStr = (month + 1).toString().padStart(2, '0')
            dataConsumo = "$dayStr/$monthStr/$year"
        },
        localDateTime.year,
        localDateTime.monthNumber - 1,
        localDateTime.dayOfMonth
    )

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
                    avaliacao = consumo?.avaliacao?.toFloatOrNull() ?: 0f // Convertendo para Float
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
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
            // Campo nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo data (substituído por um botão que abre o DatePickerDialog)
            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (dataConsumo.isNotEmpty()) "Data: $dataConsumo" else "Selecionar Data")
            }

            // Campo tipo
            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo gênero
            OutlinedTextField(
                value = genero,
                onValueChange = { genero = it },
                label = { Text("Gênero") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo descrição
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo avaliação (Slider)
            Text(
                text = "Avaliação: ${avaliacao.toInt()}",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
            Slider(
                value = avaliacao,
                onValueChange = { avaliacao = it },
                valueRange = 0f..10f,
                steps = 9, // Passos de 1 em 1 (0 a 10)
                modifier = Modifier.fillMaxWidth()
            )

            // Campo comentário pessoal
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
                        "Tentando salvar consumo: nome=$nome, data=$dataConsumo, tipo=$tipo, genero=$genero, descricao=$descricao, avaliacao=${avaliacao.toInt()}"
                    )

                    if (nome.isNotBlank() && dataConsumo.isNotBlank() && tipo.isNotBlank() && genero.isNotBlank() && descricao.isNotBlank() && avaliacao > 0 && comentarioPessoal.isNotBlank()) {
                        val consumoAtualizado = consumo!!.copy(
                            nome = nome,
                            dataConsumo = dataConsumo,
                            tipo = tipo,
                            genero = genero,
                            descricao = descricao,
                            avaliacao = avaliacao.toInt().toString(), // Convertendo para String
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