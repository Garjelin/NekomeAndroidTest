package com.chesire.nekome.helpers

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import io.github.kakaocup.compose.node.assertion.NodeAssertions

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

fun NodeAssertions.assertTextMatches(
    regex: Regex
) {
    val matcher = SemanticsMatcher("Text matches pattern '${regex.pattern}'") { node ->
        val textValues = node.config.getOrNull(SemanticsProperties.Text)
        val actualText = textValues?.joinToString("") { it.text } ?: ""
        
        regex.matches(actualText)
    }
    assert(matcher)
}
