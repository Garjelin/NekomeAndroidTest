package com.chesire.nekome.pageobjects

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags
import com.chesire.nekome.base.BaseComposeScreen
import com.chesire.nekome.helpers.kNodes.KExtendedFabNode
import com.chesire.nekome.helpers.kNodes.KSeriesItemNode
import io.github.kakaocup.compose.node.element.KNode

/**
 * Page Object для экрана коллекции серий (Anime/Manga).
 * 
 * Использование в тестах:
 * ```kotlin
 * CollectionScreen {
 *     appBarTitle {
 *         assertIsDisplayed()
 *     }
 *     searchFab {
 *         assertIsDisplayed()
 *         text { assertTextEquals("Add new") }
 *     }
 * }
 * ```
 */
class CollectionScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<CollectionScreen>(semanticsProvider) {

    // Root элемент экрана
    val root: KNode = createNode(SeriesCollectionTags.Root)

    // TopAppBar элементы
    val appBarTitle: KNode = createNode(SeriesCollectionTags.AppBarTitle)
    val filterButton: KNode = createNode(SeriesCollectionTags.MenuFilter)
    val sortButton: KNode = createNode(SeriesCollectionTags.MenuSort)
    val refreshButton: KNode = createNode(SeriesCollectionTags.MenuRefresh)

    fun searchFab(block: KExtendedFabNode.() -> Unit = {}): KExtendedFabNode {
        return createNodeByTestTag(
            testTag = SeriesCollectionTags.SearchFab,
            block = block
        )
    }

    // Контейнер со списком серий
    val seriesCollectionContainer: KNode = createNode(SeriesCollectionTags.SeriesCollectionContainer)

    // Empty View (показывается когда нет серий)
    val emptyView: KNode = createNode(SeriesCollectionTags.EmptyView)

    // Snackbar для ошибок
    val snackbar: KNode = createNode(SeriesCollectionTags.Snackbar)

    /**
     * Получить карточку серии по индексу (с индексации 0).
     * Используется для проверки элементов внутри карточки.
     * 
     * Пример использования:
     * ```kotlin
     * seriesItem(0) {
     *     assertIsDisplayed()
     *     poster { assertIsDisplayed() }
     *     title { assertTextContains("Attack on Titan") }
     *     progress { assertTextEquals("1 / 25") }
     *     plusOneButton { performClick() }
     *     plusOneIcon { assertIsDisplayed() }
     * }
     * ```
     */
    fun seriesItem(index: Int = 0, block: KSeriesItemNode.() -> Unit = {}): KSeriesItemNode {
        return createNodeByTestTag(
            testTag = "${SeriesCollectionTags.SeriesItem}_$index",
            block = block
        )
    }
}

/**
 * DSL функция для работы с CollectionScreen.
 */
fun collectionScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
    block: CollectionScreen.() -> Unit
) = CollectionScreen(semanticsProvider).apply(block)
