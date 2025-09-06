package com.techmania.maargdarshak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.techmania.maargdarshak.ui.theme.MaargDarshakTheme
import com.techmania.maargdarshak.users.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // <-- 1. Add this annotation for Hilt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaargDarshakTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 2. Call your NavGraph composable here
                    NavGraph()
                }
            }
        }
    }
}
