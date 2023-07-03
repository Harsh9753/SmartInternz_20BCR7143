package com.example.remindmeahead.database

import android.provider.ContactsContract
import kotlinx.coroutines.flow.Flow

class repo(private val dbDao: dbDao) {
    suspend fun insertEvent(event: Event)=dbDao.insertEvent(event = event)

    suspend fun deletEvent(event: Event)=dbDao.deletEvent(event = event)

    suspend fun updateEvent(event: Event)=dbDao.updateEvent(event = event)

    fun getAll(): Flow<List<Event>> = dbDao.getAll()
    fun getById(eid:Int): Flow<List<Event>> = dbDao.getById(eid = eid)
    fun getByCat(category:String): Flow<List<Event>> = dbDao.getByCat(category = category)

}