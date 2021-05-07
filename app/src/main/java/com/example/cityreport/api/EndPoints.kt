package com.example.cityreport.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import retrofit2.Call

interface EndPoints {

    @GET("utilizador/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<Utilizador>

    @GET("reports/{id}")
    fun getReportsByUser(@Path("id") id: Int): Call<List<Report>>

    @GET("reports")
    fun getReports(): Call<List<Report>>

    @GET("tiposReport")
    fun gettiposReport(): Call<List<tiposReport>>

    @Multipart
    @POST("insere_report")
    fun report(
            @Part("id_utilizador") id_utilizador: RequestBody,
            @Part("id_cidade") id_cidade: RequestBody,
            @Part("endereco") endereco: RequestBody,
            @Part("descricao") descricao: RequestBody,
            @Part("latitude") latitude: RequestBody,
            @Part("longitude") longitude: RequestBody,
            @Part imagem: MultipartBody.Part,
            @Part("id_tipoReport") id_tipoReport: RequestBody
    ) : Call<OutputPostReport>
}