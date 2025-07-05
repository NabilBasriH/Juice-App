package com.nbh.juiceapp.presentation.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbh.juiceapp.data.prefs.AppTheme
import com.nbh.juiceapp.data.prefs.ThemePrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _theme = MutableStateFlow(AppTheme.LIGHT)
    val theme: StateFlow<AppTheme> = _theme.asStateFlow()

    init {
        viewModelScope.launch {
            ThemePrefs.read(context).collect { _theme.value = it }
        }
    }

    fun toggleTheme() {
        val next = if (_theme.value == AppTheme.LIGHT) AppTheme.DARK else AppTheme.LIGHT
        viewModelScope.launch { ThemePrefs.save(context, next) }
    }
}