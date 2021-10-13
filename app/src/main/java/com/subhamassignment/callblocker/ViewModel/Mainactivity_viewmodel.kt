package com.subhamassignment.callblocker.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.subhamassignment.callblocker.dataBaseHandle.NumberDatabase
import com.subhamassignment.callblocker.dataBaseHandle.NumberModel_table
import com.subhamassignment.callblocker.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Mainactivity_viewmodel(application: Application) : AndroidViewModel(application) {

    var getallnumber: LiveData<List<NumberModel_table>>
    var repository: Repository

    init {
        val dao = NumberDatabase.getdbinstance(application).Daocall()
        repository = Repository(dao)
        getallnumber = repository.getallnumber
    }

    suspend fun addnumber(number: NumberModel_table) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertnumber(number)
    }

    suspend fun deletenumber(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletenumber(id)
    }

    suspend fun updatedetails(number: NumberModel_table) = viewModelScope.launch(Dispatchers.IO) {
        repository.updatedetail(number)
    }


}