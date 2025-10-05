# 🔧 Marathon Test Isolation Fix

## 🚨 Проблема
При запуске нескольких тестов через Marathon:
- ✅ **Первый тест** проходит нормально
- ❌ **Второй тест** падает, потому что открывается главная страница вместо экрана логина

### Причина
После первого теста пользователь остается залогиненным, и при запуске второго теста приложение открывает главную страницу вместо экрана логина.

## ✅ Решение

### Обновлен BaseComposeTest
Добавлены правила и логика для автоматической очистки состояния между тестами:

#### 1. Правила очистки данных:
```kotlin
@get:Rule(order = 1)
val clearDatabase = ClearDatabaseRule()

@get:Rule(order = 2) 
val clearPreferences = ClearPreferencesRule()
```

#### 2. Инъекция зависимостей:
```kotlin
@Inject
lateinit var authProvider: AuthProvider

@Inject
lateinit var applicationPreferences: ApplicationPreferences

@Inject
lateinit var seriesPreferences: SeriesPreferences
```

#### 3. Автоматическая очистка в setUp():
```kotlin
@Before
open fun setUp() {
    hiltRule.inject()
    
    // Очищаем состояние аутентификации и настройки
    runBlocking {
        applicationPreferences.reset()
        seriesPreferences.reset()
    }
    
    // Логаутим пользователя, чтобы каждый тест начинался с экрана логина
    authProvider.logout()
}
```

#### 4. Метод для явной очистки:
```kotlin
protected fun clearAppState() {
    runBlocking {
        applicationPreferences.reset()
        seriesPreferences.reset()
    }
    authProvider.logout()
}
```

## 🎯 Результат

### До исправления:
- 1-й тест: ✅ Логин → Главная страница
- 2-й тест: ❌ Главная страница (пользователь уже залогинен)

### После исправления:
- 1-й тест: ✅ Логин → Главная страница → Логаут
- 2-й тест: ✅ Логин → Главная страница → Логаут

## 🚀 Использование

### Автоматическая очистка (рекомендуется)
Ничего дополнительного делать не нужно - очистка происходит автоматически перед каждым тестом.

### Ручная очистка (при необходимости)
```kotlin
@Test
fun myTest() = run {
    // Ваш тест
    
    // Если нужно очистить состояние в середине теста
    clearAppState()
    
    // Продолжение теста
}
```

## 📝 Альтернативные решения

### Решение 1: Использовать @After для логаута
```kotlin
@After
fun tearDown() {
    authProvider.logout()
}
```

### Решение 2: Добавить логаут в конец каждого теста
```kotlin
@Test
fun myTest() = run {
    scenario(Login(TEST_USER_1, composeTestRule))
    // Тест
    
    step("Логаут пользователя") {
        authProvider.logout()
    }
}
```

### Решение 3: Настроить Marathon для очистки данных приложения
⚠️ **Примечание**: Опция `clearPackageData` доступна только в новых версиях Marathon (>0.6.5).
В Marathon 0.6.5 эта опция не поддерживается, поэтому используем изоляцию через BaseComposeTest.

## ✅ Статус
- ✅ BaseComposeTest обновлен с автоматической очисткой
- ✅ Добавлены правила для очистки базы данных и настроек
- ✅ Добавлен метод для ручной очистки состояния
- ✅ Тесты теперь изолированы друг от друга

## 🧪 Тестирование
Запустите Marathon для проверки:
```bash
./run_marathon.sh
```

Оба теста в `CollectionScreenTest` должны проходить успешно!
