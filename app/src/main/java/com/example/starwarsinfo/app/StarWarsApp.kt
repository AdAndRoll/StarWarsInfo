package com.example.starwarsinfo.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Главный класс приложения для Star Wars Info.
 * Инициирует генерацию кода Hilt для DI.
 */
@HiltAndroidApp
class StarWarsApp : Application() {
    // Здесь можно инициализировать логирование (например, Timber),
    // если решишь его добавить позже.
}