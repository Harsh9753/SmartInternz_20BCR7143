package com.example.remindmeahead.database

import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


interface viewAbstract{
    val allData:Flow<List<Event>>
    fun allCat(category: String):Flow<List<Event>>
    fun getById(eid:Int):Flow<List<Event>>

    fun addEvent(event: Event)

    fun deleteEvent(event: Event)

    fun updateEvent(event: Event)
}

@HiltViewModel
class MainViewModel
@Inject constructor(private val repo: repo):ViewModel(),viewAbstract
{
    private val ioScope= CoroutineScope(Dispatchers.IO)
    override val allData: Flow<List<Event>> = repo.getAll()
    override fun allCat(category: String): Flow<List<Event>> {
        return repo.getByCat(category)
    }
    override fun getById(eid:Int): Flow<List<Event>> {
        return repo.getById(eid)
    }

    override fun addEvent(event: Event) {
        ioScope.launch { repo.insertEvent(event = event) }
    }


    override fun deleteEvent(event: Event) {
        ioScope.launch { repo.deletEvent(event) }
    }


    override fun updateEvent(event: Event) {
        ioScope.launch { repo.updateEvent(event) }
    }
}
