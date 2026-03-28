package com.example.starwarsinfo.presentation.character_list

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.starwarsinfo.presentation.character_filter.CharacterFilterScreen
import com.example.starwarsinfo.presentation.character_list.components.CharacterItem
import com.example.starwarsinfo.presentation.character_list.components.CharacterListToolbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (String) -> Unit,
    viewModel: CharactersViewModel = hiltViewModel()
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // Состояние для индикатора обновления (Swipe-to-Refresh)
    // Крутим лоадер сверху только если идет REFRESH и в списке уже есть данные
    val isRefreshing = characters.loadState.refresh is LoadState.Loading && characters.itemCount > 0
    val pullToRefreshState = rememberPullToRefreshState()

    // Логика задержки сообщения о пустом списке
    var canShowEmptyMessage by remember { mutableStateOf(false) }

    LaunchedEffect(characters.loadState, characters.itemCount) {
        Log.d("PagingUI", "📱 UI Update: Count=${characters.itemCount}, " +
                "Refresh=${characters.loadState.refresh}, " +
                "Append=${characters.loadState.append}")
    }

    LaunchedEffect(characters.loadState.refresh, characters.itemCount) {
        if (characters.loadState.refresh is LoadState.Loading || characters.itemCount > 0) {
            canShowEmptyMessage = false
        } else if (characters.loadState.refresh is LoadState.NotLoading && characters.itemCount == 0) {
            delay(500)
            canShowEmptyMessage = true
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CharacterListToolbar(
                title = "Star Wars Characters",
                onFilterClick = { showFilterSheet = true }
            )
        }
    ) { paddingValues ->
        // Добавляем PullToRefreshBox как основной контейнер контента
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { characters.refresh() },
            state = pullToRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Если в БД есть данные, LazyColumn ВСЕГДА остается в иерархии.
                if (characters.itemCount > 0) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            count = characters.itemCount,
                            key = characters.itemKey { it.id },
                            contentType = characters.itemContentType { "character" }
                        ) { index ->
                            val character = characters[index]
                            if (character != null) {
                                CharacterItem(
                                    name = character.name,
                                    height = character.height,
                                    mass = character.mass,
                                    gender = character.gender,
                                    onClick = { onCharacterClick(character.id) }
                                )
                            }
                        }

                        // Лоадер APPEND (догрузка внизу списка)
                        if (characters.loadState.append is LoadState.Loading) {
                            item(key = "append_loader") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp),
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }
                    }
                }

                // Состояния загрузки, когда список пуст (первый запуск или ошибка)
                val loadState = characters.loadState
                when {
                    loadState.refresh is LoadState.Loading && characters.itemCount == 0 -> {
                        CircularProgressIndicator()
                    }

                    loadState.refresh is LoadState.Error && characters.itemCount == 0 -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Ошибка загрузки",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Button(onClick = { characters.retry() }) {
                                Text("Повторить")
                            }
                        }
                    }

                    loadState.refresh is LoadState.NotLoading &&
                            characters.itemCount == 0 && canShowEmptyMessage -> {
                        Text(
                            text = "Персонажи не найдены",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState
        ) {
            CharacterFilterScreen(
                onApplyFilter = { newFilter ->
                    canShowEmptyMessage = false
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