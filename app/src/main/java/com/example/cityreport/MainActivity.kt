package com.example.cityreport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.cityreport.Notas.ALLNotes
import com.example.cityreport.api.EndPoints
import com.example.cityreport.api.ServiceBuilder
import com.example.cityreport.api.Utilizador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val go_notas = findViewById<Button>(R.id.btn_notas)
        go_notas.setOnClickListener{
            val go_notas_intent = Intent(this, ALLNotes::class.java)
            startActivity(go_notas_intent)
        }

        val go_login = findViewById<Button>(R.id.btn_entrar)
        go_login.setOnClickListener{
            val go_login_intent = Intent(this, Login::class.java)
            startActivity(go_login_intent)
        }
    }
}