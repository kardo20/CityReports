package com.example.cityreport.Notas.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cityreport.Notas.entities.Nota

@Dao
interface NotaDao {

    @Query("SELECT * from nota_table ORDER BY nome ASC")
    fun getAllNotas(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("DELETE FROM nota_table where id == :id")
    suspend fun deleteByID(id: Int)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()

    @Query("UPDATE nota_table SET nome=:nome, descricao=:descricao  WHERE id == :id")
    suspend fun updateNota(id: Int, nome: String, descricao: String)

}