package com.example.lume.ui.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.lume.R

@Composable
fun MainScreen() {

        // Conteúdo principal sobre a imagem
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Tela Principal", style = MaterialTheme.typography.headlineMedium)
        }
    }


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
