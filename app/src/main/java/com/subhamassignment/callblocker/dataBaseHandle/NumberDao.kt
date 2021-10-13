package com.subhamassignment.callblocker.dataBaseHandle

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NumberDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addnumber(number:NumberModel_table)

    @Query("DELETE FROM NUMBER_TABLE WHERE id =:id")
    fun deletenumber(id: Int)

    @Update
    fun updatedetail(number: NumberModel_table)

    @Query("select * from number_table order by id desc")
    fun getallnumber(): LiveData<List<NumberModel_table>>

    @Query("SELECT id FROM number_table WHERE number =:num")
    fun getid(num:String):Int

    @Query("SELECT timescalled FROM number_table WHERE number =:num")
    fun getnocalls(num:String):String

    @Query("UPDATE number_table SET calltime = :timed, timescalled= :noscalls WHERE ID =:ids")
    fun updatesecific(timed:String,noscalls:String,ids: Int)

    @Query("SELECT name FROM number_table WHERE number =:num")
    fun getname(num:String):String

    @Query("SELECT number FROM number_table WHERE number =:num")
    fun getnum(num:String):String

}