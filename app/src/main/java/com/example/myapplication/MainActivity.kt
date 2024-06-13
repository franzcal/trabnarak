package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AdapterObra
import com.example.myapplication.model.Obra
import com.example.myapplication.model.PaginaObras
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerviewObras = findViewById<RecyclerView>(R.id.recyclerview_obras)
        recyclerviewObras.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerviewObras.setHasFixedSize(true)

        val db = FirebaseFirestore.getInstance()
        val catalogoRef = db.collection("teste")
        val listaObras:  MutableList<Obra> = mutableListOf()
        val adapterObra = AdapterObra(this, listaObras)
        recyclerviewObras.adapter = adapterObra

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val mostrarBotaoAdicionar = intent.getBooleanExtra("mostrarBotaoAdicionar", false)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val buttonSair = findViewById<Button>(R.id.buttonDeslogar)
        val buttonQr = findViewById<Button>(R.id.buttonQr)

        val editRemove = intent.getBooleanExtra("editRemove", false)

        if (editRemove) {
            val logarCat = Intent(this, PaginaObras::class.java)
            logarCat.putExtra("editRemove", true)
        }

        var currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            buttonAdd.visibility = View.VISIBLE
            buttonLogin.visibility = View.INVISIBLE
            buttonSair.visibility = View.VISIBLE
            buttonQr.visibility = View.INVISIBLE
        } else {
            buttonAdd.visibility = View.INVISIBLE
            buttonLogin.visibility = View.VISIBLE
            buttonSair.visibility = View.INVISIBLE
            buttonQr.visibility = View.VISIBLE
        }

        buttonAdd.setOnClickListener(){
            val telaAddObra = Intent(this, AddObra::class.java)
            startActivity(telaAddObra)
        }

        buttonLogin.setOnClickListener(){
            val segundaTela = Intent(this, Login::class.java)
            startActivity(segundaTela)
        }

        buttonSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val segundaTela = Intent(this, MainActivity::class.java)
            startActivity(segundaTela)
        }

        buttonQr.setOnClickListener {

        }


        catalogoRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    val nome = document.getString("nome")
                    val descricao = document.getString("descricao")
                    val img = document.getString("imagem")

                    val obra = Obra(id.toString(),img, nome, descricao)

                    listaObras.add(obra)
                }

                adapterObra.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Erro ao obter documentos: ", exception)

                Toast.makeText(this@MainActivity, "Erro ao obter obras: ${exception.message}", Toast.LENGTH_SHORT).show()
            }



    }
}