package com.nbh.juiceapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nbh.juiceapp.presentation.JuiceApp
import com.nbh.juiceapp.presentation.home.ui.viewmodel.JuiceListViewModel
import com.nbh.juiceapp.presentation.splash.SplashScreen
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
                val isLoading by juiceListViewModel.isLoading.collectAsState()

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    if (isLoading) {
                        SplashScreen()
                    } else {
                        JuiceApp()
                    }
                } else {
                    JuiceApp()
                }
            }
        }
    }
}