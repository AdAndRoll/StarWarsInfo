package com.example.starwarsinfo.presentation.character_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.SWCharacterDetailed
import com.example.starwarsinfo.presentation.character_detail.components.DetailRow
import com.example.starwarsinfo.presentation.character_detail.components.FilmList
import java.net.URLEncoder // 1. Добавляем импорт
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: String,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onPlanetClick: (String) -> Unit,
    onFilmClick: (String) -> Unit,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = characterId) {
        viewModel.loadCharacterDetails(characterId)
    }

    val state by viewModel.characterDetailState.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Досье персонажа") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = onCloseClick) {
                        Icon(Icons.Default.Close, "Закрыть")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            )
        }
    ) { paddingValues ->
        when (val currentState = state) {
            is CharacterDetailState.Loading -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is CharacterDetailState.Success -> {
                val data: SWCharacterDetailed = currentState.character

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = data.character.name.uppercase(),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    item { DetailRow("Рост", "${data.height} см") }
                    item { DetailRow("Дата рождения", data.birthYear) }
                    item { DetailRow("Цвет волос", data.hairColor) }
                    item { DetailRow("Цвет глаз", data.eyeColor) }

                    item {
                        DetailRow(
                            label = "Родная планета",
                            value = data.homeworld.name,
                            isLink = true,
                            onClick = {
                                // 2. Кодируем URL планеты
                                val encodedUrl = URLEncoder.encode(data.homeworld.url, StandardCharsets.UTF_8.toString())
                                onPlanetClick(encodedUrl)
                            }
                        )
                    }

                    item {
                        FilmList(
                            films = data.films,
                            onFilmClick = { filmUrl ->
                                // 3. Кодируем URL фильма
                                val encodedUrl = URLEncoder.encode(filmUrl, StandardCharsets.UTF_8.toString())
                                onFilmClick(encodedUrl)
                            }
                        )
                    }
                }
            }

            is CharacterDetailState.Error -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), Alignment.Center) {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}