package br.com.wphec.sensors.database.dao

import androidx.room.*
import br.com.wphec.sensors.model.Sensor
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorDao {

    @Query("SELECT * FROM Sensor")
    fun searchAll(): Flow<List<Sensor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg sensor: Sensor)

    @Delete
    suspend fun remove(sensor: Sensor)

    @Query("SELECT * FROM Sensor WHERE id = :id")
    fun searchById(id: Long): Flow<Sensor?>

}