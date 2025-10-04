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
 * Кастомный класс для работы с карточкой серии (SeriesItem).
 * Позволяет обращаться к внутренним элементам карточки.
 * 
 * Структура карточки:
 * - Постер (изображение слева)
 * - Заголовок (Title)
 * - Подтип и дата (Subtype + Date)
 * - Прогресс (Progress)
 * - Кнопка +1 (PlusOne button)
 * 
 * Использование:
 * ```kotlin
 * seriesItem(0) {
 *     assertIsDisplayed()
 *     title { assertTextEquals("Attack on Titan") }
 *     progress { assertTextEquals("1 / 25") }
 *     plusOneButton { performClick() }
 * }
 * ```
 */
class KSeriesItemNode(
    semanticsProvider: SemanticsNodeInteractionsProvider? = null,
    nodeMatcher: NodeMatcher,
    parentNode: BaseNode<*>? = null,
    private val parentTestTag: String = ""
) : KNode(semanticsProvider, nodeMatcher, parentNode) {

    private fun createChildMatcher(
        predicate: (SemanticsNode) -> Boolean,
        position: Int = 0
    ): NodeMatcher {
        // Создаём matcher для дочернего элемента карточки
        val combinedMatcher = if (parentTestTag.isNotEmpty()) {
            hasAnyAncestor(hasTestTag(parentTestTag)).and(
                SemanticsMatcher("SeriesItemChildMatcher", predicate)
            )
        } else {
            SemanticsMatcher("SeriesItemChildMatcher", predicate)
        }
        
        return NodeMatcher(
            matcher = combinedMatcher,
            position = position,
            useUnmergedTree = true
        )
    }

    /**
     * Постер серии (изображение слева).
     * Ищет элемент с ролью Image.
     */
    val poster: KNode by lazy {
        object : KNode(
            semanticsProvider = semanticsProvider,
            nodeMatcher = createChildMatcher(
                predicate = { node ->
                    node.config.getOrNull(SemanticsProperties.Role) == Role.Image &&
                            // Исключаем иконку кнопки +1
                            node.parent?.config?.getOrNull(SemanticsProperties.Role) != Role.Button
                }
            ),
            parentNode = this
        ) {}
    }

    /**
     * Заголовок серии.
     * Первый текстовый элемент в карточке.
     */
    val title: KNode by lazy {
        object : KNode(
            semanticsProvider = semanticsProvider,
            nodeMatcher = createChildMatcher(
                predicate = { node ->
                    val hasText = node.config.getOrNull(SemanticsProperties.Text)?.isNotEmpty() == true
                    val isNotButton = node.config.getOrNull(SemanticsProperties.Role) != Role.Button
                    
                    // Заголовок - это первый текстовый элемент
                    hasText && isNotButton && isTitleText(node)
                },
                position = 0
            ),
            parentNode = this
        ) {}
    }

    /**
     * Подтип и дата (например "TV   2013-04-07 to 2013-09-28").
     * Второй текстовый элемент в карточке.
     */
    val subtypeAndDate: KNode by lazy {
        object : KNode(
            semanticsProvider = semanticsProvider,
            nodeMatcher = createChildMatcher(
                predicate = { node ->
                    val hasText = node.config.getOrNull(SemanticsProperties.Text)?.isNotEmpty() == true
                    val isNotButton = node.config.getOrNull(SemanticsProperties.Role) != Role.Button
                    
                    hasText && isNotButton && !isTitleText(node) && !isProgressText(node)
                },
                position = 0
            ),
            parentNode = this
        ) {}
    }

    /**
     * Прогресс просмотра (например "1 / 25").
     */
    val progress: KNode by lazy {
        object : KNode(
            semanticsProvider = semanticsProvider,
            nodeMatcher = createChildMatcher(
                predicate = { node ->
                    val hasText = node.config.getOrNull(SemanticsProperties.Text)?.isNotEmpty() == true
                    val isNotButton = node.config.getOrNull(SemanticsProperties.Role) != Role.Button
                    
                    hasText && isNotButton && isProgressText(node)
                }
            ),
            parentNode = this
        ) {}
    }

    /**
     * Кнопка +1 для увеличения прогресса.
     * Может отсутствовать, если серия завершена.
     */
    val plusOneButton: KNode by lazy {
        object : KNode(
            semanticsProvider = semanticsProvider,
            nodeMatcher = createChildMatcher(
                predicate = { node ->
                    // Ищем кнопку с иконкой PlusOne
                    node.config.getOrNull(SemanticsProperties.Role) == Role.Button &&
                            node.config.getOrNull(SemanticsProperties.ContentDescription)
                                ?.any { it.contains("plus one", ignoreCase = true) } == true
                }
            ),
            parentNode = this
        ) {}
    }

    // Вспомогательные функции для определения типа текстового элемента
    private fun isTitleText(node: SemanticsNode): Boolean {
        // Заголовок обычно самый длинный текст и находится в начале
        val text = node.config.getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text ?: return false
        return text.length > 5 && !text.contains("/") && !text.matches(Regex(".*\\d{4}.*"))
    }

    private fun isProgressText(node: SemanticsNode): Boolean {
        // Прогресс содержит слэш (например "1 / 25")
        val text = node.config.getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text ?: return false
        return text.contains("/") || text.matches(Regex("\\d+\\s*/\\s*\\d+"))
    }
}

