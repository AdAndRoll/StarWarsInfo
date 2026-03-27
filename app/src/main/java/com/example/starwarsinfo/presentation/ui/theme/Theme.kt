package com.example.starwarsinfo.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Цвета в стиле Star Wars
val SwYellow = Color(0xFFFFE81F) // Тот самый желтый из логотипа
val SwBlack = Color(0xFF000000)
val SwDarkGray = Color(0xFF1C1C1C)
val SwWhite = Color(0xFFFFFFFF)

private val StarWarsColorScheme = darkColorScheme(
    primary = SwYellow,
    secondary = SwWhite,
    tertiary = Color.Gray,
    background = SwBlack,
    surface = SwDarkGray,
    onPrimary = SwBlack,
    onSecondary = SwBlack,
    onBackground = SwWhite,
    onSurface = SwWhite
)

@Composable
fun StarWarsInfoTheme(
    // Для Star Wars лучше всего подходит темная тема по умолчанию
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    // Используем нашу кастомную схему
    val colorScheme = StarWarsColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Убедись, что файл Typography.kt существует
        content = content
    )
}