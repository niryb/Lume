package com.example.lume.ui.telas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.lume.R
import com.example.lume.model.dados.Consumo
import com.example.lume.model.dados.ConsumoDAO
import com.example.lume.ui.theme.DeepBlue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroConsumoScreen(onSave: () -> Unit) {
    val consumoDAO = remember { ConsumoDAO() } // Instância do DAO
    val scope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var dataConsumo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var avaliacao by remember { mutableStateOf("") }
    var comentarioPessoal by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var capaUrl by remember { mutableStateOf<String?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


    val tipos = listOf("Filme", "Série", "Dorama", "Livro", "Outros")
    var isExpanded by remember { mutableStateOf(false) }

    val imagePickerResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = uri // Salva a URI da imagem selecionada
            }
        }
    )

    // Função para abrir a galeria
    fun openGallery() {
        imagePickerResult.launch("image/*")
    }



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

        // Campo tipo (menu suspenso)
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it }
        ) {
            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                tipos.forEach { tipoItem ->
                    DropdownMenuItem(
                        text = { Text(tipoItem) },
                        onClick = {
                            tipo = tipoItem
                            isExpanded = false

                            // Busca os dados automaticamente com base no tipo selecionado
                            if (nome.isNotBlank()) {
                                scope.launch {
                                    when (tipo) {
                                        "Filme", "Série" -> {
                                            val resultado = buscarFilme(nome)
                                            if (resultado != null) {
                                                nome = resultado["titulo"] ?: nome
                                                descricao = resultado["descricao"] ?: "Descrição não encontrada"
                                                capaUrl = resultado["capaUrl"]
                                            }
                                        }
                                        "Livro" -> {
                                            val resultado = buscarLivro(nome)
                                            if (resultado != null) {
                                                nome = resultado["titulo"] ?: nome
                                                descricao = resultado["descricao"] ?: "Descrição não encontrada"
                                                capaUrl = resultado["capaUrl"]
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
        // Botão para abrir a galeria e selecionar uma foto
        Button(onClick = { openGallery() }) {
            Text(text = "Escolher Imagem")
        }
        selectedImageUri?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = "Imagem da Galeria",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } ?: capaUrl?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = "Capa do Filme",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

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
                val num = input.toIntOrNull() // Tenta converter para número
                if (num != null && num in 0..10) {
                    avaliacao = input // Aceita apenas se estiver no intervalo 0-10
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
                comentarioPessoal = comentarioPessoal,
                capaUrl = capaUrl,
                imagemUri = selectedImageUri.toString()
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

suspend fun buscarFilme(nomeFilme: String): Map<String, String>? {
    return withContext(Dispatchers.IO) {
        try {
            val apiKey = "90f38b2a055f8b03fe62500b4ca55807"
            val url = URL("https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$nomeFilme&language=pt-BR")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                inputStream.close()

                // Extrai os dados do JSON
                val jsonResponse = JSONObject(response)
                val results = jsonResponse.getJSONArray("results")
                if (results.length() > 0) {
                    val primeiroFilme = results.getJSONObject(0)
                    val titulo = primeiroFilme.getString("title")
                    val descricao = primeiroFilme.getString("overview")
                    val capaUrl = primeiroFilme.getString("poster_path")

                    // Retorna os dados como um Map
                    mapOf(
                        "titulo" to titulo,
                        "descricao" to descricao,
                        "capaUrl" to "https://image.tmdb.org/t/p/w500$capaUrl"
                    )
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

suspend fun buscarLivro(nomeLivro: String): Map<String, String>? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL("https://openlibrary.org/search.json?q=$nomeLivro")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                inputStream.close()

                // Extrai os dados do JSON
                val jsonResponse = JSONObject(response)
                val docs = jsonResponse.getJSONArray("docs")

                if (docs.length() > 0) {
                    val primeiroLivro = docs.getJSONObject(0)
                    val titulo = primeiroLivro.getString("title")

                    // Verifique se os campos opcionais existem
                    val autor = primeiroLivro.optJSONArray("author_name")?.getString(0) ?: "Autor desconhecido"
                    val descricao = primeiroLivro.optString("first_sentence", "Descrição não disponível")
                    val coverId = primeiroLivro.optString("cover_i", null)

                    // Gera a URL da capa, se existir
                    val capaUrl = coverId?.let {
                        "https://covers.openlibrary.org/b/id/$it-M.jpg"
                    }

                    // Retorna os dados como um Map
                    return@withContext mapOf(
                        "titulo" to titulo,
                        "autor" to autor,
                        "descricao" to descricao,
                        "capaUrl" to capaUrl.orEmpty() // Use orEmpty() caso capaUrl seja null
                    )
                } else {
                    // Retorna null caso não haja resultado
                    return@withContext null
                }
            } else {
                // Retorna null se a resposta não for OK
                return@withContext null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Retorna null em caso de exceção
            return@withContext null
        }
    }
}