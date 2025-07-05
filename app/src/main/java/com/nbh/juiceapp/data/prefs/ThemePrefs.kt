package com.nbh.juiceapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("ui_prefs")
private val THEME_KEY = stringPreferencesKey("app_theme")

enum class AppTheme {
    LIGHT, DARK
}

object ThemePrefs {

    suspend fun save(context: Context, theme: AppTheme) {
        context.dataStore.edit { it[THEME_KEY] = theme.name }
    }

    fun read(context: Context): Flow<AppTheme> =
        context.dataStore.data.map { it[THEME_KEY]?.let(AppTheme::valueOf) ?: AppTheme.LIGHT }
}