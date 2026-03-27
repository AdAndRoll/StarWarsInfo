package com.example.starwarsinfo.presentation.character_list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search // Заменили Filter на Search
import androidx.compose.material3.CenterAlignedTopAppBar // Сделаем заголовок по центру
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListToolbar(
    title: String,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
    // В SWAPI у нас по сути только поиск, так что иконка Search будет логичнее
    filterIcon: ImageVector = Icons.Default.Search,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title.uppercase(), // В стиле системных заголовков
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary // Наш основной желтый
            )
        },
        actions = {
            IconButton(onClick = onFilterClick) {
                Icon(
                    imageVector = filterIcon,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        modifier = modifier,
    )
}