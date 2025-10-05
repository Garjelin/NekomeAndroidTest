# 🔧 Marathon Configuration Fix

## 🚨 Проблема
Marathon перестал запускаться с ошибкой:
```
UnrecognizedPropertyException: Unrecognized field "clearPackageData"
```

## 🔍 Причина
Опция `clearPackageData` не поддерживается в Marathon версии 0.6.5. Эта опция появилась в более новых версиях.

## ✅ Решение
Убрана неподдерживаемая опция из `Marathonfile.Local`:

### До:
```yaml
vendorConfiguration:
  # ...
  clearPackageData: true  # ❌ Не поддерживается в 0.6.5
  # ...
```

### После:
```yaml
vendorConfiguration:
  # ...
  # clearPackageData убрана
  # ...
```

## 🎯 Изоляция тестов
Вместо `clearPackageData` используется изоляция через `BaseComposeTest`:
- ✅ `ClearDatabaseRule` - очистка базы данных
- ✅ `ClearPreferencesRule` - очистка настроек
- ✅ `authProvider.logout()` - разлогин пользователя
- ✅ Автоматическая очистка в `@Before setUp()`

## ✅ Статус
- ✅ Marathon снова запускается
- ✅ Изоляция тестов работает через BaseComposeTest
- ✅ Конфигурация совместима с Marathon 0.6.5

## 🚀 Тестирование
```bash
./run_marathon.sh
```

Marathon должен запуститься без ошибок! 🎉
