package com.chesire.nekome.tests

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.base.BaseComposeTest
import com.chesire.nekome.helpers.Users.TEST_USER_1
import com.chesire.nekome.helpers.scenario.Login
import com.chesire.nekome.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тесты для экрана коллекции серий (Anime/Manga).
 * 
 * Проверяет отображение всех элементов UI на главном экране после авторизации.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CollectionScreenTest : BaseComposeTest() {

    @Test
    fun checkAllCollectionScreenElements() = run {
        // Авторизация
        ActivityScenario.launch(MainActivity::class.java)
        waitForTime(3000)
        scenario(Login(TEST_USER_1, composeTestRule))


//        step("Ожидание загрузки главного экрана с коллекцией") {
//            // Даем время на загрузку данных с сервера
//            Thread.sleep(3000)
//        }
//
        step("Проверить отображение TopBar") {
            CollectionScreen {
                root {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                    }
                }
            }
        }

        step("Проверить заголовок TopBar") {
            CollectionScreen {
                appBarTitle {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Anime", includeEditableText = false)
                    }
                }
            }
        }

        step("Проверить кнопки в TopBar") {
            CollectionScreen {
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
                    // FAB может быть скрыт при скролле, поэтому используем flakySafely
                    flakySafely(5_000) {
                        assertExists()
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

