package com.example.lume.ui.telas

import ListScreen
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var consumos by remember { mutableStateOf(listOf<Consumo>()) } // Estado compartilhado da lista de consumos

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("main") },
                onRegisterClicked = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(onRegisterSuccess = { navController.navigate("main") })
        }
        composable("main") {
            MainScreen(
                onNavigateToConsumption = { navController.navigate("consumption") },
                onNavigateToList = { navController.navigate("list") },
                navController = navController
            )
        }
        composable("consumption") {
            CadastroConsumoScreen(
                onSave = { novoConsumo ->
                    consumos = consumos + novoConsumo
                    navController.navigate("list") // Redireciona para a lista após salvar
                }
            )
        }
        composable("list") {
            ListScreen(consumos)
        }
    }
}
