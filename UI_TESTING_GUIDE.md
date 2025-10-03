# 🧪 Руководство по UI тестированию (Compose + Kaspresso)

## 📚 Оглавление

1. [Введение](#введение)
2. [Архитектура тестов](#архитектура-тестов)
3. [Быстрый старт](#быстрый-старт)
4. [Создание Page Objects](#создание-page-objects)
5. [Написание тестов](#написание-тестов)
6. [Лучшие практики](#лучшие-практики)
7. [Запуск тестов](#запуск-тестов)

---

## Введение

В проекте используется современный стек для UI тестирования Compose приложений:

### 🔧 Технологии

| Библиотека | Назначение |
|------------|------------|
| **Kaspresso** | Фреймворк для UI тестирования (надстройка над Espresso) |
| **Kakao Compose** | DSL для работы с Compose элементами |
| **Jetpack Compose Test** | Официальная библиотека для тестирования Compose |
| **Allure** | Красивые отчеты о тестировании |
| **Hilt** | Dependency Injection для тестов |
| **MockK** | Мокирование зависимостей |

### ✨ Преимущества

✅ **Надежность** - Kaspresso автоматически обрабатывает flaky тесты  
✅ **Читаемость** - DSL делает тесты понятными  
✅ **Page Object** - переиспользование кода  
✅ **Скриншоты** - автоматические скриншоты при падении  
✅ **Allure отчеты** - красивые отчеты с шагами  

---

## Архитектура тестов

```
app/src/androidTest/
├── java/com/chesire/nekome/
│   ├── base/                    # Базовые классы
│   │   ├── BaseComposeTest.kt        # Базовый класс для тестов
│   │   └── BaseComposeScreen.kt      # Базовый класс для Page Objects
│   ├── pageobjects/             # Page Objects (экраны)
│   │   ├── LoginScreen.kt
│   │   ├── SeriesListScreen.kt
│   │   └── ...
│   ├── tests/                   # Сами тесты
│   │   ├── LoginFlowComposeTest.kt
│   │   ├── SeriesListComposeTest.kt
│   │   └── ...
│   └── helpers/                 # Вспомогательные утилиты
│       └── TestHelpers.kt
```

### Паттерны

**1. Page Object Pattern** - каждый экран = отдельный класс  
**2. DSL** - chainable методы для читаемости  
**3. Test Case** - структурирование тестов с помощью `step()`  

---

## Быстрый старт

### 1. Создайте базовый тест

```kotlin
import com.chesire.nekome.base.BaseComposeTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MyFeatureTest : BaseComposeTest() {

    @Test
    fun testMyFeature() = run {
        step("Шаг 1: Открыть экран") {
            // Действия
        }
        
        step("Шаг 2: Проверить элемент") {
            // Проверки
        }
    }
}
```

### 2. Создайте Page Object

```kotlin
import com.chesire.nekome.base.BaseComposeScreen
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider

class MyScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<MyScreen>(semanticsProvider) {

    val button = createNode("MyButton")
    val textField = createNode("MyTextField")
}

// DSL функция
fun myScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
    block: MyScreen.() -> Unit
) = MyScreen(semanticsProvider).apply(block)
```

### 3. Используйте в тесте

```kotlin
@Test
fun myTest() = run {
    myScreen(composeTestRule) {
        button {
            flakySafely {
                assertIsDisplayed()
            }
            performClick()
        }
    }
}
```

---

## Создание Page Objects

### Базовая структура

```kotlin
class LoginScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<LoginScreen>(semanticsProvider) {

    // Простые элементы
    val usernameField = createNode("UsernameField")
    val passwordField = createNode("PasswordField")
    val loginButton = createNode("LoginButton")
    
    // Динамические элементы
    fun listItem(index: Int) = createNode("ListItem_$index")
    
    // Сложные элементы с дополнительной логикой
    fun verifyLoginForm() {
        usernameField { assertIsDisplayed() }
        passwordField { assertIsDisplayed() }
        loginButton { assertIsDisplayed() }
    }
}
```

### TestTags в UI коде

Добавьте `testTag` к Compose элементам:

```kotlin
@Composable
fun LoginScreen() {
    Column {
        TextField(
            modifier = Modifier.testTag("UsernameField"),
            value = username,
            onValueChange = { username = it }
        )
        
        Button(
            modifier = Modifier.testTag("LoginButton"),
            onClick = { /* ... */ }
        ) {
            Text("Login")
        }
    }
}
```

### Работа со списками

```kotlin
class SeriesListScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<SeriesListScreen>(semanticsProvider) {

    val list = createNode("SeriesList")
    
    // Доступ к элементам по индексу
    fun item(index: Int) = createNode("SeriesItem_$index")
    
    // Получить количество элементов
    fun getItemsCount() = getItemCountByPrefix("SeriesItem_")
    
    // Прокрутка к элементу
    fun scrollToItem(index: Int) {
        scrollTo("SeriesList", "SeriesItem_$index")
    }
}
```

---

## Написание тестов

### Структура теста с Kaspresso

```kotlin
@Test
fun completeUserFlow() = run {
    // before - выполняется до теста
    before {
        // Подготовка: моки, данные и т.д.
    }
    
    // Шаг 1
    step("Открыть экран логина") {
        launchActivity()
    }
    
    // Шаг 2
    step("Ввести данные") {
        loginScreen(composeTestRule) {
            usernameField {
                performTextInput("user@example.com")
            }
        }
    }
    
    // after - выполняется после теста
    after {
        // Очистка
    }
}
```

### flakySafely - надежные проверки

```kotlin
// Базовое использование (5 попыток, интервал 1с)
button {
    flakySafely {
        assertIsDisplayed()
    }
}

// С кастомным таймаутом
button {
    flakySafely(timeoutMs = 10000) {
        assertIsDisplayed()
        assertTextContains("Login")
    }
}
```

### Проверки

```kotlin
// Отображение
element { assertIsDisplayed() }
element { assertIsNotDisplayed() }

// Текст
element { assertTextEquals("Hello") }
element { assertTextContains("Hello", substring = true) }

// Состояние
element { assertIsEnabled() }
element { assertIsNotEnabled() }
element { assertIsSelected() }

// Кастомные проверки
element { 
    assert(condition) { "Custom error message" }
}
```

### Действия

```kotlin
// Клики
element { performClick() }
element { performLongClick() }

// Ввод текста
textField { performTextInput("Hello") }
textField { performTextClearance() }

// Прокрутка
scrollableElement { performScrollToNode(hasText("Target")) }

// Свайпы
element { performSwipeLeft() }
element { performSwipeRight() }
```

### Работа с диалогами

```kotlin
step("Открыть диалог") {
    dialogTriggerButton { performClick() }
}

step("Проверить диалог") {
    dialogScreen(composeTestRule) {
        title { assertTextEquals("Confirm") }
        message { assertTextContains("Are you sure?") }
        
        confirmButton { performClick() }
    }
}
```

---

## Лучшие практики

### ✅ Хорошо

```kotlin
// 1. Используйте Page Objects
loginScreen(composeTestRule) {
    loginButton { performClick() }
}

// 2. Структурируйте тесты с step()
@Test
fun userLogin() = run {
    step("Open login") { /* ... */ }
    step("Enter credentials") { /* ... */ }
    step("Verify success") { /* ... */ }
}

// 3. Используйте flakySafely для проверок
element {
    flakySafely {
        assertIsDisplayed()
    }
}

// 4. Давайте понятные имена testTag
Modifier.testTag("LoginButton")  // ✅ Хорошо
Modifier.testTag("btn1")          // ❌ Плохо

// 5. Один тест = одна функциональность
@Test
fun loginButton_whenClicked_navigatesToMainScreen()
```

### ❌ Плохо

```kotlin
// 1. Прямая работа с composeTestRule (без Page Object)
composeTestRule.onNodeWithTag("LoginButton").performClick()  // ❌

// 2. Тесты без структуры
@Test
fun test() {
    // 100 строк кода без step()
}

// 3. Проверки без flakySafely
element { assertIsDisplayed() }  // Может быть flaky

// 4. Хардкод задержек
Thread.sleep(5000)  // ❌ Никогда так не делайте!

// 5. Один тест проверяет все
@Test
fun testEverything() {
    // Логин + поиск + создание + удаление...
}
```

### 🎯 Что тестировать

**High Priority:**
- ✅ Критические user flows (логин, основные функции)
- ✅ Навигация между экранами
- ✅ Валидация форм
- ✅ Обработка ошибок

**Medium Priority:**
- ⚠️ Фильтрация и сортировка
- ⚠️ Поиск
- ⚠️ Диалоги и bottomsheets

**Low Priority:**
- ⭕ UI анимации (визуальные тесты)
- ⭕ Цвета и стили
- ⭕ Мелкие UI детали

---

## Запуск тестов

### Через Gradle

```bash
# Все UI тесты
./gradlew connectedDebugAndroidTest

# Конкретный класс
./gradlew connectedDebugAndroidTest \
  --tests com.chesire.nekome.tests.LoginFlowComposeTest

# Конкретный тест
./gradlew connectedDebugAndroidTest \
  --tests com.chesire.nekome.tests.LoginFlowComposeTest.successfulLogin_withPageObject

# С подробными логами
./gradlew connectedDebugAndroidTest --info

# С Allure отчетами
./gradlew connectedDebugAndroidTest allureReport
```

### Через Android Studio

1. Открыть файл теста
2. Нажать зеленую стрелку рядом с тестом
3. Выбрать "Run 'testName'"

### Отчеты

После запуска отчеты находятся в:
```
app/build/reports/androidTests/connected/index.html
```

---

## 🆘 Troubleshooting

### Тест не находит элемент

```kotlin
// 1. Проверьте testTag в UI
Modifier.testTag("MyButton")

// 2. Выведите semantic tree для отладки
printFullSemanticTree()

// 3. Используйте useUnmergedTree
createNode("MyButton", useUnmergedTree = true)
```

### Flaky тесты

```kotlin
// Используйте flakySafely
element {
    flakySafely(timeoutMs = 10000) {
        assertIsDisplayed()
    }
}
```

### Тест падает на CI но работает локально

```kotlin
// 1. Увеличьте таймауты
flakySafely(timeoutMs = 15000) { /* ... */ }

// 2. Отключите анимации в testOptions
android {
    testOptions {
        animationsDisabled = true
    }
}
```

---

## 📚 Дополнительные ресурсы

- [Kaspresso Docs](https://github.com/KasperskyLab/Kaspresso)
- [Kakao Compose](https://github.com/KakaoCup/Compose)
- [Compose Testing](https://developer.android.com/jetpack/compose/testing)
- [Allure Framework](https://docs.qameta.io/allure/)

---

## 🎯 Итог

Теперь у вас есть:
✅ Структура для UI тестов как в MyBroker  
✅ Базовые классы (BaseComposeTest, BaseComposeScreen)  
✅ Примеры Page Objects  
✅ Примеры тестов  
✅ Best practices  

**Следующие шаги:**
1. Добавьте testTags в ваш UI код
2. Создайте Page Objects для ваших экранов
3. Напишите тесты для критических user flows
4. Запустите тесты и проверьте отчеты

Удачного тестирования! 🚀


