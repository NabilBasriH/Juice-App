package com.nbh.juiceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nbh.juiceapp.presentation.JuiceApp
import com.nbh.juiceapp.presentation.home.ui.viewmodel.JuiceListViewModel
import com.nbh.juiceapp.ui.theme.JuiceAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val juiceListViewModel: JuiceListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            juiceListViewModel.isLoading.value
        }

        enableEdgeToEdge()
        setContent {
            JuiceAppTheme {
                JuiceApp()
            }
        }
    }
}