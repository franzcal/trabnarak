package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class Cadastro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        val nomeUsuarioEditText = findViewById<EditText>(R.id.editTextNome)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val senhaEditText = findViewById<EditText>(R.id.editTextPassword)
        val cadastrarButton = findViewById<Button>(R.id.buttonCadastro)
        val loginButton = findViewById<Button>(R.id.buttonVoltar)
        var view = findViewById<View>(android.R.id.content).rootView

        cadastrarButton.setOnClickListener() {
            val nome = nomeUsuarioEditText.text.toString()
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                showSnackbar(view, "Campos de Cadastro vazio")
            } else {
                val email = emailEditText.text.toString()
                val senha = senhaEditText.text.toString()

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                        }
                    }
            }


        }

        loginButton.setOnClickListener{
            IrParaSegundaTela()
        }

    }
    fun showSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun IrParaSegundaTela(){
        val segundaTela = Intent(this, MainActivity::class.java)
        startActivity(segundaTela)
    }
}



