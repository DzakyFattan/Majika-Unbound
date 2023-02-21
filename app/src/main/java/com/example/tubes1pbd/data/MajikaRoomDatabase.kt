package com.example.tubes1pbd.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartEntity::class], version = 2, exportSchema = false)
abstract class MajikaRoomDatabase : RoomDatabase() {
    abstract val cartDao: CartDao

    companion object {
        @Volatile
        private var INSTANCE: MajikaRoomDatabase? = null

        fun getDatabase(context: Context): MajikaRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MajikaRoomDatabase::class.java,
                    "majika_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    // .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}