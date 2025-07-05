package com.nbh.juiceapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nbh.juiceapp.data.prefs.AppTheme
import com.nbh.juiceapp.presentation.home.ui.JuiceListScreen
import com.nbh.juiceapp.presentation.theme.ThemeViewModel
import com.nbh.juiceapp.ui.theme.JuiceAppTheme

@Composable
fun JuiceApp(modifier: Modifier = Modifier) {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val currentTheme by themeViewModel.theme.collectAsState()

    val dark = currentTheme == AppTheme.DARK

    JuiceAppTheme(darkTheme = dark) {
        JuiceListScreen(onToggleTheme = themeViewModel::toggleTheme)
    }
}