package com.example.remindmeahead.database

import android.provider.ContactsContract
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface dbDao {
    //inserts
    @Upsert
    suspend fun insertEvent(event: Event)
    //Delete
    @Delete
    suspend fun deletEvent(event: Event)
    //update
    @Update
    suspend fun updateEvent(event: Event)
    //Query
    @Query("select * from eventTable")
    fun getAll():Flow<List<Event>>
    @Query("select * from eventTable where eid=:eid")
    fun getById(eid:Int):Flow<List<Event>>
    @Query("select * from eventTable where category=:category")
    fun getByCat(category:String):Flow<List<Event>>

}