package com.example.cityreport

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cityreport.R
import com.example.cityreport.adapters.NotasAdapter
import com.example.cityreport.entities.Nota
import com.example.cityreport.viewModel.NotaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var notaViewModel: NotaViewModel
    private val newWordActivityRequestCode = 1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotasAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        notaViewModel.allNotas.observe(this, Observer { notas ->
            // Update the cached copy of the words in the adapter.
            notas?.let { adapter.setNotas(it) }
        })

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNota::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val new_nome = data?.getStringExtra(AddNota.EXTRA_REPLY_NOTENAME)
            val new_descricao = data?.getStringExtra(AddNota.EXTRA_REPLY_DESCRICAO)

            if (new_nome!= null && new_descricao != null) {
                val nota = Nota(nome = new_nome, descricao = new_descricao)
                notaViewModel.insert(nota)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.apagartudo -> {
                cityViewModel.deleteAll()
                true
            }

            R.id.cidadesPortugal -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = CityAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.getCitiesByCountry("Portugal").observe(this, Observer { cities ->
                    // Update the cached copy of the words in the adapter.
                    cities?.let { adapter.setCities(it) }
                })

                true
            }

            R.id.todasCidades -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = CityAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.allCities.observe(this, Observer { cities ->
                    // Update the cached copy of the words in the adapter.
                    cities?.let { adapter.setCities(it) }
                })


                true
            }

            R.id.getCountryFromAveiro -> {
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.getCountryFromCity("Aveiro").observe(this, Observer { city ->
                    Toast.makeText(this, city.country, Toast.LENGTH_SHORT).show()
                })
                true
            }

            R.id.apagarAveiro -> {
                cityViewModel.deleteByCity("Aveiro")
                true
            }

            R.id.alterar -> {
                val city = City(id = 1, city = "xxx", country = "xxx")
                cityViewModel.updateCity(city)
                true
            }

            R.id.alteraraveiro -> {
                cityViewModel.updateCountryFromCity("Aveiro", "Japão")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }*/

}