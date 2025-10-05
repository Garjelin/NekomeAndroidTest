package com.chesire.nekome.helpers

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.KNode

/**
 * Extension функции для работы с LazyColumn и получения информации о коллекции через CollectionInfo.
 * 
 * Для использования этих функций, LazyColumn должен иметь:
 * 1. testTag в semantics
 * 2. collectionInfo в semantics с указанием rowCount
 * 
 * Пример настройки LazyColumn в UI:
 * ```kotlin
 * LazyColumn(
 *     modifier = Modifier
 *         .fillMaxSize()
 *         .semantics {
 *             testTag = "MyCollectionTag"
 *             collectionInfo = CollectionInfo(rowCount = items.size, columnCount = 1)
 *         }
 * ) {
 *     // items...
 * }
 * ```
 */

/**
 * Получить количество элементов в LazyColumn через CollectionInfo.
 * 
 * @param testTag тестовый тег LazyColumn
 * @return количество элементов (rowCount) или 0 если CollectionInfo не найден
 */
fun SemanticsNodeInteractionsProvider.getCollectionSize(testTag: String): Int {
    val node = onNode(
        hasTestTag(testTag),
        useUnmergedTree = true
    ).fetchSemanticsNode()
    
    val collectionInfo = node.config.getOrNull(SemanticsProperties.CollectionInfo)
    return collectionInfo?.rowCount ?: 0
}


/**
 * Проверить, что количество элементов в LazyColumn соответствует ожидаемому.
 * 
 * @param testTag тестовый тег LazyColumn
 * @param expectedCount ожидаемое количество элементов
 * @throws AssertionError если количество не соответствует
 */
fun SemanticsNodeInteractionsProvider.assertCollectionSize(testTag: String, expectedCount: Int) {
    val actualCount = getCollectionSize(testTag)
    assert(actualCount == expectedCount) {
        "Expected $expectedCount items in collection with tag '$testTag', but found $actualCount"
    }
}


/**
 * Проверить, что коллекция не пустая.
 * 
 * @param testTag тестовый тег LazyColumn
 * @throws AssertionError если коллекция пустая
 */
fun SemanticsNodeInteractionsProvider.assertCollectionNotEmpty(testTag: String) {
    val count = getCollectionSize(testTag)
    assert(count > 0) {
        "Expected collection with tag '$testTag' to be not empty, but it has $count items"
    }
}


/**
 * Проверить, что коллекция пустая.
 * 
 * @param testTag тестовый тег LazyColumn
 * @throws AssertionError если коллекция не пустая
 */
fun SemanticsNodeInteractionsProvider.assertCollectionEmpty(testTag: String) {
    val count = getCollectionSize(testTag)
    assert(count == 0) {
        "Expected collection with tag '$testTag' to be empty, but it has $count items"
    }
}

