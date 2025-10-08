package com.chesire.nekome.helpers.kNodes

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import com.chesire.nekome.app.series.collection.ui.SeriesCollectionTags
import com.chesire.nekome.app.series.item.ui.ItemScreenTags
import io.github.kakaocup.compose.node.builder.NodeMatcher
import io.github.kakaocup.compose.node.core.BaseNode
import io.github.kakaocup.compose.node.element.KNode

/**
 * Кастомный класс для работы с компонентом OutlinedTextField.
 * Позволяет обращаться к внутренним элементам поля ввода, включая trailingIcon.
 *
 * Структура компонента:
 * - Поле ввода (OutlinedTextField)
 * - Текст trailingIcon (например, "/ 25")
 *
 * Использование:
 * ```kotlin
 * outlinedTextField(ItemScreenTags.ProgressInput) {
 *     assertIsDisplayed()
 *     input { assertTextEquals("3") }
 *     trailingIcon { assertTextEquals("/ 25") }
 * }
 * ```
 */
class KOutlinedTextField(
    semanticsProvider: SemanticsNodeInteractionsProvider? = null,
    nodeMatcher: NodeMatcher,
    parentNode: BaseNode<*>? = null,
    private val parentTestTag: String = ""
) : KNode(semanticsProvider, nodeMatcher, parentNode) {

    /**
     * Создаёт matcher для дочернего элемента OutlinedTextField.
     */
    private fun createChildMatcher(
        predicate: (SemanticsNode) -> Boolean,
        position: Int = 0
    ): NodeMatcher {
        val combinedMatcher = if (parentTestTag.isNotEmpty()) {
            hasAnyAncestor(hasTestTag(parentTestTag)).and(
                SemanticsMatcher("OutlinedTextFieldChildMatcher", predicate)
            )
        } else {
            SemanticsMatcher("OutlinedTextFieldChildMatcher", predicate)
        }

        return NodeMatcher(
            matcher = combinedMatcher,
            position = position,
            useUnmergedTree = true // Используем не объединённое дерево для доступа к trailingIcon
        )
    }

    /**
     * Текст в trailingIcon (например, "/ 25").
     * Использует contentDescription для поиска.
     */
    val trailingIcon: KNode by lazy {
        object : KNode(
            semanticsProvider = semanticsProvider,
            nodeMatcher = createChildMatcher(
                predicate = { node ->
                    node.config.getOrNull(SemanticsProperties.ContentDescription)
                        ?.contains(ItemScreenTags.ProgressOutOf) == true
                }
            ),
            parentNode = this
        ) {}
    }
}

