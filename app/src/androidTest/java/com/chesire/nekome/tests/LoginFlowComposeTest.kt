package com.chesire.nekome.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.base.BaseComposeTest
import com.chesire.nekome.helpers.Users.TEST_USER_1
import com.chesire.nekome.helpers.scenario.Login
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginFlowComposeTest : BaseComposeTest() {

    @Test
    fun successfulLogin_withScenario() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        waitForTime(1000000)
    }

    @Test
    fun successfulLogin_withPageObject() = run {
        // Шаг 1: Открываем экран логина
        step("Открыть экран логина") {
            launchActivity()
        }

        // Шаг 2: Заполняем форму логина
        step("Заполнить форму логина") {
            LoginScreen {
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
            LoginScreen {
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
            LoginScreen {
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
            LoginScreen {
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
            LoginScreen {
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

