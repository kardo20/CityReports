package com.example.cityreport.Notas.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cityreport.Notas.db.NotasDB
import com.example.cityreport.Notas.db.NotasRepository
import com.example.cityreport.Notas.entities.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotaViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: NotasRepository

    val allNotas: LiveData<List<Nota>>

    init {
        val notasDao = NotasDB.getDatabase(application, viewModelScope).NotaDao()
        repository = NotasRepository(notasDao)
        allNotas = repository.allNotas
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(nota)
    }

    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    // delete by id
    fun deleteByID(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByID(id)
    }

    fun updateNota(id: Int, nome: String, descricao: String) = viewModelScope.launch {
        repository.updateNota(id, nome, descricao)
    }

}