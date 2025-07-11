package com.nbh.juiceapp.presentation.theme

import com.google.common.truth.Truth.assertThat
import com.nbh.juiceapp.data.prefs.AppTheme
import com.nbh.juiceapp.data.prefs.ThemePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var themeViewModel: ThemeViewModel
    private val fakePrefs: ThemePreferences = mock()

    private val themeFlow = MutableStateFlow(AppTheme.LIGHT)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(fakePrefs.read()).thenReturn(themeFlow)
        themeViewModel = ThemeViewModel(fakePrefs)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial theme is LIGHT when loaded from prefs`() {
        assertThat(themeViewModel.theme.value).isEqualTo(AppTheme.LIGHT)
    }

    @Test
    fun `when toggleTheme is called, opposite theme is saved`() = runTest {
        themeViewModel.toggleTheme()

        verify(fakePrefs).save(AppTheme.DARK)
    }

    @Test
    fun `toggleTheme toggles theme correctly`() = runTest {
        themeViewModel.toggleTheme()
        themeFlow.emit(AppTheme.DARK)

        assertThat(themeViewModel.theme.value).isEqualTo(AppTheme.DARK)

        themeViewModel.toggleTheme()
        themeFlow.emit(AppTheme.LIGHT)

        assertThat(themeViewModel.theme.value).isEqualTo(AppTheme.LIGHT)
    }
}