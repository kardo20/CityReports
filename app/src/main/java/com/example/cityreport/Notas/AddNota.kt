package com.example.cityreport.Notas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.cityreport.R

class AddNota : AppCompatActivity(){

    private lateinit var noteName: EditText
    private lateinit var descricao: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nota)

        noteName = findViewById(R.id.input_note_name)
        descricao = findViewById(R.id.input_note_description)

        val button = findViewById<Button>(R.id.button_save_note)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(noteName.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_NOTENAME, noteName.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, descricao.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_NOTENAME = "com.example.android.notename.REPLY"
        const val EXTRA_REPLY_DESCRICAO = "com.example.android.descricao.REPLY"
    }
}