package com.example.starwarsinfo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.starwarsinfo.R
import com.example.starwarsinfo.presentation.navigation.StarWarsNavHost
import com.example.starwarsinfo.presentation.ui.theme.StarWarsInfoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Используем новую тему Star Wars
            StarWarsInfoTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    // 2. Меняем фон на космический/звездный
                    Image(
                        painter = painterResource(id = R.drawable.star_wars_background),
                        contentDescription = "Галактический фон",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        // Делаем поверхность прозрачной, чтобы видеть звезды на фоне
                        color = Color.Black.copy(alpha = 0.4f)
                    ) {
                        // 3. Главный навигатор проекта
                        StarWarsNavHost()
                    }
                }
            }
        }
    }
}