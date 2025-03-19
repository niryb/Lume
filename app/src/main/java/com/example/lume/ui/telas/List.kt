package com.example.lume.ui.telas

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.lume.R
import com.example.lume.model.dados.Consumo
import com.example.lume.model.dados.ConsumoDAO
import com.example.lume.ui.theme.DeepBlue
import com.example.lume.ui.theme.NightBlack
import com.example.lume.ui.theme.StarYellow
import java.net.URLEncoder

@Composable
fun ListScreen(navController: NavController) {
    val consumoDAO = remember { ConsumoDAO() }
    var consumos by remember { mutableStateOf<List<Consumo>>(emptyList()) }
    var searchText by remember { mutableStateOf("") }
    var consumoParaExcluir by remember { mutableStateOf<Consumo?>(null) }

    LaunchedEffect(Unit) {
        consumoDAO.buscarEmTempoReal { lista ->
            consumos = lista
        }
    }

    val filteredConsumos = consumos.filter { consumo ->
        consumo.nome.contains(searchText, ignoreCase = true) || consumo.genero.contains(
            searchText,
            ignoreCase = true
        ) || consumo.tipo.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = { BarraNavegacaoList(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    "Meu diário de consumo",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                SearchBar(searchText, onSearchTextChanged = { searchText = it })
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(filteredConsumos) { consumo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E1BE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Exibe a capa do filme, se disponível
                        if (!consumo.imagemUri.isNullOrEmpty()) {
                            val uri = Uri.parse(consumo.imagemUri)
                            Image(
                                painter = rememberImagePainter(uri),
                                contentDescription = "Imagem do Consumo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        } else if (!consumo.capaUrl.isNullOrEmpty()) {
                            // Exibe a capa se não houver imagem da galeria
                            Image(
                                painter = rememberImagePainter(consumo.capaUrl),
                                contentDescription = "Capa",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Text(consumo.nome, style = MaterialTheme.typography.titleMedium)
                        Text("Gênero: ${consumo.genero}")
                        Text("Data Consumo: ${consumo.dataConsumo}")
                        Text("Tipo: ${consumo.tipo}")
                        Text("Gênero: ${consumo.genero}")
                        Text("Descrição: ${consumo.descricao}")
                        Text("Avaliação: ${consumo.avaliacao}")
                        Text("Comentário: ${consumo.comentarioPessoal}")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            // botão de editar
                            IconButton(onClick = {
                                navController.navigate("editar/${URLEncoder.encode(consumo.nome, "UTF-8")}")

                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = DeepBlue)
                            }
                            //botao de excluir
                            IconButton(onClick = { consumoParaExcluir = consumo }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Excluir",
                                    tint = DeepBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // exibe o diálogo
    consumoParaExcluir?.let { consumo ->
        AlertDialog(
            onDismissRequest = { consumoParaExcluir = null },
            title = { Text("Excluir consumo") },
            text = { Text("Tem certeza que deseja excluir '${consumo.nome}'? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        consumoDAO.removerPorNome(consumo.nome) { sucesso ->
                            if (sucesso) {
                                consumos = consumos.filter { it.nome != consumo.nome }
                            }
                        }
                        consumoParaExcluir = null
                    }
                ) {
                    Text("Excluir", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { consumoParaExcluir = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNavegacaoList(navController: NavController, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = DeepBlue
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF5E1BE)
        ),
        actions = {
            IconButton(onClick = { navController.navigate("main") }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.bottom_navigation_home),
                    tint = DeepBlue
                )
            }
            /*IconButton(onClick = { *//* Ação para o Perfil *//* }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.bottom_navigation_profile)
                )
            }*/
        },
        modifier = modifier
    )
}

@Composable
fun SearchBar(searchText: String, onSearchTextChanged: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        placeholder = { Text("Pesquisar") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { onSearchTextChanged("") }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(60.dp)
    )
}


