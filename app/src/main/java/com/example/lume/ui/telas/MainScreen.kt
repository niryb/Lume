package com.example.lume.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lume.R
import com.example.lume.ui.theme.NightBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToList: () -> Unit,
    onNavigateToConsumption: () -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = { BarraNavegacao() },
        bottomBar = { Footer() },
        containerColor = Color(0xFFF5E1BE)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Topo()
            /*Spacer(modifier = Modifier.height(16.dp))
            SearchBar()*/
            Spacer(modifier = Modifier.height(24.dp))
            RegisterButton(navController)
            Spacer(modifier = Modifier.height(16.dp))
            ListConsumptionButton(navController)
        }
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

/*@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text("Pesquisar por nome, categoria...") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
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
}*/

@Composable
fun RegisterButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("consumption") },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F1A2C)),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(80.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color(0xFFFFD700))
        Spacer(modifier = Modifier.width(30.dp))
        Column {
            Text("Registrar novo consumo", color = Color.White, fontSize = 18.sp)
            Text("Filmes, séries, livros, podcasts...", color = Color(0xFFFFD700), fontSize = 12.sp)
        }
    }
}

@Composable
fun ListConsumptionButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("list") },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F1A2C)),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(80.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(imageVector = Icons.Default.List, contentDescription = "List", tint = Color(0xFFFFD700))
        Spacer(modifier = Modifier.width(30.dp))
        Column {
            Text("Meu diário de consumo", color = Color.White, fontSize = 18.sp)
            Text("Veja seus consumos registrados", color = Color(0xFFFFD700), fontSize = 12.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNavegacao(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text (text = stringResource(R.string.app_name), color = NightBlack) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF5E1BE)
        ),
        actions = {
            /*IconButton(onClick = { *//* Ação para a Home *//* }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.bottom_navigation_home)
                )
            }*/
            IconButton(onClick = { /* Ação para o Perfil */ }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.bottom_navigation_profile),
                    tint = NightBlack
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
    val navController = rememberNavController()
    MainScreen(
        onNavigateToList = { /* Navegação de exemplo */ },
        onNavigateToConsumption = { /* Navegação de exemplo */ },
        navController = navController
    )
}
