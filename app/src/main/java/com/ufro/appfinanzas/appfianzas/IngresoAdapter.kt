package com.ufro.appfinanzas.appfianzas

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

class IngresoAdapter(val ingresoList: ArrayList<Ingreso>): RecyclerView.Adapter<IngresoAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.txtComentarioIngresoRecycler?.text = ingresoList[position].comentario
        holder?.txtCantidadIngresoRecycler?.text = "$" + ingresoList[position].cantidad.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.ingreso_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return ingresoList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtComentarioIngresoRecycler = itemView.findViewById<TextView>(R.id.txtComentarioIngresoRecycler)
        val txtCantidadIngresoRecycler = itemView.findViewById<TextView>(R.id.txtCantidadIngresoRecycler)
    }

}