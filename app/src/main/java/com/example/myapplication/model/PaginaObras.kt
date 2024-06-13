package com.example.myapplication.model

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.google.firebase.database.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.speech.tts.TextToSpeech
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.Cadastro
import com.example.myapplication.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.net.URL
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.io.ByteArrayInputStream
import java.util.Locale


class PaginaObras : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var database: FirebaseFirestore

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_obras)


        // Inicialize o Firestore
        database = FirebaseFirestore.getInstance()

        tts = TextToSpeech(this, this)

        // Referências aos elementos de layout
        val imageViewObra: ImageView = findViewById(R.id.imageViewObra)
        val textViewTituloObra: TextView = findViewById(R.id.textViewTituloObra)
        val textViewDescricaoObra: TextView = findViewById(R.id.textViewDescricaoObra)
        val buttonVoltar: Button = findViewById(R.id.buttonVoltar)
        val buttonEditar: Button = findViewById(R.id.buttonEditar)
        val buttonRemover: Button = findViewById(R.id.buttonRemover)
        val buttonAudio: Button = findViewById(R.id.buttonAudio)

        val editRemove = intent.getBooleanExtra("editRemove", false)
        if (editRemove) {
            buttonEditar.visibility = View.INVISIBLE
            buttonRemover.visibility = View.INVISIBLE
        }

        var currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            buttonEditar.visibility = View.INVISIBLE
            buttonRemover.visibility = View.INVISIBLE
        } else {
            buttonEditar.visibility = View.VISIBLE
            buttonRemover.visibility = View.VISIBLE
        }
        // Recuperando o ID do documento do catálogo passado através do Intent
        val obraId = intent.getStringExtra("obra_id")

        // Referência ao documento no Firestore onde os detalhes da obra são armazenados
        val obraRef = obraId?.let { database.collection("teste").document(obraId) }

        // Adicionar um listener para recuperar os detalhes da obra

        fun decodeImage(base64: String?): Bitmap? {
            if (base64.isNullOrEmpty()) return null

            val imageBytes = Base64.decode(base64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

        if (obraRef != null) {
            obraRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Recuperar os detalhes da obra
                        val fotoBase64 = document.getString("imagem")
                        val nome = document.getString("nome")
                        val descricao = document.getString("descricao")

                        // Decodificar a imagem do Base64
                        val bitmap = decodeImage(fotoBase64)

                        // Exibir os detalhes da obra nos elementos de layout correspondentes
                        imageViewObra.setImageBitmap(bitmap)
                        textViewTituloObra.text = nome
                        textViewDescricaoObra.text = descricao
                        // Exibir os detalhes da obra nos elementos de layout correspondentes
                        // Aqui você pode carregar a imagem da URL, definir o nome e a descrição nos TextViews, etc.
                    } else {
                        // Se o documento não existir, trate de acordo (por exemplo, exibindo uma mensagem de erro)
                    }
                }
                .addOnFailureListener { exception ->
                    // Em caso de erro ao recuperar os dados, você pode tratar isso de acordo
                }
        }

        // Adicione um listener de clique ao botão de voltar
        buttonVoltar.setOnClickListener {
            IrParaSegundaTela() // Encerre a atividade quando o botão de voltar for clicado
        }
        buttonEditar.setOnClickListener {
            val intent = Intent(this, EditarObraActivity::class.java)
            intent.putExtra("obra_id", obraId)
            intent.putExtra("nome", textViewTituloObra.text.toString())
            intent.putExtra("descricao", textViewDescricaoObra.text.toString())
            startActivityForResult(intent, 1)
        }
        buttonRemover.setOnClickListener {
            // Lógica para remover a obra
        }
        buttonRemover.setOnClickListener {
            val obraId = intent.getStringExtra("obra_id")

            obraId?.let { id ->
                val obraRef = database.collection("teste").document(id)

                obraRef.delete()
                    .addOnSuccessListener {
                        Log.d("PaginaObras", "Documento removido com sucesso!")

                    }
                    .addOnFailureListener { e ->
                        Log.w("PaginaObras", "Erro ao remover documento", e)
                    }
            } ?: run {
                Log.w("PaginaObras", "ID da obra é nulo ou vazio")
            }
        }
        buttonAudio.setOnClickListener {
            val descricao = textViewDescricaoObra.text.toString() // Obtém a descrição do TextView
            if (::tts.isInitialized) {
                val result = tts.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
                } else {
                    tts.speak(descricao, TextToSpeech.QUEUE_FLUSH, null) // Passa a descrição para o método speak
                }
            } else {
                Toast.makeText(this, "Text-to-speech não está disponível.", Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun IrParaSegundaTela(){
        val segundaTela = Intent(this, MainActivity::class.java)
        startActivity(segundaTela)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Initialization failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.shutdown()
        super.onDestroy()
    }
}