# Marathon Test Runner

Этот проект настроен для запуска тестов с помощью Marathon с использованием аннотации `@Debug`.

## Настройка

1. **Аннотация @Debug**: Создана аннотация `com.chesire.nekome.helpers.annotations.Debug` для маркировки тестов
2. **Конфигурация**: Файл `Marathonfile.Local` содержит настройки Marathon
3. **Скрипты запуска**: Созданы скрипты для автоматической загрузки и запуска Marathon

## Использование

### Первый запуск (с загрузкой Marathon)
```bash
./run_marathon.sh
```

### Последующие запуски (быстрый запуск)
```bash
./marathon_quick.sh
```

### Ручной запуск (если Marathon уже установлен)
```bash
# Сборка проекта
./gradlew assembleDebug assembleDebugAndroidTest

# Запуск Marathon
marathon-0.6.5/bin/marathon -m Marathonfile.Local
```

## Добавление тестов

Чтобы тест запускался через Marathon, добавьте аннотацию `@Debug` к классу или методу:

```kotlin
import com.chesire.nekome.helpers.annotations.Debug

@Debug
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MyTestClass : BaseComposeTest() {
    
    @Test
    @Debug  // Можно также добавить к отдельным методам
    fun myTest() {
        // Тест
    }
}
```

## Результаты

Результаты тестов сохраняются в директории `./marathon/` и включают:
- HTML отчеты
- Allure результаты
- Логи выполнения
- Скриншоты (если включены)

## Конфигурация

Основные настройки в `Marathonfile.Local`:
- **Фильтрация**: Запускаются только тесты с аннотацией `@Debug`
- **Покрытие кода**: Включено
- **Allure**: Включен для детальной отчетности
- **Retry**: Отключен (no-retry)
- **Sharding**: 1 устройство

## Требования

- Android SDK установлен в `/Users/admin/Library/Android/sdk`
- Подключенное Android устройство или эмулятор
- Gradle для сборки проекта
- Интернет соединение для первой загрузки Marathon
