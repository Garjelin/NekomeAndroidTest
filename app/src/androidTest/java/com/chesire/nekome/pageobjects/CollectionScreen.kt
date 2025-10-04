package com.chesire.nekome.pageobjects

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags
import com.chesire.nekome.base.BaseComposeScreen
import com.chesire.nekome.helpers.kNodes.KExtendedFabNode
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
        return createNodeByTestTag(SeriesCollectionTags.SearchFab, block)
    }

    // Контейнер со списком серий
    val seriesCollectionContainer: KNode = createNode(SeriesCollectionTags.SeriesCollectionContainer)

    // Empty View (показывается когда нет серий)
    val emptyView: KNode = createNode(SeriesCollectionTags.EmptyView)

    // Snackbar для ошибок
    val snackbar: KNode = createNode(SeriesCollectionTags.Snackbar)

    /**
     * Получить карточку серии по индексу.
     * Используется для проверки отдельных карточек.
     */
    fun seriesItemAt(index: Int): KNode {
        return createNode("${SeriesCollectionTags.SeriesItem}_$index")
    }
}

/**
 * DSL функция для работы с CollectionScreen.
 */
fun collectionScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
    block: CollectionScreen.() -> Unit
) = CollectionScreen(semanticsProvider).apply(block)
