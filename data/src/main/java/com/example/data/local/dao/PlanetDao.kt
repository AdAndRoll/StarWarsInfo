package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.PlanetDetailEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы с данными о планетах (вместо локаций) в базе данных Room.
 */
@Dao
interface PlanetDao {

    /**
     * Сохраняет или обновляет детальную информацию о планете.
     * Если запись с таким же ID уже существует, она будет заменена.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanet(planetDetail: PlanetDetailEntity)

    /**
     * Получает детальную информацию о планете по её String ID.
     * Использование Flow позволяет UI подписываться на изменения данных.
     *
     * @param planetId ID планеты (вырезанный из URL).
     * @return Flow с объектом [PlanetDetailEntity] или null.
     */
    @Query("SELECT * FROM planet_details WHERE id = :planetId")
    fun getPlanetById(planetId: String): Flow<PlanetDetailEntity?>

    // Для логики в Репозитории (one-shot)
    @Query("SELECT * FROM planet_details WHERE id = :id")
    suspend fun getPlanetByIdOnce(id: String): PlanetDetailEntity?

    /**
     * Удаляет все записи о планетах. Полезно при полной очистке кэша.
     */
    @Query("DELETE FROM planet_details")
    suspend fun clearAllPlanets()
}