package br.com.wphec.sensors.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.wphec.sensors.database.converter.Converters
import br.com.wphec.sensors.database.dao.SensorDao
import br.com.wphec.sensors.database.dao.UserDao
import br.com.wphec.sensors.model.Sensor
import br.com.wphec.sensors.model.User

@Database(
    entities = [
        Sensor::class,
        User::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun SensorDao(): SensorDao

    abstract fun UserDao(): UserDao

    companion object {
        @Volatile
        private var db: AppDatabase? = null
        fun instance(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "sensor.db"
            ).addMigrations(MIGRATION_1_2)
                .build().also {
                db = it
            }
        }
    }
}