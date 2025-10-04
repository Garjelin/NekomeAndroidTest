package com.chesire.nekome.helpers.scenario

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.test.core.app.ActivityScenario
import com.chesire.nekome.helpers.Users
import com.chesire.nekome.pageobjects.LoginScreen
import com.chesire.nekome.ui.MainActivity
import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

class Login(
    private val user: Users,
    private val semanticsProvider: SemanticsNodeInteractionsProvider
) : Scenario() {

    override val steps: TestContext<Unit>.() -> Unit = {
        ActivityScenario.launch(MainActivity::class.java)

        val loginScreen = LoginScreen(semanticsProvider)

        step("Ввести email: ${user.login}") {
            loginScreen {
                usernameField {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Kitsu email", includeEditableText = false)
                    }
                    performClick()
                    performTextInput(user.login)
                }
            }
        }

        step("Ввести пароль") {
            loginScreen {
                passwordField {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Password", includeEditableText = false)
                    }
                    performClick()
                    performTextInput(user.password)
                }
            }
        }

        step("Нажать кнопку Login") {
            loginScreen {
                loginButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Login", includeEditableText = false)
                        assertIsEnabled()
                    }
                    performClick()
                }
            }
        }
    }
}

