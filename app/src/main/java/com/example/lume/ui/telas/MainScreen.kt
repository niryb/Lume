package com.example.lume.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lume.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E1BE)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BarraNavegacao()
        Topo()
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(24.dp))
        RegisterButton()
        Spacer(modifier = Modifier.weight(1f))
        Footer()
    }
}

@Composable
fun Topo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F1A2C))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Seu diário de consumo cultural, iluminando suas experiências.",
            color = Color(0xFFFFD700),
            fontSize = 16.sp
        )
    }
}

@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text("Pesquisar por nome, categoria...") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search,
                contentDescription = "Search")
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { searchText = "" }) {
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

@Composable
fun RegisterButton() {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F1A2C)),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(80.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Registrar novo consumo", color = Color.White, fontSize = 18.sp)
                Text("Filmes, séries, livros, podcasts...", color = Color(0xFFFFD700), fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNavegacao(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        actions = {
            IconButton(onClick = { /* Ação para a Home */ }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.bottom_navigation_home)
                )
            }
            IconButton(onClick = { /* Ação para o Perfil */ }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.bottom_navigation_profile)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun Footer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F1A2C))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "© 2025 Lume - Todos os direitos reservados",
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    HomeScreen()
    Topo()
    SearchBar()
    BarraNavegacao()

}
