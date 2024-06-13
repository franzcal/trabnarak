package com.example.myapplication.model

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore

class EditarObraActivity : AppCompatActivity() {
    private lateinit var database: FirebaseFirestore
    private var obraId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_obra)

        // Inicialize o Firestore
        database = FirebaseFirestore.getInstance()

        // Referências aos elementos de layout
        val editTextNomeObra: EditText = findViewById(R.id.editTextNomeObra)
        val editTextDescricaoObra: EditText = findViewById(R.id.editTextDescricaoObra)
        val buttonSalvar: Button = findViewById(R.id.buttonSalvar)

        // Recuperar os dados passados pelo Intent
        obraId = intent.getStringExtra("obra_id")
        val nome = intent.getStringExtra("nome")
        val descricao = intent.getStringExtra("descricao")

        // Preencher os campos de input com os dados atuais
        editTextNomeObra.setText(nome)
        editTextDescricaoObra.setText(descricao)

        // Adicionar um listener ao botão salvar
        buttonSalvar.setOnClickListener {
            val novoNome = editTextNomeObra.text.toString()
            val novaDescricao = editTextDescricaoObra.text.toString()

            // Atualizar os dados no Firestore
            if (obraId != null) {
                val obraRef = database.collection("teste").document(obraId!!)
                obraRef.update("autor", novoNome, "descricao", novaDescricao)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Obra atualizada com sucesso", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao atualizar obra: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
