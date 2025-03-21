package com.example.lume.ui.telas

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
//import androidx.media3.common.util.Log
import coil.compose.rememberImagePainter
import com.example.lume.Util.parseDate
import com.example.lume.model.dados.Consumo
import com.example.lume.model.dados.ConsumoDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

import java.io.IOException
import java.net.MalformedURLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroConsumoScreen(onSave: () -> Unit) {
    val consumoDAO = remember { ConsumoDAO() } // Instância do DAO
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var dataConsumo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var avaliacao by remember { mutableStateOf(0f) }
    var comentarioPessoal by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var capaUrl by remember { mutableStateOf<String?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUriToCapture by remember { mutableStateOf<Uri?>(null) }

    val tipos = listOf("Filme", "Série", "Dorama", "Livro", "Artigo", "Podcast", "Outros")
    var isExpanded by remember { mutableStateOf(false) }

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


    val imagePickerResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = uri // Salva a URI da imagem selecionada
            }
        }
    )

    // Launcher para capturar foto usando a câmera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                selectedImageUri = imageUriToCapture
            }
        }
    )

    // Função para criar um arquivo de imagem
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        if (storageDir == null) {
            throw IOException("Não foi possível acessar o diretório de armazenamento.")
        }

        // Cria o arquivo temporário
        return File.createTempFile(
            "JPEG_${timeStamp}_", // Prefixo do nome do arquivo
            ".jpg", // Sufixo do nome do arquivo
            storageDir // Diretório onde o arquivo será salvo
        ).apply {
            // Garante que o arquivo seja excluído após o uso (opcional)
            deleteOnExit()
        }
    }

    // Função para abrir a galeria
    fun openGallery() {
        imagePickerResult.launch("image/*")
    }

    // Função para abrir a câmera
    fun openCamera(context: Context) {
        val file = createImageFile(context)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        imageUriToCapture = uri
        cameraLauncher.launch(uri)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permissão concedida, abra a câmera
            openCamera(context)
        } else {
            // Permissão negada, exiba uma mensagem para o usuário
            Toast.makeText(context, "Permissão da câmera negada", Toast.LENGTH_SHORT).show()
        }
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

        // Campo nome
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo data
        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (dataConsumo.isNotEmpty()) "Data: $dataConsumo" else "Selecionar Data")
        }

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

        // Botões para abrir a galeria ou câmera
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { openGallery() }) {
                Text(text = "Escolher Imagem")
            }
            Button(onClick = { openCamera(context) }) {
                Text(text = "Tirar Foto")
            }
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

        // Campo avaliação
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


        // Botão de cadastrar
        Button(onClick = {
            if (nome.isBlank() || dataConsumo.isBlank() || tipo.isBlank() || genero.isBlank() || descricao.isBlank() || avaliacao > 0 || comentarioPessoal.isBlank()) {
                feedbackMessage = "Todos os campos devem ser preenchidos."
                return@Button
            }

            // Validação da data
            val instantData = parseDate(dataConsumo)
            if (instantData == null) {
                feedbackMessage = "Formato de data inválido."
                return@Button
            }

            val novoConsumo = Consumo(
                nome = nome,
                dataConsumo = dataConsumo,
                tipo = tipo,
                genero = genero,
                descricao = descricao,
                avaliacao = avaliacao.toInt().toString(),
                comentarioPessoal = comentarioPessoal,
                capaUrl = capaUrl,
                imagemUri = selectedImageUri?.toString()
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

            android.util.Log.d("API_FILME", "Tentando conectar à API: ${url.toString()}")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                android.util.Log.d("API_FILME", "Conexão bem-sucedida. Código de resposta: ${connection.responseCode}")

                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                inputStream.close()

                android.util.Log.d("API_FILME", "Resposta da API: $response")

                // Extrai os dados do JSON
                val jsonResponse = JSONObject(response)
                val results = jsonResponse.getJSONArray("results")
                if (results.length() > 0) {
                    val primeiroFilme = results.getJSONObject(0)
                    val titulo = primeiroFilme.getString("title")
                    val descricao = primeiroFilme.getString("overview")
                    val capaUrl = primeiroFilme.getString("poster_path")

                    android.util.Log.d("API_FILME", "Dados do filme encontrado: Título = $titulo, Descrição = $descricao, Capa = $capaUrl")

                    // Retorna os dados como um Map
                    mapOf(
                        "titulo" to titulo,
                        "descricao" to descricao,
                        "capaUrl" to "https://image.tmdb.org/t/p/w500$capaUrl"
                    )
                } else {
                    android.util.Log.d("API_FILME", "Nenhum filme encontrado para o termo: $nomeFilme")
                    null
                }
            } else {
                android.util.Log.e("API_FILME", "Erro na conexão. Código de resposta: ${connection.responseCode}")
                null
            }
        } catch (e: MalformedURLException) {
            android.util.Log.e("API_FILME", "URL malformada: ${e.message}")
            null
        } catch (e: IOException) {
            android.util.Log.e("API_FILME", "Erro de I/O: ${e.message}")
            null
        } catch (e: JSONException) {
            android.util.Log.e("API_FILME", "Erro ao processar JSON: ${e.message}")
            null
        } catch (e: Exception) {
            android.util.Log.e("API_FILME", "Erro inesperado: ${e.message}")
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