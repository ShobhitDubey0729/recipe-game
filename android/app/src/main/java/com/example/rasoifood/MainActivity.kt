package com.example.rasoifood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.rasoifood.ui.RasoiRoyaleAppRoot
import com.example.rasoifood.ui.theme.RasoiRoyaleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RasoiRoyaleTheme {
                RasoiRoyaleAppRoot()
            }
        }
    }
}
