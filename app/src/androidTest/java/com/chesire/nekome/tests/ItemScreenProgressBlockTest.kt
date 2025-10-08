package com.chesire.nekome.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.base.BaseComposeTest
import com.chesire.nekome.helpers.Users.TEST_USER_1
import com.chesire.nekome.helpers.annotations.Debug
import com.chesire.nekome.helpers.assertTextMatches
import com.chesire.nekome.helpers.closeKeyboard
import com.chesire.nekome.helpers.getText
import com.chesire.nekome.helpers.scenario.Login
import com.kaspersky.kaspresso.annotations.Regression
import dagger.hilt.android.testing.HiltAndroidTest
import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.Link
import io.qameta.allure.kotlin.Owner
import io.qameta.allure.kotlin.Story
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test
import org.junit.runner.RunWith

@Debug
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Owner("Team")
@Epic("Основной функционал")
@Feature("Детальная карточка")
@Story("Блок Прогресс")
class ItemScreenProgressBlockTest : BaseComposeTest() {

    @Test
    @Regression
    @Link(
        name = "Тест-кейс",
        url = "https://sergey-yakimov.youtrack.cloud/issue/DAT-9"
    )
    @DisplayName("Ввод корректного значения")
    fun enteringCorrectValue() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        var progressValue: Pair<Int, Int>? = null
        step("Сохранить значения Прогресса") {
            CollectionScreen {
                seriesItem(0) {
                    var progressValueText = ""
                    progress {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertTextMatches(Regex("""\d+ / \d+"""))
                        }
                        progressValueText = getText()
                    }
                    val progressValueList = progressValueText.split(" / ")
                    progressValue = Pair(progressValueList[0].toInt(), progressValueList[1].toInt())
                }
            }
        }
        step("Переход на детальную карточку") {
            CollectionScreen {
                seriesItem(0) {
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                        performClick()
                    }
                }
            }
        }
        step("Ввод значения") {
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                    performTextReplacement((progressValue!!.second - 1).toString())
                }
                closeKeyboard()
                confirmButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Confirm")
                    }
                    performClick()
                }
            }
        }
        step("Проверяем переход на главный экран Анимэ") {
            CollectionScreen {
                seriesItem(0) {
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                        performClick()
                    }
                }
            }
        }
        step("Проверяем что значение сохранилось в блоке Прогресс") {
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals((progressValue!!.second - 1).toString())
                    }
                }
            }
        }
        step("Возврат в исходное состояние") {
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                    performTextReplacement(progressValue?.first.toString())
                }
                closeKeyboard()
                confirmButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Confirm")
                    }
                    performClick()
                }
            }
        }
    }

    @Test
    @Regression
    @Link(
        name = "Тест-кейс",
        url = "https://sergey-yakimov.youtrack.cloud/issue/DAT-10"
    )
    @DisplayName("Ввод некорректного значения (больше допустимого)")
    fun enteringIncorrectValueGreaterThanAllowedValue() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        var progressValue: Pair<Int, Int>? = null
        step("Сохранить значения Прогресса") {
            CollectionScreen {
                seriesItem(0) {
                    var progressValueText = ""
                    progress {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertTextMatches(Regex("""\d+ / \d+"""))
                        }
                        progressValueText = getText()
                    }
                    val progressValueList = progressValueText.split(" / ")
                    progressValue = Pair(progressValueList[0].toInt(), progressValueList[1].toInt())
                }
            }
        }
        step("Переход на детальную карточку") {
            CollectionScreen {
                seriesItem(0) {
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                        performClick()
                    }
                }
            }
        }
        step("Ввод значения") {
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                    performTextReplacement((progressValue!!.second + 1).toString())
                }
                closeKeyboard()
                confirmButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Confirm")
                    }
                    performClick()
                }
            }
        }
        step("Проверка снекбара") {
            // Проверка появления Snackbar с текстом - ждем пока появится текст
            waitForText("Failed to update", 5000)
            // Проверяем что Snackbar с текстом исчез
            waitForTextNotExist("Failed to update", 5000)
        }
        step("Проверяем что мы остались на странице с блоком Прогресс") {
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
            }
        }
    }

    @Test
    @Regression
    @Link(
        name = "Тест-кейс",
        url = "https://sergey-yakimov.youtrack.cloud/issue/DAT-11"
    )
    @DisplayName("Ввод некорректного значения (отрицательное число)")
    fun enteringIncorrectValueNegativeNumber() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        step("Переход на детальную карточку") {
            CollectionScreen {
                seriesItem(0) {
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                        performClick()
                    }
                }
            }
        }
        step("Ввод значения") {
            val testValue = "-3"
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                    performTextReplacement(testValue)
                }
                closeKeyboard()
                confirmButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Confirm")
                    }
                    performClick()
                }
            }
        }
        step("Проверка снекбара") {
            // Проверка появления Snackbar с текстом - ждем пока появится текст
            waitForText("Failed to update", 5000)
            // Проверяем что Snackbar с текстом исчез
            waitForTextNotExist("Failed to update", 5000)
        }
        step("Проверяем что мы остались на странице с блоком Прогресс") {
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
            }
        }
    }

    @Test
    @Regression
    @Link(
        name = "Тест-кейс",
        url = "https://sergey-yakimov.youtrack.cloud/issue/DAT-12"
    )
    @DisplayName("Ввод некорректного значения (дробное через запятую)")
    fun enteringIncorrectValueFractionalSeparatedByComma() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        step("Переход на детальную карточку") {
            CollectionScreen {
                seriesItem(0) {
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                        performClick()
                    }
                }
            }
        }
        step("Ввод значения") {
            val testValue = "2,5"
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                    performTextReplacement(testValue)
                }
                closeKeyboard()
                confirmButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Confirm")
                    }
                    performClick()
                }
            }
        }
        step("Проверка снекбара") {
            // Проверка появления Snackbar с текстом - ждем пока появится текст
            waitForText("Failed to update", 5000)
            // Проверяем что Snackbar с текстом исчез
            waitForTextNotExist("Failed to update", 5000)
        }
        step("Проверяем что мы остались на странице с блоком Прогресс") {
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
            }
        }
    }

    @Test
    @Regression
    @Link(
        name = "Тест-кейс",
        url = "https://sergey-yakimov.youtrack.cloud/issue/DAT-13"
    )
    @DisplayName("Ввод некорректного значения (дробное через точку)")
    fun enteringIncorrectValueFractionalSeparatedByPoint() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        step("Переход на детальную карточку") {
            CollectionScreen {
                seriesItem(0) {
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                        performClick()
                    }
                }
            }
        }
        step("Ввод значения") {
            val testValue = "2.5"
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                    performTextReplacement(testValue)
                }
                closeKeyboard()
                confirmButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Confirm")
                    }
                    performClick()
                }
            }
        }
        step("Проверка снекбара") {
            // Проверка появления Snackbar с текстом - ждем пока появится текст
            waitForText("Failed to update", 5000)
            // Проверяем что Snackbar с текстом исчез
            waitForTextNotExist("Failed to update", 5000)
        }
        step("Проверяем что мы остались на странице с блоком Прогресс") {
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
            }
        }
    }

    @Test
    @Regression
    @Link(
        name = "Тест-кейс",
        url = "https://sergey-yakimov.youtrack.cloud/issue/DAT-14"
    )
    @DisplayName("Ввод некорректного значения (содержит буквы)")
    fun enteringIncorrectValueContainsLetters() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        step("Переход на детальную карточку") {
            CollectionScreen {
                seriesItem(0) {
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                        performClick()
                    }
                }
            }
        }
        step("Ввод значения") {
            val testValue = "2qw"
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                    performTextReplacement(testValue)
                }
                closeKeyboard()
                confirmButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Confirm")
                    }
                    performClick()
                }
            }
        }
        step("Проверка снекбара") {
            // Проверка появления Snackbar с текстом - ждем пока появится текст
            waitForText("Failed to update", 5000)
            // Проверяем что Snackbar с текстом исчез
            waitForTextNotExist("Failed to update", 5000)
        }
        step("Проверяем что мы остались на странице с блоком Прогресс") {
            ItemScreen {
                outlinedTextField {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
            }
        }
    }
}

