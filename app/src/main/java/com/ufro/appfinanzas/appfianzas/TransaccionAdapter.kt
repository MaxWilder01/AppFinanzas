package com.ufro.appfinanzas.appfianzas

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class TransaccionAdapter(private val transaccionList: ArrayList<Transaccion>): RecyclerView.Adapter<TransaccionAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.txtComentarioIngresoRecycler?.text = transaccionList[position].comentario
        val cantidad = "$ ${transaccionList[position].cantidad}"
        holder?.txtCantidadIngresoRecycler?.text = cantidad

        if (transaccionList[position].tipo == "ingreso") {
            holder?.imgIngresoRecycler!!.setImageResource(R.drawable.ic_ingreso_web)
        } else {
            holder?.imgIngresoRecycler!!.setImageResource(R.drawable.ic_gasto_web)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.transaccion_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return transaccionList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtComentarioIngresoRecycler = itemView.findViewById<TextView>(R.id.txtComentarioIngresoRecycler)!!
        val txtCantidadIngresoRecycler = itemView.findViewById<TextView>(R.id.txtCantidadIngresoRecycler)!!
        val imgIngresoRecycler = itemView.findViewById<ImageView>(R.id.imgIngresoRecycler)!!
    }

}