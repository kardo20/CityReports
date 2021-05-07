package com.example.cityreport

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cityreport.Notas.ALLNotes
import com.example.cityreport.Notas.AddNota
import com.example.cityreport.api.EndPoints
import com.example.cityreport.api.ServiceBuilder
import com.example.cityreport.api.Utilizador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login: AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_pass)

        val btn_entrar_login = findViewById<Button>(R.id.btn_entrar_login)
        btn_entrar_login.setOnClickListener{
            if(TextUtils.isEmpty(email.text) || TextUtils.isEmpty(password.text)){
                Toast.makeText(applicationContext, "Não pode deixar campos vazios!", Toast.LENGTH_SHORT).show()
            }
            else{
                checkLogin("${email.text}", "${password.text}")
            }
        }

    }


    fun checkLogin(email: String, pass: String){
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUserByEmail(email)

        call.enqueue(object : Callback<Utilizador> {
            override fun onFailure(call: Call<Utilizador>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Email introduzido, não se encontra registado!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(call: Call<Utilizador>, response: Response<Utilizador>) {
                if (response.isSuccessful) {
                    val u: Utilizador = response.body()!!
                    if (TextUtils.equals(u.password, pass)) {
                        Toast.makeText(
                            applicationContext,
                            "Bem-Vindo ${u.nome}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@Login, MapsActivity::class.java)
                        intent.putExtra("id", u.id_utilizador)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        Toast.makeText(applicationContext, "Password Incorreta!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}