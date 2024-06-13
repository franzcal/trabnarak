package com.example.myapplication


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.view.View
import com.google.android.material.snackbar.Snackbar


class EditarObras: AppCompatActivity() {
    lateinit var img : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_obra)

        var nomeObra = findViewById<EditText>(R.id.editTextNomeObra)
        var autor = findViewById<EditText>(R.id.editTextAutor)
        var ano = findViewById<EditText>(R.id.editTextAno)
        var btnSalvar = findViewById<Button>(R.id.buttonSalvar)
        var btnEditar = findViewById<Button>(R.id.buttonEditar)
        var btnRemover = findViewById<Button>(R.id.buttonRemover)

        img = findViewById<ImageView>(R.id.imageView)
        var view = findViewById<View>(android.R.id.content).rootView

        img.setOnClickListener {
            val OpenGalleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(OpenGalleryIntent, 1)

        }

        btnSalvar.setOnClickListener(){

            val obra = nomeObra.text.toString()
            val nomeAutor = autor.text.toString()
            val data = autor.text.toString()
            val imagem = img.drawable

            if (obra.isEmpty() || nomeAutor.isEmpty() || data.isEmpty() || imagem == null) {
                showSnackbar(view, "Prencha todos os campos")
            }else {
                Firebase.firestore.collection("teste")
                    .add(
                        mapOf(
                            "nome" to nomeObra.text.toString(),
                            "autor" to autor.text.toString(),
                            "descricao" to ano.text.toString(),
                            "imagem" to encodeImg()
                        )
                    )
            }
        }

        btnEditar.setOnClickListener {


        }

        btnRemover.setOnClickListener {


        }

        Firebase.firestore.collection("Obras")
            .document("SaFVp7x5ugJpyaHIHJVC").update(mapOf(
                "nomeObra" to "codigo fonte1",
                "autor" to "codigo fonte2",
                "descrição" to "codigo fonte 3"
            ))

    }

    fun encodeImg():String{
        // Verifica se a imagem foi definida
        if (img.drawable == null) {
            return "" // Retorna uma string vazia se não houver imagem
        }

        // Converte a imagem em um array de bytes
        val bitmap = (img.drawable as BitmapDrawable).bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // Codifica o array de bytes em Base64
        val encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)

        return encodedString
    }
    fun showSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // Check if data is null (for safety), and proceed if it's not null
            data?.data?.let { imageUri ->
                // Load the image using the URI
                val imageBitmap =
                    BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                // Set the bitmap to your ImageView
                img.setImageBitmap(imageBitmap)
            }
        }    }
}