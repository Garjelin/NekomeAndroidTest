package com.chesire.nekome.helpers

/**
 * Enum с тестовыми пользователями для автоматизации.
 * 
 * @property login Email пользователя для авторизации
 * @property password Пароль пользователя
 */
enum class Users(val login: String, val password: String) {

    /**
     * Дефолтный тестовый пользователь
     */
    DEFAULT_USER("testprofdepo@gmail.com", "Qwerty123"),

    /**
     * Второй тестовый пользователь (если нужен)
     */
    TEST_USER_2("test2@example.com", "Password123"),
}

