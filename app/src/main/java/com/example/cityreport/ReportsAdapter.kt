package com.example.cityreport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cityreport.api.Report
import java.util.*


class ReportsAdapter(val reports: List<Report>): RecyclerView.Adapter<ReportsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_reports, parent, false)
        return ReportsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) {
        return holder.bind(reports[position])
    }
}

class ReportsViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    private val endereco: TextView = itemView.findViewById(R.id.cidade)
    private val data: TextView= itemView.findViewById(R.id.data)
    private val descricao: TextView = itemView.findViewById(R.id.descricao)
   // private val imagem: ImageView = itemView.findViewById(R.id.imagem)

    fun bind(report: Report) {
        endereco.text = report.endereco
        //imagem. = report.imagem
        data.text = report.data.toString()
        descricao.text = report.descricao
    }

}