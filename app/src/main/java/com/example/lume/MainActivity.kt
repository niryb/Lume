package com.example.lume

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.lume.ui.telas.AppNavigation
import com.example.lume.ui.telas.AppNavigation
import com.example.lume.ui.theme.LumeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LumeTheme {
                AppNavigation()
            }
        }
    }
}
