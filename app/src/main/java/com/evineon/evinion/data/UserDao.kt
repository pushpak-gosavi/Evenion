package com.evineon.evinion.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.evineon.evinion.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user:User)

    @Query ("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

    @Update
    suspend fun updateUser(user:User)

    @Delete
    suspend fun deleteUser(user:User)

    @Query ("Delete from user_table")
    suspend fun deleteAll()
}