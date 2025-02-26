package com.example.lume.ui.telas

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lume.model.dados.Consumo
import com.example.lume.model.dados.ConsumoDAO

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

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
                onSave = { navController.navigate("list") } // Navega para a lista após salvar
            )
        }
        composable("list") {
            ListScreen(navController = navController)
        }
    }
}
/*
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val consumoDAO = remember { ConsumoDAO() }

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
        */
/*composable("consumption") {
            CadastroConsumoScreen(
                onSave = { novoConsumo ->
                    consumoDAO.adicionar(novoConsumo) { sucesso ->
                        if (sucesso) {
                            navController.navigate("list") // Navega para a lista após salvar
                        }
                    }
                }
            )
        }*//*

        composable("consumption") {
            CadastroConsumoScreen { novoConsumo ->
                consumoDAO.adicionar(novoConsumo) { sucesso ->
                    if (sucesso) {
                        navController.navigate("list") // Navega para a lista após salvar
                    }
                }
            }
        }
        composable("list") {
            ListScreen()
        }
    }
}*/
