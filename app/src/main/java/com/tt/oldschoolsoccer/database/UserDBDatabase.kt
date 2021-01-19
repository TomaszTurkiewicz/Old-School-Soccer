package com.tt.oldschoolsoccer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [UserDB::class],
        version = 1
)
abstract class UserDBDatabase(): RoomDatabase() {

    abstract fun getUserDBDao(): UserDBDao

    companion object{
        @Volatile private var instance : UserDBDatabase ?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }


        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                UserDBDatabase::class.java,
                "user"
        ).build()


    }


}