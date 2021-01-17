package com.evineon.evinion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.evineon.evinion.Constants.Companion.USER_DATABASE
import com.evineon.evinion.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase():RoomDatabase() {
    abstract fun userDao():UserDao
    companion object{
        @Volatile
        private var INSTANCE:UserDatabase?=null
        fun getDatabase(context:Context):UserDatabase{
            val tempInstance= INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    USER_DATABASE
                ).build()
                INSTANCE= instance
                return instance
            }
        }
    }
}