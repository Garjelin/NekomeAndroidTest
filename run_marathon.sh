#!/bin/bash

# Скрипт для запуска Marathon тестов с аннотацией @Debug

MARATHON_VERSION="0.6.5"
MARATHON_DIR="marathon-${MARATHON_VERSION}"
MARATHON_ZIP="${MARATHON_DIR}.zip"
MARATHON_URL="https://github.com/MarathonLabs/marathon/releases/download/${MARATHON_VERSION}/${MARATHON_ZIP}"

# Проверяем, существует ли директория Marathon
if [ ! -d "$MARATHON_DIR" ]; then
    echo "Загружаем Marathon ${MARATHON_VERSION}..."
    
    # Загружаем Marathon, если его нет
    if [ ! -f "$MARATHON_ZIP" ]; then
        curl -L -o "$MARATHON_ZIP" "$MARATHON_URL"
        if [ $? -ne 0 ]; then
            echo "Ошибка при загрузке Marathon"
            exit 1
        fi
    fi
    
    # Распаковываем архив
    unzip -q "$MARATHON_ZIP"
    if [ $? -ne 0 ]; then
        echo "Ошибка при распаковке Marathon"
        exit 1
    fi
    
    # Делаем исполняемым
    chmod +x "${MARATHON_DIR}/bin/marathon"
    
    echo "Marathon ${MARATHON_VERSION} успешно установлен"
fi

# Собираем проект перед запуском тестов
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
