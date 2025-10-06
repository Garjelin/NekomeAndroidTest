package com.chesire.nekome.base

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.chesire.nekome.helpers.kNodes.KExtendedFabNode
import com.chesire.nekome.helpers.kNodes.KOutlinedTextField
import com.chesire.nekome.helpers.kNodes.KSeriesItemNode
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
     * Поддерживает кастомные типы нод (KNode, KExtendedFabNode, KSeriesItemNode, и т.д.).
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
            KSeriesItemNode::class -> KSeriesItemNode(
                semanticsProvider = provider,
                nodeMatcher = NodeMatcher(
                    matcher = hasTestTag(testTag),
                    useUnmergedTree = true
                ),
                parentNode = null,
                parentTestTag = testTag
            )
            KOutlinedTextField::class -> KOutlinedTextField(
                semanticsProvider = provider,
                nodeMatcher = NodeMatcher(
                    matcher = hasTestTag(testTag),
                    useUnmergedTree = true
                ),
                parentNode = null,
                parentTestTag = testTag
            )
            else -> throw IllegalArgumentException("Unsupported node type: ${T::class}")
        } as T
        node.apply(block)
        return node
    }

    // Функция для печати семантического дерева по тегу
    fun printSemanticTreeByTag(
        tag: String,
        prefix: String = "PRINT_SEMANTIC_NODE"
    ) {
        val nodes = provider.onAllNodesWithTag(tag, useUnmergedTree = true)
        val nodeCount = nodes.fetchSemanticsNodes().size

        if (nodeCount == 0) {
            println("$prefix: No nodes found with tag '$tag'")
            return
        }

        nodes.apply {
            fetchSemanticsNodes().forEachIndexed { index, _ ->
                get(index).printToLog("${prefix}_${tag}_$index")
                get(index).onChildren().printToLog("${prefix}_CHILDREN-${tag}_$index")
            }
        }
    }

    fun printSemanticTreeByTagWithIndex(
        tag: String,
        position: Int = 0,
        prefix: String = "PRINT_SEMANTIC_NODE"
    ) {
        try {
            val nodes = provider.onAllNodesWithTag(tag, useUnmergedTree = true)
            val nodeCount = nodes.fetchSemanticsNodes().size

            if (position >= nodeCount) {
                println("$prefix: Position $position out of bounds (total nodes: $nodeCount)")
                return
            }

            nodes.get(position).printToLog("${prefix}_${tag}_pos$position")
            nodes.get(position).onChildren().printToLog("${prefix}_CHILDREN-${tag}_pos$position")

        } catch (e: Exception) {
            println("$prefix: Error printing node with tag '$tag' at position $position: ${e.message}")
        }
    }

    // Функция для печати полного семантического дерева
    fun printFullSemanticTree(prefix: String = "FULL_SEMANTIC_TREE") {
        provider.onRoot(useUnmergedTree = true)
            .printToLog("${prefix}_ROOT")
        provider.onRoot(useUnmergedTree = true)
            .onChildren()
            .printToLog("${prefix}_CHILDREN")
    }
}
