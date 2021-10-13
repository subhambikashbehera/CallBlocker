package com.subhamassignment.callblocker.dataBaseHandle

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [NumberModel_table::class], version = 1, exportSchema = false)
abstract class NumberDatabase:RoomDatabase() {

    abstract fun Daocall():NumberDao

    companion object {
        @Volatile
        var dbinstance: NumberDatabase? = null
        fun getdbinstance(Context: Context):NumberDatabase {

            if (dbinstance == null) {
                synchronized(this){
                    dbinstance = Room.databaseBuilder(
                        Context.applicationContext,
                        NumberDatabase::class.java,
                        "NUMBER_DB"
                    ).fallbackToDestructiveMigration().build()
                }
            }

            return dbinstance!!
        }
    }



}