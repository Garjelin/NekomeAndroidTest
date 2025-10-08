package com.chesire.nekome.pageobjects

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags
import com.chesire.nekome.app.series.item.ui.ItemScreenTags
import com.chesire.nekome.base.BaseComposeScreen
import com.chesire.nekome.helpers.assertCollectionEmpty
import com.chesire.nekome.helpers.assertCollectionNotEmpty
import com.chesire.nekome.helpers.assertCollectionSize
import com.chesire.nekome.helpers.getCollectionSize
import com.chesire.nekome.helpers.kNodes.KExtendedFabNode
import com.chesire.nekome.helpers.kNodes.KOutlinedTextField
import com.chesire.nekome.helpers.kNodes.KSeriesItemNode
import io.github.kakaocup.compose.node.element.KNode

class ItemScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<ItemScreen>(semanticsProvider) {

    // TopAppBar elements
    val backButton: KNode = createNode(ItemScreenTags.BackButton)
    val deleteButton: KNode = createNode(ItemScreenTags.DeleteButton)

    // Header area
    val title: KNode = createNode(ItemScreenTags.Title)
    val subtitle: KNode = createNode(ItemScreenTags.Subtitle)
    val seriesImage: KNode = createNode(ItemScreenTags.SeriesImage)

    // Series status
    val seriesStatusTitle: KNode = createNode(ItemScreenTags.SeriesStatusTitle)
    val seriesStatusChips: KNode = createNode(ItemScreenTags.SeriesStatusChips)
    fun seriesStatusChip(status: String, block: KNode.() -> Unit = {}): KNode {
        return createNodeByTestTag(
            testTag = "${ItemScreenTags.SeriesStatusChip}_$status",
            block = block
        )
    }

    // Progress
    val progressTitle: KNode = createNode(ItemScreenTags.ProgressTitle)
    val progressInput: KNode = createNode(ItemScreenTags.ProgressInput)

    fun outlinedTextField(block: KOutlinedTextField.() -> Unit = {}): KOutlinedTextField {
        return createNodeByTestTag(
            testTag = ItemScreenTags.ProgressInput,
            block = block
        )
    }

    // Rating
    val ratingTitle: KNode = createNode(ItemScreenTags.RatingTitle)
    val ratingSlider: KNode = createNode(ItemScreenTags.RatingSlider)
    val ratingValue: KNode = createNode(ItemScreenTags.RatingValue)

    // Confirm button
    val confirmButton: KNode = createNode(ItemScreenTags.ConfirmButton)
}
