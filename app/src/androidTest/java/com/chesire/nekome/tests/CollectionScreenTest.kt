package com.chesire.nekome.tests

import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.onNodeWithTag
import androidx.glance.semantics.SemanticsProperties
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.app.series.collection.ui.FilterTags
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
@Feature("Cтраница Anime")
@Story("Главная страница")
class CollectionScreenTest : BaseComposeTest() {



    @Test
    @Regression
    @Link(name = "Тест-кейс", url = "https://testrail.bcs.ru/testrail/index.php?/cases/view/60786871")
    @DisplayName("Отображение элементов страницы Anime")
    fun displayingAnimePageElements() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        step("Проверка TopBar") {
            CollectionScreen {
                // Заголовок страницы
                appBarTitle {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Anime")
                    }
                }
                // Кнопка Filter
                filterButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertIsEnabled()
                        assertContentDescriptionEquals("Filter") // res/values/strings.xml
                        assertHasClickAction()
                    }
                }
                // Кнопка Sort
                sortButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertIsEnabled()
                        assertContentDescriptionEquals("Sort") // res/values/strings.xml
                        assertHasClickAction()
                    }
                }
                // Кнопка Refresh
                refreshButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertIsEnabled()
                        assertContentDescriptionEquals("Refresh series") // res/values/strings.xml
                        assertHasClickAction()
                    }
                }
            }
        }
        step("Проверить FAB кнопку для поиска") {
            CollectionScreen {
                searchFab {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertIsEnabled()
                        assertHasClickAction()
                    }
                    text {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertTextEquals("Add new")
                        }
                    }
                    icon {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertContentDescriptionEquals("Search for new series to add")
                        }
                    }
                }
            }
        }
        step("Проверить что отображаются карточки серий") {
            CollectionScreen {
                seriesCollection.container {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                    }
                }
                for (index in 0 until seriesCollection.getSize()) {
                    seriesItem(index) {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertHasClickAction()
                        }
                    }
                }
            }
        }
    }

    @Test
    @Regression
    @Link(name = "Тест-кейс", url = "https://testrail.bcs.ru/testrail/index.php?/cases/view/60786872")
    @DisplayName("Отображение элементов карточки")
    fun displayingCardElements() = run {
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

    @Test
    @Regression
    @Link(
        name = "Тест-кейс",
        url = "https://testrail.bcs.ru/testrail/index.php?/cases/view/60786873"
    )
    @DisplayName("Переход на детальную карточку")
    fun displayingElementsOfDetailedCard() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        var seriesTitle = ""
        step("Переход на детальную карточку") {
            CollectionScreen {
                seriesItem(0) {
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                        seriesTitle = getText()
                        performClick()
                    }
                }
            }
        }
        step("Проверка Header") {
            ItemScreen {
                title {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals(seriesTitle)
                    }
                }
            }
        }
    }

    @Test
    @Regression
    @Link(name = "Тест-кейс", url = "https://testrail.bcs.ru/testrail/index.php?/cases/view/60786872")
    @DisplayName("Увеличение счётчика прогресса")
    fun increasingProgressCounter() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        var progressValue: Pair<Int, Int>? = null
        step("Сохранить значения Прогресса и нажать на кнопку '+1'") {
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
                    // Иконка +1
                    plusOneButton {
                        flakySafely(10_000) { assertIsDisplayed() }
                        performClick()
                    }
                    progress {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertTextEquals((progressValue.first + 1).toString() + " / " + progressValue.second.toString())
                        }
                    }
                }
            }
        }
        step("Возврат в исходное состояние") {
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
            CollectionScreen {
                seriesItem(0) {
                    progress {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertTextEquals((progressValue?.first).toString() + " / " + progressValue?.second.toString())
                        }
                    }
                }
            }
        }
    }

    @Test
    @Regression
    @Link(name = "Тест-кейс", url = "https://testrail.bcs.ru/testrail/index.php?/cases/view/60786872")
    @DisplayName("Карточка исчезает при достижении максимального прогресса")
    fun cardDisappearsWhenMaximumProgressIsReached() = run {
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
                    }
                }
            }
        }
        step("Нажать на кнопку '+1'") {
            CollectionScreen {
                seriesItem(0) {
                    // Иконка +1
                    plusOneButton {
                        flakySafely(10_000) { assertIsDisplayed() }
                        performClick()
                    }
                }
            }
        }
        waitForTime(10_000)
        step("Возврат в исходное состояние") {
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
            CollectionScreen {
                seriesItem(0) {
                    progress {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertTextEquals((progressValue?.first).toString() + " / " + progressValue?.second.toString())
                        }
                    }
                }
            }
        }
    }

    @Test
    @Regression
    @Link(name = "Тест-кейс", url = "https://testrail.bcs.ru/testrail/index.php?/cases/view/60786871")
    @DisplayName("Отображение элементов страницы Anime")
    fun displayingAnimePageElementsq() = run {
        scenario(Login(TEST_USER_1, composeTestRule))
        step("Проверка TopBar") {
            CollectionScreen {
                // Кнопка Filter
                filterButton {
                    flakySafely(10_000) { assertIsDisplayed() }
                    performClick()
                }
                filterOptionChecked("FilterOptionChecked_Completed").apply {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertIsOff()
                    }
                    performClick()
                    flakySafely(10_000) { assertIsOn() }
                }
                waitForTime(3000)
                printSemanticTreeByTag(FilterTags.Root)
                // Кнопка Sort
            }
        }
    }
}

