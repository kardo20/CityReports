package com.example.cityreport.api

import java.util.*

data class Report(
       val id_Report: Int,
       val id_tipoReport: Int,
       val id_cidade: Int,
       val id_utilizador: Int,
       val data: Date,
       val descricao: String,
       val imagem: String,
       val latitude: Double,
       val longitude: Double,
       val endereco: String
)
