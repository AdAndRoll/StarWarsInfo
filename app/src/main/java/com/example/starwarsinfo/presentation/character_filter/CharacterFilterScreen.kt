package com.example.starwarsinfo.presentation.character_filter

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.CharacterFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFilterScreen(
    onApplyFilter: (CharacterFilter) -> Unit,
    onDismiss: () -> Unit,
    initialName: String = "" // Можно передать текущий поиск, чтобы не вводить заново
) {
    var name by remember { mutableStateOf(initialName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Поиск") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя персонажа") },
                placeholder = { Text("Например: Luke Skywalker") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (name.isNotBlank()) {
                        IconButton(onClick = { name = "" }) {
                            Icon(Icons.Default.Close, "Очистить")
                        }
                    }
                }
            )

            Button(
                onClick = {
                    onApplyFilter(CharacterFilter(name = name.ifBlank { null }))
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Найти")
            }

            OutlinedButton(
                onClick = {
                    name = ""
                    onApplyFilter(CharacterFilter(name = null))
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сбросить поиск")
            }
        }
    }
}