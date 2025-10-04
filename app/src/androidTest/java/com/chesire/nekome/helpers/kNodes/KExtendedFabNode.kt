package com.chesire.nekome.helpers.kNodes

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.builder.NodeMatcher
import io.github.kakaocup.compose.node.core.BaseNode
import io.github.kakaocup.compose.node.element.KNode

/**
 * Кастомный класс для работы с ExtendedFloatingActionButton.
 * Позволяет обращаться к внутренним элементам FAB (текст и иконка).
 * 
 * Использование:
 * ```kotlin
 * searchFab {
 *     assertIsDisplayed()
 *     text { assertTextEquals("Add new") }
 *     icon { assertContentDescriptionEquals("Search") }
 * }
 * ```
 */
class KExtendedFabNode(
    semanticsProvider: SemanticsNodeInteractionsProvider? = null,
    nodeMatcher: NodeMatcher,
    parentNode: BaseNode<*>? = null,
    private val parentTestTag: String = ""
) : KNode(semanticsProvider, nodeMatcher, parentNode) {

    private fun createChildMatcher(
        predicate: (SemanticsNode) -> Boolean,
        position: Int = 0
    ): NodeMatcher {
        // Создаём matcher, который ищет элемент с предикатом И который является потомком FAB
        val combinedMatcher = if (parentTestTag.isNotEmpty()) {
            hasAnyAncestor(hasTestTag(parentTestTag)).and(
                SemanticsMatcher("ChildMatcher", predicate)
            )
        } else {
            SemanticsMatcher("ChildMatcher", predicate)
        }
        
        return NodeMatcher(
            matcher = combinedMatcher,
            position = position,
            useUnmergedTree = true // Важно для доступа к дочерним элементам
        )
    }

    /**
     * Текстовый элемент внутри FAB.
     * Ищет дочерний Text composable.
     */
    val text: KNode by lazy {
        object : KNode(
            semanticsProvider = semanticsProvider,
            nodeMatcher = createChildMatcher(
                predicate = { node ->
                    // Ищем текстовый элемент
                    val hasText = node.config.getOrNull(SemanticsProperties.Text)?.isNotEmpty() == true
                    
                    // Проверяем, что это не contentDescription иконки
                    val isNotIconDescription = node.config.getOrNull(SemanticsProperties.Role) != Role.Image
                    
                    hasText && isNotIconDescription
                }
            ),
            parentNode = this
        ) {}
    }

    /**
     * Иконка внутри FAB.
     * Ищет дочерний Icon composable.
     */
    val icon: KNode by lazy {
        object : KNode(
            semanticsProvider = semanticsProvider,
            nodeMatcher = createChildMatcher(
                predicate = { node ->
                    // Ищем элемент с ролью Image (иконка)
                    node.config.getOrNull(SemanticsProperties.Role) == Role.Image
                }
            ),
            parentNode = this
        ) {}
    }
}

