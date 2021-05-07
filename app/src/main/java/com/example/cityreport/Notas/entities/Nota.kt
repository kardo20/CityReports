package com.example.cityreport.Notas.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nota_table")

class Nota(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "descricao") val descricao: String
)