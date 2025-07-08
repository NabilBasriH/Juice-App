package com.nbh.juiceapp.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.common.truth.Truth.assertThat
import com.nbh.juiceapp.data.prefs.AppTheme
import com.nbh.juiceapp.data.prefs.ThemePrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ThemePrefsTest {

    private lateinit var dataStore: DataStore<Preferences>

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val tempFolder = TemporaryFolder()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val testFile = tempFolder.newFile("theme_prefs.preferences_pb")

        dataStore = PreferenceDataStoreFactory.create(
            produceFile = { testFile }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save and read dark theme`() = runTest {
        dataStore.edit { it[stringPreferencesKey("app_theme")] = "DARK"}

        val result = dataStore.data.first()[stringPreferencesKey("app_theme")]

        assertThat(result).isEqualTo("DARK")
    }

    @Test
    fun `read returns light when no value is set`() = runTest {
        val theme = dataStore.data.first()[stringPreferencesKey("app_theme")]

        val actual = theme?.let(AppTheme::valueOf) ?: AppTheme.LIGHT

        assertThat(actual).isEqualTo(AppTheme.LIGHT)
    }
}