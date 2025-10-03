package com.chesire.nekome.base

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.core.app.ActivityScenario
import com.chesire.nekome.ui.MainActivity
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule

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
     * ComposeTestRule для работы с Compose UI элементами.
     * 
     * Использование:
     * ```
     * composeTestRule.onNodeWithTag("LoginButton").performClick()
     * ```
     */
    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    /**
     * Инициализация Hilt перед каждым тестом.
     */
    @Before
    open fun setUpHilt() {
        hiltRule.inject()
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
     * Вывести семантическое дерево Compose в логи.
     * Полезно для отладки.
     * 
     * @param tag тег для фильтрации логов
     */
    protected fun printSemanticTree(tag: String = "SemanticTree") {
        composeTestRule.onRoot(useUnmergedTree = true)
            .printToLog(tag)
    }
}

