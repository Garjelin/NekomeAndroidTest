# Исправление проблемы с Layout Inspector

## Проблема
```
Warning: SDK processing. This version only understands SDK XML versions up to 3 
but an SDK XML file of version 4 was encountered.
```

Layout Inspector не показывает дерево элементов.

## Причина
**Android Gradle Plugin 8.2.2 (февраль 2024) устарел** и не поддерживает новый формат SDK XML v4, который используют новые SDK Tools (октябрь 2025).

## Решение

### Шаг 1: Обновить Android Gradle Plugin ✅ СДЕЛАНО

Обновлено в `gradle/libs.versions.toml`:

```toml
[versions]
android-gradle-plugin = "8.11.2"  # было 8.2.2
kotlin = "2.0.20"                 # было 1.9.22
# compose-compiler - удалена, теперь управляется плагином
sdk = "35"                        # было 34

[plugins]
compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

**Версия KSP**: `2.0.20-1.0.25` (автоматически совместима с Kotlin 2.0.20)

**ВАЖНО**: С Kotlin 2.0 требуется отдельный Compose Compiler плагин!
Плагин был добавлен во все модули с Compose:
- `app/build.gradle.kts`
- `core/compose/build.gradle.kts`
- `features/login/build.gradle.kts`
- `features/search/build.gradle.kts`
- `features/series/build.gradle.kts`
- `features/serieswidget/build.gradle.kts`
- `features/settings/build.gradle.kts`

### Шаг 2: Обновить Gradle Wrapper (опционально)

```bash
./gradlew wrapper --gradle-version 8.11.1
```

### Шаг 3: В Android Studio - обновить Command-line Tools

Вы уже установили нужные инструменты, но нужно убедиться:

**Settings → Languages & Frameworks → Android SDK → SDK Tools**

Установите/обновите:
- ✅ **Android SDK Command-line Tools (latest)** ← ГЛАВНОЕ!
- ✅ Android SDK Build-Tools (latest)
- ✅ Android SDK Platform-Tools (latest)
- ✅ Android Emulator (latest)

### Шаг 4: Очистить и пересобрать проект

```bash
./gradlew clean
rm -rf .gradle/
rm -rf build/
./gradlew build
```

### Шаг 5: Перезапустить Android Studio

После обновления AGP обязательно перезапустите Android Studio.

## ⚠️ КРИТИЧЕСКАЯ ПРОБЛЕМА С COMPOSE - ИСПРАВЛЕНО!

### Проблема: META-INF файлы исключались из APK

В `app/build.gradle.kts` была строка:
```kotlin
resources.excludes.add("META-INF/*")  // ❌ НЕПРАВИЛЬНО!
```

Это **удаляло ВСЕ файлы META-INF**, включая критически важные для Layout Inspector:
- `META-INF/androidx.compose.ui.version`
- `META-INF/androidx.compose.runtime.version`

**Без этих файлов Layout Inspector не может определить версию Compose и показывает пустое дерево!**

### ✅ Решение применено:

Теперь исключаются только конкретные проблемные файлы, а файлы версий Compose остаются:
```kotlin
packaging {
    resources.excludes.add("META-INF/AL2.0")
    resources.excludes.add("META-INF/LGPL2.1")
    // ... другие конкретные файлы
    // НЕ исключаем META-INF/androidx.compose.*.version!
}
```

## Для Layout Inspector с Jetpack Compose

### Требования для работы Layout Inspector:
1. **API Level 29+** на устройстве/эмуляторе
2. **Android Studio Iguana (2023.2.1) или новее**
3. Приложение запущено в **режиме Debug**
4. **META-INF/androidx.compose.*.version файлы НЕ исключены** ← ВАЖНО!

### Включить Live Updates:
1. View → Tool Windows → Layout Inspector
2. Запустить приложение на устройстве
3. Выбрать процесс: `com.chesire.nekome.debug`
4. Нажать на иконку **Live Updates** (🔄)

### Альтернатива для Compose - Semantics Tree

Если Layout Inspector все еще не работает, используйте встроенные инструменты Compose:

```kotlin
// В вашем UI тесте или в коде
composeTestRule.onRoot().printToLog("COMPOSE_TREE")

// Показать полное дерево
composeTestRule.onRoot(useUnmergedTree = true).printToLog("COMPOSE_TREE")
```

Затем смотрите в Logcat → фильтр "COMPOSE_TREE".

## Дополнительные команды для диагностики

```bash
# Проверить версию Gradle
./gradlew --version

# Проверить версию AGP
./gradlew buildEnvironment

# Проверить устройства
adb devices

# Логи Layout Inspector
adb logcat | grep "LayoutInspector"
```

## Если проблема осталась

### Вариант 1: Установить конкретную версию Command-line Tools
В Android Studio SDK Manager:
- Снимите галочку "Hide Obsolete Packages"
- Установите "Android SDK Command-line Tools" версии совместимой с AGP

### Вариант 2: Использовать старые Platform-Tools (не рекомендуется)
Откатиться на Platform-Tools 35.x, но лучше обновить AGP.

### Вариант 3: Использовать Compose @Preview вместо Layout Inspector
```kotlin
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NekomeTheme {
        LoginScreen()
    }
}
```

Preview работает без запуска приложения на устройстве!
