package com.example.expomanager.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Clase ExpoManagerDatabase que extiende RoomDatabase
// Define la base de datos de la aplicación y las entidades que contiene
@Database(entities = [EventEntity::class, CouponEntity::class], version = 5, exportSchema = false)
abstract class ExpoManagerDatabase : RoomDatabase() {
    // Métodos abstractos que proporcionan acceso a los DAOs
    abstract fun eventDao(): EventDao // Acceso a la tabla de eventos
    abstract fun couponDao(): CouponDao // Acceso a la tabla de cupones


    companion object {
        // Variable que mantiene una única instancia de la base de datos
        @Volatile
        private var INSTANCE: ExpoManagerDatabase? = null
        // Método para obtener la instancia de la base de datos (Singleton)
        fun getDatabase(context: Context): ExpoManagerDatabase {
            // Si la instancia ya existe, la retornamos
            return INSTANCE ?: synchronized(this) {
                // Si no existe, la creamos con Room
                val instance = Room.databaseBuilder( //room construye la base de datos
                    context.applicationContext,
                    ExpoManagerDatabase::class.java,// Clase de la base de datos
                    "event_database"// Nombre de la base de datos
                ).fallbackToDestructiveMigration()// Permite migraciones destructivas (elimina datos en cambios de versión
                    .build()
                // Guardamos la instancia creada
                INSTANCE = instance
                instance
            }
        }
    }
}
