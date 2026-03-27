package com.example.starwarsinfo.presentation.character_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.starwarsinfo.presentation.character_filter.CharacterFilterScreen
import com.example.starwarsinfo.presentation.character_list.components.CharacterItem
import com.example.starwarsinfo.presentation.character_list.components.CharacterListToolbar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (String) -> Unit, // Теперь String ID
    viewModel: CharactersViewModel = hiltViewModel()
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val loadState = characters.loadState

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CharacterListToolbar(
                title = "Star Wars Characters",
                onFilterClick = { showFilterSheet = true }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Если есть хоть какие-то данные
            if (characters.itemCount > 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(characters.itemCount) { index ->
                        val character = characters[index]
                        if (character != null) {
                            CharacterItem(
                                name = character.name,
                                // Используем mass или species вместо birthYear,
                                // так как в твоем SWCharacter только они
                                birthYear = "Mass: ${character.mass}",
                                gender = character.gender,
                                onClick = {
                                    // Прямо используем character.id, так как он уже String
                                    onCharacterClick(character.id)
                                }
                            )
                        }
                    }

                    // Обработка дозагрузки (Paging)
                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(Modifier.fillMaxWidth().padding(16.dp), Alignment.Center) {
                                    CircularProgressIndicator(strokeWidth = 2.dp)
                                }
                            }
                        }
                        is LoadState.Error -> {
                            item {
                                Text("Ошибка при загрузке новых данных", color = MaterialTheme.colorScheme.error)
                            }
                        }
                        else -> {}
                    }
                }
            } else {
                // Состояния пустой страницы
                when (loadState.refresh) {
                    is LoadState.Loading -> CircularProgressIndicator()
                    is LoadState.Error -> {
                        val error = loadState.refresh as LoadState.Error
                        Text(
                            text = "Ошибка: ${error.error.localizedMessage}",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    is LoadState.NotLoading -> {
                        Text("Персонажи не найдены")
                    }
                }
            }
        }
    }

    // BottomSheet для поиска
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState
        ) {
            CharacterFilterScreen(
                onApplyFilter = { newFilter ->
                    viewModel.onFilterApplied(newFilter)
                    coroutineScope.launch {
                        sheetState.hide()
                        showFilterSheet = false
                    }
                },
                onDismiss = {
                    coroutineScope.launch {
                        sheetState.hide()
                        showFilterSheet = false
                    }
                }
            )
        }
    }
}