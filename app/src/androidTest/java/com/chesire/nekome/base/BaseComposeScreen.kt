package com.chesire.nekome.base

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import com.chesire.nekome.helpers.kNodes.KExtendedFabNode
import io.github.kakaocup.compose.node.builder.NodeMatcher
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

/**
 * Базовый класс для Page Object экранов.
 * Предоставляет обобщённый метод для создания кастомных нод.
 */
abstract class BaseComposeScreen<out T : ComposeScreen<T>>(
    val provider: SemanticsNodeInteractionsProvider
) : ComposeScreen<T>(provider) {

    /**
     * Создаёт обычный KNode по testTag.
     * 
     * @param testTag TestTag элемента
     * @return Созданный KNode
     */
    protected fun createNode(testTag: String): KNode {
        return KNode(
            semanticsProvider = provider,
            nodeMatcher = NodeMatcher(
                matcher = hasTestTag(testTag),
                useUnmergedTree = false
            )
        )
    }

    /**
     * Обобщённая функция для создания кастомных узлов по testTag.
     * Поддерживает кастомные типы нод (KNode, KExtendedFabNode, и т.д.).
     * 
     * @param testTag TestTag элемента
     * @param block DSL блок для настройки и взаимодействия с узлом
     * @return Созданный узел указанного типа
     */
    inline fun <reified T : KNode> createNodeByTestTag(
        testTag: String,
        block: T.() -> Unit = {}
    ): T {
        val node = when (T::class) {
            KNode::class -> KNode(
                semanticsProvider = provider,
                nodeMatcher = NodeMatcher(
                    matcher = hasTestTag(testTag),
                    useUnmergedTree = true
                )
            )
            KExtendedFabNode::class -> KExtendedFabNode(
                semanticsProvider = provider,
                nodeMatcher = NodeMatcher(
                    matcher = hasTestTag(testTag),
                    useUnmergedTree = true
                ),
                parentNode = null,
                parentTestTag = testTag // Передаём testTag для поиска дочерних элементов
            )
            else -> throw IllegalArgumentException("Unsupported node type: ${T::class}")
        } as T
        node.apply(block)
        return node
    }
}
