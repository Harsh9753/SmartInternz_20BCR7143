package com.example.remindmeahead.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "eventTable")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val eid:Int=0,
    val category:String,
    val fname:String,
    val lname:String,
    val note:String,
    val date: String,
    val toRemind: String,
    val notesToSend:String="",
    val number:String="",
    val sent:Boolean=false,
)

