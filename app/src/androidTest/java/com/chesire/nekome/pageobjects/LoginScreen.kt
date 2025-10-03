package com.chesire.nekome.pageobjects

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.chesire.nekome.base.BaseComposeScreen
import io.github.kakaocup.compose.node.element.KNode

/**
 * Page Object для экрана логина.
 * 
 * Использование в тестах:
 * ```kotlin
 * LoginScreen(composeTestRule) {
 *     usernameField {
 *         performTextInput("test@example.com")
 *     }
 *     passwordField {
 *         performTextInput("password123")
 *     }
 *     loginButton {
 *         performClick()
 *     }
 * }
 * ```
 */
class LoginScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<LoginScreen>(semanticsProvider) {

    // Элементы экрана (используйте реальные testTags из вашего UI)
    val usernameField: KNode = createNode("UsernameField")
    val passwordField: KNode = createNode("PasswordField")
    val loginButton: KNode = createNode("LoginButton")
    val forgotPasswordButton: KNode = createNode("ForgotPasswordButton")
    val signUpButton: KNode = createNode("SignUpButton")
    val errorMessage: KNode = createNode("ErrorMessage")
}

/**
 * DSL функция для работы с LoginScreen.
 * 
 * @param semanticsProvider провайдер семантики (обычно composeTestRule)
 * @param block блок действий на экране
 */
fun loginScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
    block: LoginScreen.() -> Unit
) = LoginScreen(semanticsProvider).apply(block)


