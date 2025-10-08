#!/bin/bash

# Быстрый запуск Marathon (предполагает, что Marathon уже загружен)

MARATHON_VERSION="0.6.5"
MARATHON_DIR="marathon-${MARATHON_VERSION}"

if [ ! -d "$MARATHON_DIR" ]; then
    echo "Marathon не найден. Запустите сначала ./run_marathon.sh"
    exit 1
fi

# Собираем проект
echo "Собираем проект..."
./gradlew assembleDebug assembleDebugAndroidTest

if [ $? -ne 0 ]; then
    echo "Ошибка при сборке проекта"
    exit 1
fi

# Запускаем Marathon
echo "Запускаем Marathon тесты с аннотацией @Debug..."
"${MARATHON_DIR}/bin/marathon" -m Marathonfile.Local

echo "Marathon тесты завершены. Результаты в директории ./marathon"
