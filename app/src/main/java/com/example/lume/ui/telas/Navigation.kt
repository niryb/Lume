package com.example.lume.ui.telas


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lume.ui.telas.LoginScreen

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
            LoginScreen(onLoginSuccess = { navController.navigate("main") },
                onRegisterClicked = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(onRegisterSuccess = { navController.navigate("main") })
        }
        composable("main") {
            MainScreen()
        }
    }
}
