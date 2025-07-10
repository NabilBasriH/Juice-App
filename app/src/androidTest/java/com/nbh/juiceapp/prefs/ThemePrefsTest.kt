package com.nbh.juiceapp.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.common.truth.Truth.assertThat
import com.nbh.juiceapp.data.prefs.AppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ThemePrefsTest {

    private lateinit var dataStore: DataStore<Preferences>

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var testFile: File

    @Before
    fun setup() {
        testFile = tempFolder.newFile("theme_prefs.preferences_pb")

        dataStore = PreferenceDataStoreFactory.create(
            produceFile = { testFile }
        )
    }

    @Test
    fun saveAndReadDarkTheme() = runTest {
        dataStore.edit { it[stringPreferencesKey("app_theme")] = "DARK"}

        val result = dataStore.data.first()[stringPreferencesKey("app_theme")]

        assertThat(result).isEqualTo("DARK")
    }

    @Test
    fun readReturnsLightWhenNoValueIsSet() = runTest {
        val theme = dataStore.data.first()[stringPreferencesKey("app_theme")]

        val actual = theme?.let(AppTheme::valueOf) ?: AppTheme.LIGHT

        assertThat(actual).isEqualTo(AppTheme.LIGHT)
    }
}