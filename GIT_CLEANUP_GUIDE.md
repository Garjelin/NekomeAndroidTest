# 🧹 Руководство по очистке Git

## Проблема

В репозитории закоммичено **221+ мусорных файлов**:
- `.gradle/` - кеши Gradle (генерируются при сборке)
- `.idea/` - настройки IDE (у каждого разработчика свои)
- `build/reports/` - отчеты о сборке (временные)
- `configuration-cache/` - кеши конфигурации (временные)
- Бинарные файлы: `.apk`, `.dex`, `.class`

Эти файлы:
- ❌ Раздувают размер репозитория
- ❌ Создают конфликты при merge
- ❌ Меняются при каждой сборке
- ❌ НЕ нужны в git

## ✅ Решение

### Шаг 1: Создан .gitignore

Создан правильный `.gitignore` в корне проекта, который игнорирует:
- Gradle кеши (`.gradle/`)
- IDE настройки (`.idea/`, `*.iml`)
- Build артефакты (`build/`, `*.apk`, `*.dex`)
- OS файлы (`.DS_Store`, `Thumbs.db`)
- Логи и временные файлы

### Шаг 2: Удалить мусор из Git

Есть 2 варианта:

#### Вариант A: Автоматический (рекомендуется)

```bash
# Сделать скрипт исполняемым
chmod +x cleanup_git.sh

# Запустить очистку
./cleanup_git.sh

# Проверить что удалено
git status

# Закоммитить изменения
git add .gitignore
git commit -m "chore: add .gitignore and remove tracked build files"

# Push (если работаете один - можно с --force)
git push
```

#### Вариант B: Вручную

```bash
# Удалить из git индекса (но НЕ с диска!)
git rm -r --cached .gradle/
git rm -r --cached .idea/
git rm -r --cached build/reports/
git rm -r --cached build/configuration-cache/

# Добавить .gitignore
git add .gitignore

# Закоммитить
git commit -m "chore: add .gitignore and remove tracked build files"

# Push
git push
```

### Шаг 3: Проверка

После очистки:

```bash
# Должен показать только изменения в .gitignore
git status

# Проверить что мусор больше не отслеживается
git ls-files | grep -E "^\.(gradle|idea)" | wc -l
# Должно быть 0 или очень мало
```

## 🔄 Что происходит

1. **Файлы остаются на диске** - ничего не удаляется физически
2. **Git перестает отслеживать** - файлы больше не попадут в коммиты
3. **Старые коммиты остаются** - история не изменяется (это безопасно)
4. **Новые коммиты чистые** - только исходный код

## ⚠️ Если работаете в команде

1. **Предупредите команду** перед push
2. **Попросите всех сделать pull** после вашего коммита
3. **У каждого будет свой local.properties** - это нормально
4. **Никто не потеряет данные** - только git перестанет отслеживать мусор

## 📝 Что должно быть в git

### ✅ Должно быть:
- Исходный код (`.kt`, `.java`)
- Конфигурация (`.gradle.kts`, `gradle.properties`, `.toml`)
- Ресурсы (`.xml`, `.png`, `.webp`)
- Документация (`.md`, `LICENSE`)
- Gradle wrapper (`gradlew`, `gradle-wrapper.jar`)
- CI/CD конфигурация (`Dangerfile`, `fastlane/`)
- ProGuard правила (`proguard-rules.pro`)
- Тесты

### ❌ НЕ должно быть:
- `.gradle/` - кеши Gradle
- `.idea/` - настройки IDE
- `build/` - артефакты сборки
- `*.apk`, `*.dex`, `*.class` - скомпилированные файлы
- `local.properties` - локальные пути
- `*.keystore` - приватные ключи
- `*.log` - логи

## 🚀 После очистки

Преимущества:
- ✅ Быстрый clone репозитория
- ✅ Нет конфликтов в `.gradle/` или `build/`
- ✅ Чистая история git
- ✅ Меньше размер репозитория
- ✅ Легче review изменений

## 🆘 Если что-то пошло не так

### Отменить git rm (до коммита):
```bash
git reset HEAD .
```

### Восстановить файлы (если случайно удалили с диска):
```bash
./gradlew build
```

### Вернуть все как было:
```bash
git reset --hard HEAD
```

## 📚 Дополнительная информация

### Проверить размер репозитория:
```bash
git count-objects -vH
```

### Найти большие файлы в git:
```bash
git rev-list --objects --all | \
  git cat-file --batch-check='%(objecttype) %(objectname) %(objectsize) %(rest)' | \
  awk '/^blob/ {print substr($0,6)}' | \
  sort --numeric-sort --key=2 | \
  tail -20
```

### Полная очистка истории (ОПАСНО!):
```bash
# Только если ДЕЙСТВИТЕЛЬНО нужно очистить историю
# Это изменит все коммиты!
git filter-branch --force --index-filter \
  "git rm -r --cached --ignore-unmatch .gradle/ .idea/ build/" \
  --prune-empty --tag-name-filter cat -- --all

git push origin --force --all
```

## ✨ Итог

После выполнения этих шагов:
1. ✅ Создан правильный `.gitignore`
2. ✅ Удален мусор из git индекса
3. ✅ Обновлен README с инструкциями
4. ✅ Будущие коммиты будут чистыми!

