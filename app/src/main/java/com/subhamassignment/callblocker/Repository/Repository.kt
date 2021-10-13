package com.subhamassignment.callblocker.Repository

import androidx.lifecycle.LiveData
import com.subhamassignment.callblocker.dataBaseHandle.NumberDao
import com.subhamassignment.callblocker.dataBaseHandle.NumberModel_table

class Repository(var numberdao:NumberDao){

    val getallnumber:LiveData<List<NumberModel_table>> = numberdao.getallnumber()

    suspend fun insertnumber(number:NumberModel_table){
        numberdao.addnumber(number)
    }

    suspend fun updatedetail(number: NumberModel_table){
        numberdao.updatedetail(number)
    }

    suspend fun deletenumber(id:Int){
        numberdao.deletenumber(id)
    }

    suspend fun getid(number:String){
        numberdao.getid(number)
    }



}