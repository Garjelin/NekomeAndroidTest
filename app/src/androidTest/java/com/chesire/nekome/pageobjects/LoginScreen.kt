package com.chesire.nekome.pageobjects

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.chesire.nekome.app.login.credentials.ui.CredentialsTags
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

    // Элементы экрана (используют реальные testTags из CredentialsScreen)
    val usernameField: KNode = createNode(CredentialsTags.Username)
    val passwordField: KNode = createNode(CredentialsTags.Password)
    val loginButton: KNode = createNode(CredentialsTags.LoginButton)
    val forgotPasswordButton: KNode = createNode("ForgotPasswordButton")
    val signUpButton: KNode = createNode("SignUpButton")
    val errorMessage: KNode = createNode("ErrorMessage")
}


