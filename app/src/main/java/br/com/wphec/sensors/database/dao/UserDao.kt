package br.com.wphec.sensors.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.wphec.sensors.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun salva(user: User)

    @Query(
        """
        SELECT * FROM User 
        WHERE id = :userId 
        AND password = :password"""
    )
    suspend fun authentication(
        userId: String,
        password: String
    ): User?

    @Query("SELECT * FROM User WHERE id = :userId")
    fun searchById(userId: String): Flow<User>

}