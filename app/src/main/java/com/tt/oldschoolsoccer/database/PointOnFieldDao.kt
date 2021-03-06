package com.tt.oldschoolsoccer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 *interface for accessing database
 */
@Dao
interface PointOnFieldDao {

    @Insert
    suspend fun addPointOnField(pointOnField: PointOnField)

    @Query("SELECT * FROM pointOnField")
    suspend fun getAllPointsOnField() : List<PointOnField>

    @Update
    suspend fun updatePointOnField(pointOnField: PointOnField)

}