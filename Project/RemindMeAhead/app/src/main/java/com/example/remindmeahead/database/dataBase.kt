package com.example.remindmeahead.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(converters::class)
abstract class dataBase :RoomDatabase(){
    abstract fun daos():dbDao

    companion object{
        private var INSTANCE:dataBase?=null
        fun getInstance(context: Context):dataBase{
            if (INSTANCE==null){
                INSTANCE= Room.databaseBuilder(
                    context,
                    dataBase::class.java,
                    "NoteDB"
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE as dataBase
        }
    }
}