# Тест-кейсы Nekome Android

Документация автоматизированных тест-кейсов для приложения Nekome.

## Структура

```
docs/
└── test-cases/
    ├── README.md                              # Этот файл
    ├── TC-001-Collection-Screen-Elements.md   # Проверка элементов экрана коллекции
    └── screenshots/                           # Скриншоты для документации
```

## Список тест-кейсов

### Smoke тесты

| ID | Название | Статус | Приоритет | Автоматизация |
|----|----------|---------|-----------|---------------|
| TC-001 | Проверка элементов экрана Collection | ✅ | Высокий | ✅ Автоматизирован |
| TC-002 | Обновление списка серий | 🔄 | Высокий | ✅ Автоматизирован |

### Планируемые тест-кейсы

| ID | Название | Статус | Приоритет |
|----|----------|---------|-----------|
| TC-003 | Фильтрация коллекции | 📋 Запланирован | Средний |
| TC-004 | Сортировка коллекции | 📋 Запланирован | Средний |
| TC-005 | Добавление новой серии через поиск | 📋 Запланирован | Высокий |
| TC-006 | Детальная информация о серии | 📋 Запланирован | Средний |
| TC-007 | Обновление прогресса просмотра | 📋 Запланирован | Высокий |

## Соответствие тест-кейсов и кода

| Тест-кейс | Тестовый класс | Метод |
|-----------|----------------|-------|
| TC-001 | `CollectionScreenTest.kt` | `checkAllCollectionScreenElements()` |
| TC-002 | `CollectionScreenTest.kt` | `refreshButtonUpdatesSeriesList()` |

## Используемые технологии

- **Kaspresso** - фреймворк для UI тестирования
- **Kakao Compose** - DSL для работы с Jetpack Compose
- **Hilt** - dependency injection для тестов
- **JUnit4** - test runner

## Структура автоматизации

```
app/src/androidTest/
├── java/com/chesire/nekome/
│   ├── base/
│   │   └── BaseComposeTest.kt           # Базовый класс для всех тестов
│   ├── helpers/
│   │   ├── Users.kt                     # Enum с тестовыми пользователями
│   │   └── scenario/
│   │       └── Login.kt                 # Сценарий авторизации
│   ├── pageobjects/
│   │   ├── LoginScreen.kt               # Page Object для экрана логина
│   │   └── CollectionScreen.kt          # Page Object для экрана коллекции
│   └── tests/
│       ├── LoginFlowComposeTest.kt      # Тесты авторизации
│       └── CollectionScreenTest.kt      # Тесты экрана коллекции
```

## Паттерны проектирования

### Page Object Pattern

Каждый экран приложения представлен Page Object классом:

```kotlin
class CollectionScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    BaseComposeScreen<CollectionScreen>(semanticsProvider) {
    
    val filterButton: KNode = createNode(SeriesCollectionTags.MenuFilter)
    val sortButton: KNode = createNode(SeriesCollectionTags.MenuSort)
    // ...
}
```

### Scenario Pattern

Повторяющиеся действия вынесены в сценарии:

```kotlin
class Login(
    private val user: Users,
    private val semanticsProvider: SemanticsNodeInteractionsProvider
) : Scenario()
```

Использование:
```kotlin
scenario(Login(TEST_USER_1, composeTestRule))
```

### DSL Style

Тесты используют DSL для читаемости:

```kotlin
CollectionScreen {
    filterButton {
        flakySafely(10_000) {
            assertIsDisplayed()
            assertIsEnabled()
        }
    }
}
```

## Запуск тестов

### Все тесты
```bash
./gradlew connectedDebugAndroidTest
```

### Конкретный тест
```bash
./gradlew connectedDebugAndroidTest --tests "com.chesire.nekome.tests.CollectionScreenTest.checkAllCollectionScreenElements"
```

### Конкретный класс
```bash
./gradlew connectedDebugAndroidTest --tests "com.chesire.nekome.tests.CollectionScreenTest"
```

## Тестовые данные

### Пользователи

Определены в `Users.kt`:

```kotlin
enum class Users(val login: String, val password: String) {
    TEST_USER_1("testprofdepo@gmail.com", "Qwerty123"),
    TEST_USER_2("testprofdepo2@gmail.com", "Qwerty123"),
}
```

## Правила написания тестов

1. **Используйте Page Objects** - не обращайтесь к UI элементам напрямую
2. **Используйте Scenarios** - для повторяющихся действий (логин, навигация)
3. **Используйте flakySafely** - для устойчивости к временным задержкам
4. **Документируйте тесты** - каждый тест должен иметь комментарий с описанием
5. **Следуйте структуре** - тест-кейс в .md → автоматизация в .kt

## Отчеты

После запуска тестов отчеты доступны в:
```
app/build/reports/androidTests/connected/
```

## Контакты

- **Команда QA**: [qa@team.com](mailto:qa@team.com)
- **CI/CD**: Jenkins / GitHub Actions
- **Bug Tracker**: Jira / GitHub Issues

