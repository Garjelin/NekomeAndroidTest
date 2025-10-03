# Тесты для экрана списка серий (CollectionScreen)

## Обзор

Добавлены UI тесты для экрана `CollectionScreen` с использованием реальных testTag из производственного кода.

## Структура

### 1. TestTag в экране (`CollectionScreen.kt`)

В файле `/features/series/src/main/java/com/chesire/nekome/app/series/collection/ui/CollectionScreen.kt` определены testTag:

```kotlin
object SeriesCollectionTags {
    const val Root = "SeriesCollectionRoot"
    const val EmptyView = "SeriesCollectionEmptyView"
    const val RefreshContainer = "SeriesCollectionRefreshContainer"
    const val SearchFab = "SeriesCollectionSearchFab"
    const val SeriesItem = "SeriesCollectionSeriesItem"
    const val PlusOne = "SeriesCollectionPlusOne"
    const val Snackbar = "SeriesCollectionSnackbar"
    const val MenuFilter = "SeriesCollectionMenuFilter"
    const val MenuSort = "SeriesCollectionMenuSort"
    const val MenuRefresh = "SeriesCollectionRefresh"
}
```

### 2. Page Object (`SeriesListScreen.kt`)

Page Object для экрана списка серий:

```kotlin
class SeriesListScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<SeriesListScreen>(semanticsProvider) {

    val root: KNode = createNode(SeriesCollectionTags.Root)
    val filterButton: KNode = createNode(SeriesCollectionTags.MenuFilter)
    val sortButton: KNode = createNode(SeriesCollectionTags.MenuSort)
    val refreshButton: KNode = createNode(SeriesCollectionTags.MenuRefresh)
    val searchFab: KNode = createNode(SeriesCollectionTags.SearchFab)
    val refreshContainer: KNode = createNode(SeriesCollectionTags.RefreshContainer)
    val emptyView: KNode = createNode(SeriesCollectionTags.EmptyView)
    val snackbar: KNode = createNode(SeriesCollectionTags.Snackbar)
    
    fun seriesItem(): KNode = createNode(SeriesCollectionTags.SeriesItem)
    fun plusOneButton(): KNode = createNode(SeriesCollectionTags.PlusOne)
}
```

### 3. Тесты (`SeriesListComposeTest.kt`)

Набор UI тестов для проверки функциональности экрана:

#### Доступные тесты:

1. **`collectionScreen_displaysMainElements()`**
   - Проверяет отображение основных элементов: root, кнопки фильтра, сортировки, обновления

2. **`collectionScreen_displaysSeriesList()`**
   - Проверяет отображение контейнера списка
   - Проверяет наличие элементов серий в списке

3. **`filterButton_clickable()`**
   - Проверяет что кнопка фильтра кликабельна
   - Открывает диалог фильтра

4. **`sortButton_clickable()`**
   - Проверяет что кнопка сортировки кликабельна
   - Открывает диалог сортировки

5. **`refreshButton_clickable()`**
   - Проверяет что кнопка обновления кликабельна
   - Инициирует обновление списка

6. **`seriesItem_clickable()`**
   - Проверяет что элемент списка кликабелен
   - Открывает экран деталей серии

7. **`emptyList_displaysEmptyView()`**
   - Проверяет отображение empty view при пустом списке

8. **`searchFab_visibleWhenScrollingUp()`**
   - Проверяет видимость FAB для поиска

## Запуск тестов

### Запуск всех тестов в классе:
```bash
./gradlew :app:connectedDebugAndroidTest --tests "com.chesire.nekome.tests.SeriesListComposeTest"
```

### Запуск конкретного теста:
```bash
./gradlew :app:connectedDebugAndroidTest --tests "com.chesire.nekome.tests.SeriesListComposeTest.collectionScreen_displaysMainElements"
```

### Запуск через Android Studio:
1. Откройте `SeriesListComposeTest.kt`
2. Кликните на зеленую иконку ▶ рядом с именем класса или метода теста
3. Выберите "Run ..."

## Особенности реализации

### Использование testTag

В `CollectionScreen.kt` testTag добавляются через `semantics`:

```kotlin
IconButton(
    onClick = onFilterPressed,
    modifier = Modifier.semantics { testTag = SeriesCollectionTags.MenuFilter }
) {
    Icon(imageVector = Icons.Default.FilterAlt, ...)
}
```

### Работа со множественными элементами

Элементы списка имеют один и тот же testTag, поэтому для доступа к конкретному элементу используется:

```kotlin
composeTestRule.onAllNodesWithTag(SeriesCollectionTags.SeriesItem)[0].apply {
    assertIsDisplayed()
    performClick()
}
```

### Моки и тестовые данные

В тестах используются моки для `SeriesApi`:

```kotlin
@Before
fun setupMocks() {
    coEvery {
        seriesApi.retrieveAnime(any())
    } coAnswers {
        Ok(
            listOf(
                createSeriesDomain(title = "Test Anime 1"),
                createSeriesDomain(title = "Test Anime 2"),
                createSeriesDomain(title = "Test Anime 3")
            )
        )
    }
}
```

## Соответствие архитектуре MyBroker

Реализация следует тем же принципам:
- ✅ Page Object паттерн
- ✅ BaseComposeScreen для общей функциональности
- ✅ BaseComposeTest для базовой настройки тестов
- ✅ Использование Kaspresso для структурированных тестов
- ✅ Kakao Compose для DSL работы с Compose элементами
- ✅ Hilt для внедрения зависимостей в тесты
- ✅ MockK для мокирования API

## Дальнейшее развитие

### Добавление testTag в другие экраны:
1. Определите testTag в компоненте (например, в объекте `ScreenTags`)
2. Добавьте testTag к элементам через `Modifier.semantics { testTag = ... }`
3. Создайте Page Object для экрана
4. Напишите UI тесты

### Пример добавления testTag:
```kotlin
// 1. Определение в UI файле
object LoginTags {
    const val UsernameField = "LoginUsernameField"
    const val PasswordField = "LoginPasswordField"
    const val LoginButton = "LoginButton"
}

// 2. Использование в Compose
TextField(
    value = username,
    onValueChange = onUsernameChange,
    modifier = Modifier.semantics { testTag = LoginTags.UsernameField }
)

// 3. Page Object
class LoginScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<LoginScreen>(semanticsProvider) {
    
    val usernameField = createNode(LoginTags.UsernameField)
    val passwordField = createNode(LoginTags.PasswordField)
    val loginButton = createNode(LoginTags.LoginButton)
}

// 4. Тест
@Test
fun login_withValidCredentials_success() = run {
    step("Ввести логин и пароль") {
        LoginScreen(composeTestRule) {
            usernameField.performTextInput("user@test.com")
            passwordField.performTextInput("password123")
            loginButton.performClick()
        }
    }
}
```

## Ресурсы

- [UI_TESTING_GUIDE.md](UI_TESTING_GUIDE.md) - Полное руководство по UI тестированию
- [TESTING_SETUP_SUMMARY.md](TESTING_SETUP_SUMMARY.md) - Краткое описание настройки тестов
- [Kaspresso Documentation](https://kasperskylab.github.io/Kaspresso/)
- [Compose Testing Documentation](https://developer.android.com/jetpack/compose/testing)


