package com.tt.oldschoolsoccer.database

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwnerInitializer
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [PointOnField::class],
        version = 1
)
abstract class PointOnFieldEasyDatabase () : RoomDatabase() {

    abstract fun getPointOnFiledDao() : PointOnFieldDao



    companion object{

        @Volatile private var instance : PointOnFieldEasyDatabase ?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                PointOnFieldEasyDatabase::class.java,
                "easydatabase"
        ).build()

        }



    }


// todo insert data to database
// todo update database
// todo read database