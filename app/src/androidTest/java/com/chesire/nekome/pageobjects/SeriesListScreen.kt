package com.chesire.nekome.pageobjects

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags
import com.chesire.nekome.base.BaseComposeScreen
import io.github.kakaocup.compose.node.element.KNode

/**
 * Page Object для экрана списка серий (CollectionScreen).
 * 
 * Соответствует testTag из SeriesCollectionTags в CollectionScreen.kt.
 * 
 * Использование:
 * ```kotlin
 * SeriesListScreen(composeTestRule) {
 *     filterButton {
 *         performClick()
 *     }
 *     
 *     seriesItem(0) {
 *         assertIsDisplayed()
 *         performClick()
 *     }
 *     
 *     plusOneButton {
 *         performClick()
 *     }
 * }
 * ```
 */
class SeriesListScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<SeriesListScreen>(semanticsProvider) {

    // Корневой элемент экрана
    val root: KNode = createNode(SeriesCollectionTags.Root)
    
    // Элементы меню в TopAppBar
    val filterButton: KNode = createNode(SeriesCollectionTags.MenuFilter)
    val sortButton: KNode = createNode(SeriesCollectionTags.MenuSort)
    val refreshButton: KNode = createNode(SeriesCollectionTags.MenuRefresh)
    
    // FAB для поиска
    val searchFab: KNode = createNode(SeriesCollectionTags.SearchFab)
    
    // Контейнер списка с pull-to-refresh
    val refreshContainer: KNode = createNode(SeriesCollectionTags.SeriesCollectionContainer)
    
    // Empty view (когда список пуст)
    val emptyView: KNode = createNode(SeriesCollectionTags.EmptyView)
    
    // Snackbar для ошибок
    val snackbar: KNode = createNode(SeriesCollectionTags.Snackbar)
    
    /**
     * Получить элемент серии в списке.
     * 
     * Примечание: все элементы списка имеют один и тот же testTag "SeriesCollectionSeriesItem",
     * поэтому для доступа к конкретному элементу используйте индекс через onAllNodesWithTag.
     * 
     * @return KNode элемента серии
     */
    fun seriesItem(): KNode = createNode(SeriesCollectionTags.SeriesItem)
    
    /**
     * Кнопка "+1" внутри элемента серии.
     * 
     * @return KNode кнопки +1
     */
    fun plusOneButton(): KNode = createNode(SeriesCollectionTags.PlusOne)
}

/**
 * DSL функция для работы с SeriesListScreen.
 */
fun seriesListScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
    block: SeriesListScreen.() -> Unit
) = SeriesListScreen(semanticsProvider).apply(block)

