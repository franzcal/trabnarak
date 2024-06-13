package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.model.PagObraActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val senhaEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val buttonCadastro = findViewById<Button>(R.id.buttonCadastro)
        var view = findViewById<View>(android.R.id.content).rootView

        loginButton.setOnClickListener(){
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                showSnackbar(view, "Campos de Login vazio")
            }else{
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            Logar()
                        }
                    }

            }

        }

        buttonCadastro.setOnClickListener(){

            IrParaSegundaTela()
        }

    }

    private fun IrParaSegundaTela(){

        val segundaTela = Intent(this, Cadastro::class.java)
        startActivity(segundaTela)
    }

    private fun Logar() {
        val logar = Intent(this, MainActivity::class.java)
        logar.putExtra("mostrarBotaoAdicionar", true)
        startActivity(logar)
    }

    fun showSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

}