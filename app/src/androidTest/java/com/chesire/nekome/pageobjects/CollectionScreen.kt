package com.chesire.nekome.pageobjects

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
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
 *     
 *     // Проверить количество карточек серий
 *     assertSeriesItemsCount(10)
 *     
 *     // Или получить количество
 *     val count = getSeriesItemsCount()
 *     
 *     // Работа с конкретной карточкой
 *     seriesItem(0) {
 *         poster { assertIsDisplayed() }
 *         title { assertTextContains("Attack on Titan") }
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

    /**
     * Получить количество карточек серий в списке через CollectionInfo.
     * 
     * Пример использования:
     * ```kotlin
     * CollectionScreen {
     *     val count = getSeriesItemsCount()
     *     println("Found $count series items")
     * }
     * ```
     * 
     * @return количество карточек серий в списке
     */
    fun getSeriesItemsCount(): Int {
        val node = provider.onNode(
            hasTestTag(SeriesCollectionTags.SeriesCollectionContainer),
            useUnmergedTree = true
        ).fetchSemanticsNode()
        
        val collectionInfo = node.config.getOrNull(SemanticsProperties.CollectionInfo)
        return collectionInfo?.rowCount ?: 0
    }

    /**
     * Проверить, что количество карточек серий соответствует ожидаемому.
     * 
     * Пример использования:
     * ```kotlin
     * CollectionScreen {
     *     assertSeriesItemsCount(10)
     * }
     * ```
     * 
     * @param expectedCount ожидаемое количество карточек
     */
    fun assertSeriesItemsCount(expectedCount: Int) {
        val actualCount = getSeriesItemsCount()
        assert(actualCount == expectedCount) {
            "Expected $expectedCount series items, but found $actualCount"
        }
    }
}

/**
 * DSL функция для работы с CollectionScreen.
 */
fun collectionScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
    block: CollectionScreen.() -> Unit
) = CollectionScreen(semanticsProvider).apply(block)
