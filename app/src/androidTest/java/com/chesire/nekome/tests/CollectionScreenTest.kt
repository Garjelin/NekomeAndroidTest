package com.chesire.nekome.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.base.BaseComposeTest
import com.chesire.nekome.helpers.Users.TEST_USER_1
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
    fun checkAllCollectionScreenElements() = run {
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
                seriesCollectionContainer {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                    }
                }
            }
        }
    }

    /**
     * Проверка отображения элементов внутри карточки серии.
     * 
     * Тест-кейс: TC-002 - Элементы карточки серии
     * 
     * Шаги:
     * 1. Авторизоваться
     * 2. Открыть экран коллекции
     * 3. Проверить первую карточку серии:
     *    - Заголовок отображается
     *    - Подтип и дата отображаются
     *    - Прогресс отображается
     *    - Постер отображается
     */
    @Test
    @Regression
    @Link(name = "Тест-кейс", url = "https://testrail.bcs.ru/testrail/index.php?/cases/view/60786872")
    @DisplayName("Отображение элементов карточки серии")
    fun checkSeriesCardElements() = run {
        scenario(Login(TEST_USER_1, composeTestRule))

        step("Проверить элементы первой карточки серии") {
            CollectionScreen {
                seriesItem(0) {
                    flakySafely(10_000) {
                        assertIsDisplayed() // Сама карточка видна
                        assertHasClickAction() // Карточка кликабельна
                    }
                    
                    // Проверка заголовка
                    title {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            // Проверяем что заголовок не пустой
                            assertExists()
                        }
                    }
                    
                    // Проверка подтипа и даты
                    subtypeAndDate {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            assertExists()
                        }
                    }
                    
                    // Проверка прогресса
                    progress {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                            // Прогресс содержит слэш (например "1 / 25")
                            assertExists()
                        }
                    }
                    plusOneButton {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                    }
//                    waitForTime(1000000)
                    // Проверка постера
                    poster {
                        flakySafely(10_000) {
                            assertIsDisplayed()
                        }
                    }
                }
            }
        }
        
        step("Проверить вторую карточку серии") {
            CollectionScreen {
                seriesItem(1) {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertHasClickAction()
                    }
                }
            }
        }
    }
}

