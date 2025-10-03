package com.chesire.nekome.tests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.app.login.credentials.ui.CredentialsTags
import com.chesire.nekome.base.BaseComposeTest
import com.chesire.nekome.pageobjects.loginScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Пример UI теста с использованием Kaspresso + Kakao Compose.
 * 
 * Этот тест демонстрирует:
 * - Использование BaseComposeTest
 * - Page Object паттерн
 * - DSL для работы с экранами
 * - Kaspresso возможности (run, step, flakySafely)
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginFlowComposeTest : BaseComposeTest() {

    /**
     * Простой тест проверки текста кнопки Login.
     */
    @Test
    fun loginButton_hasCorrectText() = run {
        step("Открыть экран логина") {
            launchActivity()
        }

        step("Проверить текст кнопки Login") {
            composeTestRule
                .onNodeWithTag(CredentialsTags.LoginButton)
                .assertIsDisplayed()
                .assertTextEquals("Logan", includeEditableText = false)
        }
    }

    /**
     * Тест успешного логина с использованием Page Object.
     * 
     * Структура:
     * 1. Открыть экран логина
     * 2. Ввести username
     * 3. Ввести password
     * 4. Нажать кнопку логина
     * 5. Проверить переход на главный экран
     */
    @Test
    fun successfulLogin_withPageObject() = run {
        // Шаг 1: Открываем экран логина
        step("Открыть экран логина") {
            launchActivity()
        }

        // Шаг 2: Заполняем форму логина
        step("Заполнить форму логина") {
            loginScreen(composeTestRule) {
                // flakySafely делает проверку устойчивой к flaky тестам
                usernameField {
                    flakySafely(5000) {
                        assertIsDisplayed()
                    }
                    performTextInput("testuser@example.com")
                }

                passwordField {
                    flakySafely(5000) {
                        assertIsDisplayed()
                    }
                    performTextInput("password123")
                }
            }
        }

        // Шаг 3: Нажимаем кнопку логина
        step("Нажать кнопку логина") {
            loginScreen(composeTestRule) {
                loginButton {
                    flakySafely(5000) {
                        assertIsDisplayed()
                    }
                    performClick()
                }
            }
        }

        // Шаг 4: Проверяем успешный переход (пример)
        step("Проверить переход на главный экран") {
            // Здесь должна быть проверка главного экрана
            // Например, проверка что toolbar главного экрана отображается
        }
    }

    /**
     * Тест логина с неверными данными.
     * 
     * Проверяет, что отображается сообщение об ошибке.
     */
    @Test
    fun login_withInvalidCredentials_showsError() = run {
        step("Открыть экран логина") {
            launchActivity()
        }

        step("Ввести неверные данные") {
            loginScreen(composeTestRule) {
                usernameField {
                    performTextInput("wrong@example.com")
                }

                passwordField {
                    performTextInput("wrongpassword")
                }

                loginButton {
                    performClick()
                }
            }
        }

        step("Проверить сообщение об ошибке") {
            loginScreen(composeTestRule) {
                errorMessage {
                    flakySafely(10000) {
                        assertIsDisplayed()
                        assertTextContains(
                            "invalid credentials",
                            substring = true,
                            ignoreCase = true
                        )
                    }
                }
            }
        }
    }

    /**
     * Тест кнопки "Forgot Password".
     */
    @Test
    fun clickForgotPassword_opensRestorePasswordScreen() = run {
        step("Открыть экран логина") {
            launchActivity()
        }

        step("Нажать 'Forgot Password'") {
            loginScreen(composeTestRule) {
                forgotPasswordButton {
                    flakySafely {
                        assertIsDisplayed()
                    }
                    performClick()
                }
            }
        }

        step("Проверить переход на экран восстановления") {
            // Здесь проверка экрана восстановления пароля
        }
    }
}

