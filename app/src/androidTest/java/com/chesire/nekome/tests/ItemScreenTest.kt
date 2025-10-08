package com.chesire.nekome.tests

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.base.BaseComposeTest
import com.chesire.nekome.helpers.Users.TEST_USER_1
import com.chesire.nekome.helpers.annotations.Debug
import com.chesire.nekome.helpers.assertTextMatches
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
@Story("Главная страница")
class ItemScreenTest : BaseComposeTest() {

    @Test
    @Regression
    @Link(
        name = "Тест-кейс",
        url = "https://sergey-yakimov.youtrack.cloud/issue/DAT-1"
    )
    @DisplayName("Отображение элементов детальной карточки")
    fun displayingElementsOfDetailedCard() = run {
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
        step("Проверка TopAppBar") {
            ItemScreen {
                backButton {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
                deleteButton {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
            }
        }
        step("Проверка Header") {
            ItemScreen {
                title {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextMatches(Regex(""".+"""))
                    }
                }
                subtitle {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextMatches(Regex(""".+"""))
                    }
                }
                seriesImage {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
            }
        }
        step("Проверка блока Статусы") {
            ItemScreen {
                seriesStatusTitle {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Users series status")
                    }
                }
                seriesStatusChips {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
            }
        }
        step("Проверка блока Прогресс") {
            ItemScreen {
                progressTitle {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Progress")
                    }
                }
                progressInput {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
                outlinedTextField {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        val actualText = getText()
                        Log.d("LOG_MSG_1","Progress input text: '$actualText'")
                        assertTextMatches(Regex("""\d+"""))
                    }
                    trailingIcon {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertTextMatches(Regex("""/ \d+"""))
                        }
                    }
                }
            }
        }
        step("Проверка блока Рейтинг") {
            ItemScreen {
                ratingTitle {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Rating")
                    }
                }
                ratingSlider {
                    flakySafely(10_000) { assertIsDisplayed() }
                }
                ratingValue {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextMatches(Regex(""".+"""))
                    }
                }
            }
        }
        step("Проверка кнопки Подтвердить") {
            ItemScreen {
                confirmButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Confirm")
                    }
                }
            }
        }
    }

    @Test
    @Regression
    @Link(
        name = "Тест-кейс",
        url = "https://sergey-yakimov.youtrack.cloud/issue/DAT-2"
    )
    @DisplayName("Отображение элементов карточки серии")
    fun checkSeriesCardElements() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        step("Проверить элементы первой карточки серии") {
            CollectionScreen {
                //  Первый блок карточки
                seriesItem(0) {
                    // Заголовок
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertExists()
                        }
                    }
                    // Подзаголовок и дата
                    subtypeAndDate {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertExists()
                        }
                    }
                    // Прогресс
                    progress {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertTextMatches(Regex("""\d+ / \d+"""))
                        }
                    }
                    // Иконка +1
                    plusOneButton {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                    }
                    // Проверка постера
                    poster {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                    }
                }
            }
        }
    }
}

