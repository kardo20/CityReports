package com.example.cityreport

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.cityreport.api.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*

class AddReport: AppCompatActivity() , AdapterView.OnItemSelectedListener{
    var id_user : String? = null
    var lat : String? = null
    var long: String? = null
    var morada: String? = null
    private lateinit var tiposReport: List<tiposReport>
    private var arrayAdapter: ArrayAdapter<String>? = null
    val array = arrayOf("Buraco na estrada", "Tampa Saneamento Alta", "Óleo na Estrada")
    var tipo_selected: String? = null

    lateinit var imageView: ImageView
    lateinit var takeFoto: Button
    var image_uri: Uri? = null
    val REQUEST_IMAGE_CAPTURE = 1
    private val PERMISSION_REQUEST_CODE: Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)

        //Data right now
        val data = Calendar.getInstance().time

        //Get Tipos de Report Registados
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.gettiposReport()

        call.enqueue(object : Callback<List<tiposReport>> {
            override fun onFailure(call: Call<List<tiposReport>>, t: Throwable) {
                Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<tiposReport>>,
                response: Response<List<tiposReport>>
            ) {
                if (response.isSuccessful) {
                    tiposReport = response.body()!!
                    /*for (tipo in tiposReport) {

                    }*/
                }

            }
        })

        lat = intent.getStringExtra("lat")
        long = intent.getStringExtra("long")
        morada = intent.getStringExtra("morada")
        id_user = intent.getStringExtra("id")

        var txtLatitude = findViewById<TextView>(R.id.latitude)
        var txtLongitude = findViewById<TextView>(R.id.longitude)
        var endereco = findViewById<TextView>(R.id.morada)
        var txtData = findViewById<TextView>(R.id.rep_data)

        imageView = findViewById(R.id.imageView)
        takeFoto = findViewById(R.id.add_image)

        takeFoto.setOnClickListener(View.OnClickListener {
            if (checkPersmission()) takePicture() else requestPermission()
        })

        var btn_report = findViewById<Button>(R.id.btn_reportar)
        btn_report.setOnClickListener{
            report()
        }


        txtLatitude.setText(lat)
        txtLongitude.setText(long)
        endereco.setText(morada)
        txtData.setText(data.toString())

        val spinner: Spinner = findViewById(R.id.spinner)
        arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item,array)
        spinner?.adapter = arrayAdapter
        spinner?.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items:String = parent?.getItemAtPosition(position) as String
        Toast.makeText(applicationContext, "$items", Toast.LENGTH_SHORT).show()
        tipo_selected = items
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(applicationContext, "É obrigatório selecionar um tipo de Report", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePicture()
                } else {
                    Toast.makeText(this, "Permissão Bloqueada", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(image_uri)
        }
    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
    }


    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(this@AddReport.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun report(){
        var enderecoEdit = findViewById<TextView>(R.id.morada)
        var descricaoEdit = findViewById<EditText>(R.id.input_descricao)
        if(!descricaoEdit.text.isEmpty()){
            val imgBitmap: Bitmap = findViewById<ImageView>(R.id.imageView).drawable.toBitmap()
            val imageFile: File = convertBitmapToFile("file", imgBitmap)

            val imgFileRequest: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)
            val foto: MultipartBody.Part = MultipartBody.Part.createFormData("imagem", imageFile.name, imgFileRequest)

            var idteste :  String = "1"
            var idcidteste :  String = "1"
            var idtestetipo : String = "1"

            val id_utilizador: RequestBody = RequestBody.create(MediaType.parse("text/plain"), idteste)
            val id_cidade: RequestBody = RequestBody.create(MediaType.parse("text/plain"), idcidteste)
            val endereco: RequestBody = RequestBody.create(MediaType.parse("text/plain"), enderecoEdit.text.toString())
            val descricao: RequestBody = RequestBody.create(MediaType.parse("text/plain"), descricaoEdit.text.toString())
            val latitude: RequestBody = RequestBody.create(MediaType.parse("text/plain"), lat.toString())
            val longitude: RequestBody = RequestBody.create(MediaType.parse("text/plain"), long.toString())
            val tipo: RequestBody = RequestBody.create(MediaType.parse("text/plain"), idtestetipo)


            val request = ServiceBuilder.buildService(EndPoints::class.java)

            val call = request.report(id_utilizador, id_cidade, endereco, descricao, latitude, longitude, foto, tipo)

            call.enqueue(object : Callback<OutputPostReport> {

                override fun onResponse(call: Call<OutputPostReport>, response: Response<OutputPostReport>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@AddReport, "Report Efetuado com sucesso.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    Toast.makeText(this@AddReport, "FORA", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<OutputPostReport>, t: Throwable) {
                    Toast.makeText(this@AddReport, "Erro", Toast.LENGTH_SHORT).show()
                    Log.d("***CITYREPORT***", t.toString())
                }
            })
        }else{
            Toast.makeText(this@AddReport, "Não é possível ter campos vazios.", Toast.LENGTH_SHORT).show()
        }
    }
}