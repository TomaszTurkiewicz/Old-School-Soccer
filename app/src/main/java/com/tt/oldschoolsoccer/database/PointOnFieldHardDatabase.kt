package com.tt.oldschoolsoccer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [PointOnField::class],
        version = 1
)
abstract class PointOnFieldHardDatabase() : RoomDatabase() {

    abstract fun getPointOnFieldDao() : PointOnFieldDao

    companion object{

        @Volatile private var instance : PointOnFieldHardDatabase ?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                PointOnFieldHardDatabase::class.java,
                "harddatabase"
        ).build()


    }

}