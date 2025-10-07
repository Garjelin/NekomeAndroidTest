package com.chesire.nekome.helpers

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import io.github.kakaocup.compose.node.assertion.NodeAssertions
import io.github.kakaocup.compose.node.element.KNode

//Проверяет, что текст элемента соответствует заданному regex паттерну.
//
//Пример использования:
//```kotlin
//progress {
//    assertIsDisplayed()
//    assertTextMatches(Regex("""\d+ / \d+""")) // проверка формата "число / число"
//}
//```
//
//@param regex паттерн для проверки текста

fun NodeAssertions.assertTextMatches(regex: Regex) {
    val matcher = SemanticsMatcher("Text matches pattern '${regex.pattern}'") { node ->
        // Проверяем EditableText для редактируемых полей
        val editableText = node.config.getOrNull(SemanticsProperties.EditableText)
        // Проверяем Text для обычных текстовых элементов
        val textValues = node.config.getOrNull(SemanticsProperties.Text)
        val actualText = when {
            editableText != null -> editableText
            textValues != null -> textValues.joinToString("") { it.text }
            else -> ""
        }
        regex.matches(actualText)
    }
    assert(matcher)
}

/**
 * Получает текст из Compose элемента.
 * 
 * Пример использования:
 * ```kotlin
 * val titleText = title.getText()
 * // или с проверкой на null:
 * val titleText = title.getText() ?: "Default text"
 * ```
 * 
 * @return текст элемента или null, если текст отсутствует
 */
fun KNode.getText(): String {
    var result: String? = null
    val matcher = SemanticsMatcher("Get text") { node ->
        result = node.config.getOrNull(SemanticsProperties.Text)
            ?.firstOrNull()
            ?.text
        true // всегда возвращаем true, чтобы не вызвать ошибку assert
    }
    try {
        assert(matcher)
    } catch (e: Exception) {
        // Игнорируем ошибки, нам нужен только текст
    }
    return result ?: "" // Возвращаем пустую строку, если текст null
}
