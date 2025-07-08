package com.nbh.juiceapp.data.prefs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemePreferences @Inject constructor(
    @ApplicationContext private val context: Context
){
    fun read(): Flow<AppTheme> = ThemePrefs.read(context)
    suspend fun save(theme: AppTheme) = ThemePrefs.save(context, theme)
}