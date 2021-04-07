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


class MainActivity : AppCompatActivity(), NotasAdapter.OnItemClickListener {

    private lateinit var notaViewModel: NotaViewModel
    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotasAdapter(this, this)
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

    override fun onItemClick(nota: Nota) {
        Toast.makeText(this, "${nota.id} - ${nota.nome} --> Clicada", Toast.LENGTH_SHORT).show()
        val edit_nota_activity = Intent(this@MainActivity, EditNota::class.java)

        edit_nota_activity.putExtra("nome", "${nota.nome}")
        edit_nota_activity.putExtra("descricao", "${nota.descricao}")
        edit_nota_activity.putExtra("id", nota.id)

        startActivityForResult(edit_nota_activity, 2)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //ADD NOTA
        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val new_nome = data?.getStringExtra(AddNota.EXTRA_REPLY_NOTENAME)
            val new_descricao = data?.getStringExtra(AddNota.EXTRA_REPLY_DESCRICAO)

            if (new_nome!= null && new_descricao != null) {
                val nota = Nota(nome = new_nome, descricao = new_descricao)
                notaViewModel.insert(nota)
            }

        } else if(requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
        // ----------------------------""--------------------------------

        //Edit Nota
        if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            val edited_nome = data?.getStringExtra(EditNota.EXTRA_REPLY_NEWNOTENAME)
            val edited_descricao = data?.getStringExtra(EditNota.EXTRA_REPLY_NEWDESCRICAO)
            val id = data?.getIntExtra(EditNota.EXTRA_REPLY_ID, 0)
            val codigo_apagar = data?.getIntExtra(EditNota.EXTRA_REPLY_COD_APAGAR, 0)

            //Editar campos da Nota
            if(id!=0 && id!=null && edited_nome!=null && edited_descricao!=null){
                notaViewModel.updateNota(id,edited_nome,edited_descricao)

                Toast.makeText(this, "Alteração efetuada com secesso!}", Toast.LENGTH_SHORT).show()
            }
            //Eliminar a Nota
            else if(id!=0 && id!=null && codigo_apagar == 20){
                notaViewModel.deleteByID(id)
                Toast.makeText(this, "Nota com o ID=${id} apagada com sucesso!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Erro na alteração de dados da nota!", Toast.LENGTH_SHORT).show()
            }
        }
        else if(requestCode == 2 && resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(applicationContext, "Nenhum campo foi alterado!", Toast.LENGTH_SHORT).show()
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