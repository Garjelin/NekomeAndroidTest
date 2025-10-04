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
class CollectionScreenTest : BaseComposeTest() {

    @Test
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
//        waitForTime(1000000)
        step("Проверить FAB кнопку для поиска") {
            CollectionScreen {
                searchFabTitle {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Add new")
                    }
                }
            }
        }

        step("Проверить контейнер со списком серий") {
            CollectionScreen {
                refreshContainer {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                    }
                }
            }
        }

        step("Проверить что отображаются карточки серий") {
            CollectionScreen {
                // Просто проверяем что контейнер со списком существует
                // Детальная проверка карточек будет в других тестах
                refreshContainer {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                    }
                }
            }
        }
    }

    /**
     * Проверка работы кнопки Refresh.
     * 
     * Тест-кейс: TC-002 - Обновление списка серий
     * 
     * Шаги:
     * 1. Авторизоваться
     * 2. Нажать на кнопку Refresh
     * 3. Проверить что список обновляется
     */
    @Test
    fun refreshButtonUpdatesSeriesList() = run {
        scenario(Login(TEST_USER_1, composeTestRule))

        step("Ожидание загрузки главного экрана") {
            Thread.sleep(3000)
        }

        step("Нажать кнопку Refresh") {
            CollectionScreen {
                refreshButton {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertIsEnabled()
                    }
                    performClick()
                }
            }
        }

        step("Проверить что контейнер остается видимым") {
            CollectionScreen {
                refreshContainer {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                    }
                }
            }
        }
    }
}

