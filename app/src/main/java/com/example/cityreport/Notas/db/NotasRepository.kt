package com.example.cityreport.Notas.db

import androidx.lifecycle.LiveData
import com.example.cityreport.Notas.dao.NotaDao
import com.example.cityreport.Notas.entities.Nota

class NotasRepository(private val NotaDao: NotaDao) {

    val allNotas: LiveData<List<Nota>> = NotaDao.getAllNotas()

    suspend fun insert(nota: Nota) {
        NotaDao.insert(nota)
    }

    suspend fun deleteAll(){
        NotaDao.deleteAll()
    }

    suspend fun deleteByID(id: Int){
        NotaDao.deleteByID(id)
    }

    suspend fun updateNota(id:Int, nome: String, descricao: String){
        NotaDao.updateNota(id, nome, descricao)
    }
}