package com.chesire.nekome.pageobjects

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags
import com.chesire.nekome.base.BaseComposeScreen
import io.github.kakaocup.compose.node.element.KNode

/**
 * Page Object для экрана коллекции серий (Anime/Manga).
 * 
 * Использование в тестах:
 * ```kotlin
 * CollectionScreen {
 *     topBarTitle {
 *         assertIsDisplayed()
 *     }
 *     seriesItems {
 *         assertIsDisplayed()
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

    // Floating Action Button для поиска
    val searchFab: KNode = createNode(SeriesCollectionTags.SearchFab)

    // Контейнер со списком серий
    val refreshContainer: KNode = createNode(SeriesCollectionTags.RefreshContainer)

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

