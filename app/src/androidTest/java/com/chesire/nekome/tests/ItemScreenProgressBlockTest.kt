package com.chesire.nekome.tests

import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.app.series.item.ui.ItemScreenTags
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
    @Link(name = "Тест-кейс", url = "https://testrail.bcs.ru/testrail/index.php?/cases/view/60786872")
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
                // Проверка появления Snackbar

                composeTestRule.onNodeWithTag(ItemScreenTags.Snackbar).apply {
                    flakySafely(5_000) {
                        assertIsDisplayed()
//                        assertTextContains("Failed to update", substring = true)
                    }
                }
                printSemanticTreeByTag(ItemScreenTags.Snackbar)
                printFullSemanticTree()
//                composeTestRule.onNodeWithTag(ItemScreenTags.Snackbar).apply {
//                    flakySafely(10_000) {
//                        assertDoesNotExist() // Проверяем, что Snackbar исчез
//                    }
//                }

            }
        }
    }

    @Test
    @Regression
    @Link(name = "Тест-кейс", url = "https://testrail.bcs.ru/testrail/index.php?/cases/view/60786872")
    @DisplayName("Ввод некорректного значения (больше допустимого)")
    fun enteringIncorrectValueGreaterThanAllowedValueFail() = run {
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
                // Проверка появления Snackbar

                composeTestRule.onNodeWithTag(ItemScreenTags.Snackbar).apply {
                    flakySafely(5_000) {
                        assertIsDisplayed()
//                        assertTextContains("Failed to update", substring = true)
                    }
                }
                printSemanticTreeByTag(ItemScreenTags.Snackbar)
                printFullSemanticTree()
                waitForTime(10_000)
                composeTestRule.onNodeWithTag(ItemScreenTags.Snackbar).apply {
                    flakySafely(5_000) {
                        assertIsDisplayed()
//                        assertTextContains("Failed to update", substring = true)
                    }
                }
            }
        }
    }
}

