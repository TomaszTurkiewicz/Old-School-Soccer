package com.tt.oldschoolsoccer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDBDao {

    @Update
    suspend fun updateUserInDB (userDB: UserDB)

    @Query("SELECT * FROM UserDB")
    suspend fun getAllUsers():List<UserDB>

    @Insert
    suspend fun addUser(userDB: UserDB)

    @Query("SELECT * FROM UserDB WHERE id LIKE :id")
    suspend fun getUser(id:String):UserDB
}