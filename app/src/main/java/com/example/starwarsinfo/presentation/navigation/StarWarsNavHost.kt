package com.example.starwarsinfo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.starwarsinfo.presentation.character_detail.CharacterDetailScreen
import com.example.starwarsinfo.presentation.character_list.CharacterListScreen
import com.example.starwarsinfo.presentation.film_detail.FilmDetailScreen
import com.example.starwarsinfo.presentation.planet_detail.PlanetDetailScreen

@Composable
fun StarWarsNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "character_list"
    ) {
        // 1. Список персонажей
        composable(route = "character_list") {
            CharacterListScreen(
                onCharacterClick = { characterId ->
                    navController.navigate("character_detail/$characterId")
                }
            )
        }

        // 2. Детали персонажа
        composable(
            route = "character_detail/{characterId}",
            arguments = listOf(
                navArgument("characterId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId") ?: ""
            CharacterDetailScreen(
                characterId = characterId,
                onBackClick = { navController.popBackStack() },
                onCloseClick = {
                    navController.popBackStack("character_list", false)
                },
                onPlanetClick = { planetId ->
                    navController.navigate("planet_detail/$planetId")
                },
                onFilmClick = { filmId ->
                    navController.navigate("film_detail/$filmId")
                }
            )
        }

        // 3. Детали планеты (бывшая локация)
        composable(
            route = "planet_detail/{planetId}",
            arguments = listOf(
                navArgument("planetId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            PlanetDetailScreen(
                onBackClick = { navController.popBackStack() },
                onCloseClick = {
                    navController.popBackStack("character_list", false)
                },
                onCharacterClick = { characterId ->
                    navController.navigate("character_detail/$characterId")
                }
            )
        }

        // 4. Детали фильма (бывший эпизод)
        composable(
            route = "film_detail/{filmId}",
            arguments = listOf(
                navArgument("filmId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            FilmDetailScreen(
                onBackClick = { navController.popBackStack() },
                onCloseClick = {
                    navController.popBackStack("character_list", false)
                },
                onCharacterClick = { characterId ->
                    navController.navigate("character_detail/$characterId")
                }
            )
        }
    }
}