             package com.example.cityreport.Notas

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cityreport.R

             class EditNota : AppCompatActivity() {

    private lateinit var noteName_lay: EditText
    private lateinit var descricao_lay: EditText

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        noteName_lay = findViewById(R.id.nome_nota)
        descricao_lay = findViewById(R.id.descricao_nota)

        //Ler dados passados da nota selecionada
        val nota_selecionada = intent.extras
        var nome: String? = nota_selecionada?.getString("nome")
        var descricao: String? = nota_selecionada?.getString("descricao")
        var id = nota_selecionada?.getInt("id")

        noteName_lay.setText(nome)
        descricao_lay.setText(descricao)


        //Botão clear text
        val btn_clear = findViewById<Button>(R.id.button_apagar)
        btn_clear.setOnClickListener{
            noteName_lay.setText("")
            descricao_lay.setText("")
        }
        //Botão save edited note
        val btn_save = findViewById<Button>(R.id.button_guardar)
        btn_save.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(noteName_lay.text) || TextUtils.isEmpty(descricao_lay.text)){
                Toast.makeText(applicationContext, "Não pode deixar campos vazios!", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.equals(nome,noteName_lay.text) && TextUtils.equals(descricao,descricao_lay.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
                finish()
            }
            else{
                replyIntent.putExtra(EXTRA_REPLY_NEWNOTENAME, noteName_lay.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_NEWDESCRICAO, descricao_lay.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_ID, id)
                setResult(Activity.RESULT_OK, replyIntent)

                finish()
            }
        }

        //Botão Eliminar Nota
        val btn_delete = findViewById<Button>(R.id.btn_delete_note)
        btn_delete.setOnClickListener{
            val replyIntent = Intent()
            val builder = AlertDialog.Builder(this)
            builder.setTitle(applicationContext.getString(R.string.Titulo_Dialogo))
            builder.setMessage(noteName_lay.text)
            builder.setPositiveButton(applicationContext.getString(R.string.sim), { dialogInterface: DialogInterface, i: Int ->
                replyIntent.putExtra(EXTRA_REPLY_ID, id)
                replyIntent.putExtra(EXTRA_REPLY_COD_APAGAR, 20)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            })
            builder.setNegativeButton(applicationContext.getString(R.string.cancelar), { dialogInterface: DialogInterface, i: Int ->

            })
            builder.show()
        }

    }


    companion object {
        const val EXTRA_REPLY_NEWNOTENAME = "com.example.android.NEWnotename.REPLY"
        const val EXTRA_REPLY_NEWDESCRICAO = "com.example.android.NEWdescricao.REPLY"
        const val EXTRA_REPLY_ID = "com.example.android.ID.REPLY"
        const val EXTRA_REPLY_COD_APAGAR = "com.example.android.Cod_Apagar.REPLY"
    }
}