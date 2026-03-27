package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SWFilmEntity

/**
 * DAO для работы с данными о фильмах Star Wars (вместо Эпизодов).
 */
@Dao
interface FilmDao {

    /**
     * Вставляет один фильм в базу данных.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: SWFilmEntity)

    /**
     * Вставляет список фильмов (удобно при кэшировании всех фильмов персонажа).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFilms(films: List<SWFilmEntity>)

    /**
     * Получает фильм по его строковому ID (вырезанному из URL).
     */
    @Query("SELECT * FROM films WHERE id = :filmId")
    suspend fun getFilmById(filmId: String): SWFilmEntity?

    /**
     * Получает список фильмов по списку их ID.
     * Именно этот метод мы будем вызывать в репозитории, передавая список ID из CharacterDetails.
     */
    @Query("SELECT * FROM films WHERE id IN (:filmIds)")
    suspend fun getFilmsByIds(filmIds: List<String>): List<SWFilmEntity>

    /**
     * Очистка таблицы фильмов.
     */
    @Query("DELETE FROM films")
    suspend fun clearAllFilms()
}