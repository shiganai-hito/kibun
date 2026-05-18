package com.example.kibun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import com.example.kibun.ui.theme.KibunAppTheme
import com.example.kibun.ui.viewmodel.KibunViewModel
import com.example.kibun.ui.viewmodel.KibunViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: KibunViewModel by viewModels {
        val app = application as KibunApplication
        KibunViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KibunAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KibunApp(viewModel)
                }
            }
        }
    }
}
