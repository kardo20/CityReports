package com.example.cityreport

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cityreport.api.EndPoints
import com.example.cityreport.api.Report
import com.example.cityreport.api.ServiceBuilder
import com.example.cityreport.api.Utilizador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportsUser: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports_user)

        val recyclerViewReports = findViewById<RecyclerView>(R.id.recyclerviewReports)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReportsByUser(1)

        call.enqueue(object : Callback<List<Report>> {
            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                if (response.isSuccessful) {
                    recyclerViewReports.apply{
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@ReportsUser)
                        adapter = ReportsAdapter(response.body()!!)
                    }

                }
            }
        }
        )


    }

}