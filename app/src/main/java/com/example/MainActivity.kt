package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private lateinit var mainViewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    enableEdgeToEdge()
    setContent {
      val darkTheme = mainViewModel.darkTheme.collectAsState().value
      MyApplicationTheme(darkTheme = darkTheme) {
        MainScreen(viewModel = mainViewModel)
      }
    }
  }

  override fun onResume() {
    super.onResume()
    if (::mainViewModel.isInitialized) {
      mainViewModel.refreshOnAppOpen()
    }
  }
}
