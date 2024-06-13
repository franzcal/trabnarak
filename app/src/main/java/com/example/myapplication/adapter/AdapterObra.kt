package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Cadastro
import com.example.myapplication.R
import com.example.myapplication.model.Obra
import com.example.myapplication.model.PagObraActivity
import com.example.myapplication.model.PaginaObras
import java.io.ByteArrayOutputStream

class AdapterObra(private val context: Context, private val obras: MutableList<Obra>) : RecyclerView.Adapter<AdapterObra.ObraViewHolder>() {

    inner class ObraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome = itemView.findViewById<TextView>(R.id.nomeobra)
        val foto    = itemView.findViewById<ImageView>(R.id.fotoobra)
        val descricao = itemView.findViewById<TextView>(R.id.descricaoobra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObraViewHolder {
        val itemLista = LayoutInflater.from(context).inflate(R.layout.obras_item, parent, false)
        return ObraViewHolder(itemLista)
    }


    override fun getItemCount(): Int = obras.size

    override fun onBindViewHolder(holder: ObraViewHolder, position: Int) {

        val obra = obras[position]
        holder.foto.setImageBitmap(decodeIMg(obra.foto))
        holder.foto.setOnClickListener {
            val intent = Intent(holder.foto.context, PaginaObras::class.java)
            intent.putExtra("obra_id", obra.id) // Passando o ID da obra

            holder.foto.context.startActivity(intent)
        }

        holder.nome.text = obra.nome
        holder.descricao.text = obra.descricao
    }


    fun decodeIMg(stringImg:String?):Bitmap{

        var decodeString = Base64.decode(stringImg,Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(decodeString,Base64.DEFAULT,decodeString.size)

        return bitmap
    }
    fun setData(newObras: List<Obra>) {
        obras.clear()
        obras.addAll(newObras)
        notifyDataSetChanged()
    }

    fun addObra(obra: Obra) {
        obras.add(obra)
        notifyItemInserted(obras.size - 1)
    }

}

