package com.chesire.nekome.base

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

/**
 * Базовый класс для Page Objects (экранов) в Compose.
 * 
 * Используйте Page Object паттерн для создания переиспользуемых экранов:
 * 
 * ```kotlin
 * class LoginScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
 *     BaseComposeScreen<LoginScreen>(semanticsProvider) {
 *     
 *     val usernameField = createNode("UsernameField")
 *     val passwordField = createNode("PasswordField")
 *     val loginButton = createNode("LoginButton")
 * }
 * 
 * // В тесте:
 * LoginScreen(composeTestRule) {
 *     usernameField {
 *         performTextInput("user@example.com")
 *     }
 *     loginButton {
 *         performClick()
 *     }
 * }
 * ```
 * 
 * @param T тип экрана (для типобезопасности)
 */
abstract class BaseComposeScreen<out T : ComposeScreen<T>>(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<T>(semanticsProvider) {

    /**
     * Создать узел (KNode) по testTag.
     * 
     * @param testTag тег элемента (Modifier.testTag("MyTag"))
     * @return KNode для взаимодействия с элементом
     */
    protected fun createNode(testTag: String): KNode {
        // Используем child() который доступен из ComposeScreen
        return child {
            hasTestTag(testTag)
        }
    }
}

