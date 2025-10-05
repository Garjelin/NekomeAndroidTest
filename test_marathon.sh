#!/bin/bash

# Тестовый скрипт для проверки настройки Marathon (без реального запуска)

echo "=== Проверка настройки Marathon ==="

# Проверяем наличие файлов конфигурации
echo "1. Проверка файлов конфигурации:"
if [ -f "Marathonfile.Local" ]; then
    echo "   ✅ Marathonfile.Local найден"
else
    echo "   ❌ Marathonfile.Local не найден"
    exit 1
fi

# Проверяем наличие аннотации
echo "2. Проверка аннотации @Debug:"
if [ -f "app/src/androidTest/java/com/chesire/nekome/helpers/annotations/Debug.kt" ]; then
    echo "   ✅ Аннотация @Debug создана"
else
    echo "   ❌ Аннотация @Debug не найдена"
    exit 1
fi

# Проверяем использование аннотации в тестах
echo "3. Проверка использования аннотации в тестах:"
if grep -q "@Debug" app/src/androidTest/java/com/chesire/nekome/tests/CollectionScreenTest.kt; then
    echo "   ✅ Аннотация @Debug используется в CollectionScreenTest"
else
    echo "   ❌ Аннотация @Debug не используется в тестах"
    exit 1
fi

# Проверяем наличие APK файлов
echo "4. Проверка APK файлов:"
APP_APK="./app/build/outputs/apk/debug/app-debug.apk"
TEST_APK="./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"

if [ -f "$APP_APK" ]; then
    echo "   ✅ Основной APK найден: $APP_APK"
else
    echo "   ❌ Основной APK не найден: $APP_APK"
    echo "   Запустите: ./gradlew assembleDebug"
fi

if [ -f "$TEST_APK" ]; then
    echo "   ✅ Тестовый APK найден: $TEST_APK"
else
    echo "   ❌ Тестовый APK не найден: $TEST_APK"
    echo "   Запустите: ./gradlew assembleDebugAndroidTest"
fi

# Проверяем Android SDK
echo "5. Проверка Android SDK:"
ANDROID_SDK="/Users/admin/Library/Android/sdk"
if [ -d "$ANDROID_SDK" ]; then
    echo "   ✅ Android SDK найден: $ANDROID_SDK"
else
    echo "   ❌ Android SDK не найден: $ANDROID_SDK"
    echo "   Обновите путь в Marathonfile.Local"
fi

echo ""
echo "=== Результат проверки ==="
echo "✅ Настройка Marathon завершена!"
echo ""
echo "Для запуска Marathon:"
echo "1. Подключите Android устройство или запустите эмулятор"
echo "2. Запустите: ./run_marathon.sh (первый раз)"
echo "3. Или: ./marathon_quick.sh (последующие запуски)"
echo ""
echo "Тесты с аннотацией @Debug будут выполнены через Marathon"
