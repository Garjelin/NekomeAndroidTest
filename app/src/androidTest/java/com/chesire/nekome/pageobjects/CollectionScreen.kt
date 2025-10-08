package com.chesire.nekome.pageobjects

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithText
import com.chesire.nekome.app.series.collection.ui.FilterTags
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags
import com.chesire.nekome.base.BaseComposeScreen
import com.chesire.nekome.helpers.assertCollectionEmpty
import com.chesire.nekome.helpers.assertCollectionNotEmpty
import com.chesire.nekome.helpers.assertCollectionSize
import com.chesire.nekome.helpers.getCollectionSize
import com.chesire.nekome.helpers.kNodes.KExtendedFabNode
import com.chesire.nekome.helpers.kNodes.KSeriesItemNode
import io.github.kakaocup.compose.node.element.KNode

class CollectionScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<CollectionScreen>(semanticsProvider) {

    // TopAppBar элементы
    val appBarTitle: KNode = createNode(SeriesCollectionTags.AppBarTitle)
    val filterButton: KNode = createNode(SeriesCollectionTags.MenuFilter)
    val sortButton: KNode = createNode(SeriesCollectionTags.MenuSort)
    val refreshButton: KNode = createNode(SeriesCollectionTags.MenuRefresh)
    fun filterOptionChecked(testTag: String): KNode = createNode(testTag)
    val filterOkButton: KNode = createNode(FilterTags.OkButton)

    fun searchFab(block: KExtendedFabNode.() -> Unit = {}): KExtendedFabNode {
        return createNodeByTestTag(
            testTag = SeriesCollectionTags.SearchFab,
            block = block
        )
    }

    // Коллекция серий
    val seriesCollection = SeriesCollectionHelper()

    val emptyView: KNode = createNode(SeriesCollectionTags.EmptyView)
    val snackbar: KNode = createNode(SeriesCollectionTags.Snackbar)
    
    fun seriesItem(index: Int = 0, block: KSeriesItemNode.() -> Unit = {}): KSeriesItemNode {
        return createNodeByTestTag(
            testTag = "${SeriesCollectionTags.SeriesItem}_$index",
            block = block
        )
    }

    inner class SeriesCollectionHelper {
        val container: KNode = createNode(SeriesCollectionTags.SeriesCollectionContainer)

        fun getSize(): Int {
            return provider.getCollectionSize(SeriesCollectionTags.SeriesCollectionContainer)
        }

        fun assertSize(expectedCount: Int) {
            provider.assertCollectionSize(SeriesCollectionTags.SeriesCollectionContainer, expectedCount)
        }

        fun assertNotEmpty() {
            provider.assertCollectionNotEmpty(SeriesCollectionTags.SeriesCollectionContainer)
        }

        fun assertEmpty() {
            provider.assertCollectionEmpty(SeriesCollectionTags.SeriesCollectionContainer)
        }
    }
}
