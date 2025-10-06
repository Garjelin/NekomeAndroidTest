package com.chesire.nekome.base

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.core.app.ActivityScenario
import com.adevinta.android.barista.rule.cleardata.ClearDatabaseRule
import com.adevinta.android.barista.rule.cleardata.ClearPreferencesRule
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.datasource.auth.local.AuthProvider
import com.chesire.nekome.helpers.logout
import com.chesire.nekome.helpers.reset
import com.chesire.nekome.pageobjects.CollectionScreen
import com.chesire.nekome.pageobjects.ItemScreen
import com.chesire.nekome.pageobjects.LoginScreen
import com.chesire.nekome.ui.MainActivity
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Базовый класс для Compose UI тестов с Kaspresso.
 * 
 * Наследуйтесь от этого класса для создания UI тестов:
 * ```
 * class MyFeatureTest : BaseComposeTest() {
 *     @Test
 *     fun testMyFeature() = run {
 *         launchActivity()
 *         // Ваш тест
 *     }
 * }
 * ```
 * 
 * Возможности:
 * - Автоматические скриншоты при падении теста
 * - flakySafely для устойчивых проверок
 * - step() для структурирования тестов
 * - ComposeTestRule для работы с Compose UI
 */
abstract class BaseComposeTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple()
) {

    /**
     * HiltAndroidRule для работы с Hilt dependency injection.
     * Должен быть первым правилом для корректной инициализации.
     */
    @Suppress("LeakingThis")
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Правило для очистки базы данных между тестами.
     */
    @get:Rule(order = 1)
    val clearDatabase = ClearDatabaseRule()

    /**
     * Правило для очистки SharedPreferences между тестами.
     */
    @get:Rule(order = 2)
    val clearPreferences = ClearPreferencesRule()

    /**
     * ComposeTestRule для работы с Compose UI элементами.
     * 
     * Использование:
     * ```
     * composeTestRule.onNodeWithTag("LoginButton").performClick()
     * ```
     */
    @get:Rule(order = 3)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var authProvider: AuthProvider

    @Inject
    lateinit var applicationPreferences: ApplicationPreferences

    @Inject
    lateinit var seriesPreferences: SeriesPreferences

    protected val LoginScreen: LoginScreen by lazy { LoginScreen(composeTestRule) }

    /**
     * Page Object для экрана коллекции серий (Anime/Manga).
     */
    protected val CollectionScreen: CollectionScreen by lazy { CollectionScreen(composeTestRule) }
    protected val ItemScreen: ItemScreen by lazy { ItemScreen(composeTestRule) }

    /**
     * Инициализация Hilt и очистка состояния перед каждым тестом.
     */
    @Before
    open fun setUp() {
        hiltRule.inject()
        
        // Очищаем состояние аутентификации и настройки
        runBlocking {
            applicationPreferences.reset()
            seriesPreferences.reset()
        }
        
        // Логаутим пользователя, чтобы каждый тест начинался с экрана логина
        authProvider.logout()
    }

    /**
     * Запускает MainActivity используя ActivityScenario.
     * 
     * Рекомендуется вызывать в начале каждого теста.
     */
    protected fun launchActivity() {
        ActivityScenario.launch(MainActivity::class.java)
        // Небольшая задержка для стабилизации Compose UI
        Thread.sleep(200)
    }

    /**
     * Утилита для ожидания определенного времени.
     * 
     * @param millis время ожидания в миллисекундах
     */
    protected fun waitForTime(millis: Long) {
        val startTime = System.currentTimeMillis()
        composeTestRule.waitUntil(millis + 1000) {
            System.currentTimeMillis() - startTime >= millis
        }
    }

    /**
     * Явная очистка состояния приложения.
     * Используйте этот метод, если нужно очистить состояние в середине теста.
     */
    protected fun clearAppState() {
        runBlocking {
            applicationPreferences.reset()
            seriesPreferences.reset()
        }
        authProvider.logout()
    }
}

