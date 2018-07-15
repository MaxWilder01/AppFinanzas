package com.ufro.appfinanzas.appfianzas

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList


class TransaccionAdapter(private val items: MutableList<Transaccion>): RecyclerView.Adapter<TransaccionAdapter.ViewHolder>() {
    private var transaccionList: ArrayList<Transaccion> = ArrayList()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.txtComentarioIngresoRecycler?.text = transaccionList[position].comentario
        val cantidad = "$ ${String.format(Locale.US, "%,d", transaccionList[position].cantidad).replace(',', '.')}"
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

    fun add(transaccion: Transaccion) {
        transaccionList.add(transaccion)
        notifyDataSetChanged()
    }

    fun removeAt(posicion: Int) {
        val mAuth = FirebaseAuth.getInstance()

        FirebaseDatabase.getInstance()
                        .getReference("usuarios")
                        .child((mAuth.currentUser)!!
                        .uid)
                        .child("transacciones")
                        .child(transaccionList[posicion].id)
                        .removeValue()

        transaccionList.removeAt(posicion)

        notifyItemRemoved(posicion)
    }

    fun limpiar () {
        transaccionList.clear()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtComentarioIngresoRecycler = itemView.findViewById<TextView>(R.id.txtComentarioIngresoRecycler)!!
        val txtCantidadIngresoRecycler = itemView.findViewById<TextView>(R.id.txtCantidadIngresoRecycler)!!
        val imgIngresoRecycler = itemView.findViewById<ImageView>(R.id.imgIngresoRecycler)!!
    }
}