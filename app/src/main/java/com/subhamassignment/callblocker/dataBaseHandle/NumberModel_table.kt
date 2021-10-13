package com.subhamassignment.callblocker.dataBaseHandle

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "number_table")
data class NumberModel_table(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var name: String,
    var calltime: String,
    var number: String,
    var timescalled:String
)