package com.chesire.nekome.helpers

/**
 * Enum с тестовыми пользователями для автоматизации.
 * 
 * @property login Email пользователя для авторизации
 * @property password Пароль пользователя
 */
enum class Users(val login: String, val password: String) {

    TEST_USER_1("testprofdepo@gmail.com", "Qwerty123"),
    TEST_USER_2("testprofdepo2@gmail.com", "Qwerty123"),
}

