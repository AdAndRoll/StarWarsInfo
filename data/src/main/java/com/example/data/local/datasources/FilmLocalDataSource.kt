package com.example.data.local.datasources

import com.example.data.local.dao.FilmDao
import com.example.data.local.entity.SWFilmEntity
import javax.inject.Inject

/**
 * Локальный источник данных для работы с фильмами Star Wars в БД Room.
 */
class FilmLocalDataSource @Inject constructor(
    private val dao: FilmDao
) {
    /**
     * Получает фильм из локальной базы данных по его строковому ID.
     */
    suspend fun getFilm(filmId: String): SWFilmEntity? {
        return dao.getFilmById(filmId)
    }

    /**
     * Получает список фильмов из локальной базы данных по списку их ID.
     * Полезно для отображения списка фильмов на экране деталей персонажа.
     */
    suspend fun getFilms(filmIds: List<String>): List<SWFilmEntity> {
        return dao.getFilmsByIds(filmIds)
    }

    /**
     * Вставляет один фильм в локальную базу данных.
     */
    suspend fun insertFilm(film: SWFilmEntity) {
        dao.insertFilm(film)
    }

    /**
     * Вставляет список фильмов в локальную базу данных.
     */
    suspend fun insertFilms(films: List<SWFilmEntity>) {
        dao.insertAllFilms(films)
    }
}