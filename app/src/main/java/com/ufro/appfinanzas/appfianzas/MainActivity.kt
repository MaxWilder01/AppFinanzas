package com.ufro.appfinanzas.appfianzas

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ufro.appfinanzas.appfianzas.R.id.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity (): AppCompatActivity(), View.OnClickListener {

    private var transaccionesList: ArrayList<Transaccion>? = null
    private var mDatabaseTransacciones: DatabaseReference? = null
    private var mDatabaseTotales: DatabaseReference? = null
    private var adapter: TransaccionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mAuth = FirebaseAuth.getInstance()
        mDatabaseTransacciones = FirebaseDatabase.getInstance().reference.child("usuarios").child((mAuth.currentUser)!!.uid).child("transacciones")
        mDatabaseTotales = FirebaseDatabase.getInstance().reference.child("usuarios").child((mAuth.currentUser)!!.uid).child("totales")

        escucharIngresos()

        recyclerViewMain.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        transaccionesList = ArrayList()

        adapter = TransaccionAdapter(transaccionesList as MutableList<Transaccion>)

        recyclerViewMain.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerViewMain.adapter as TransaccionAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerViewMain)

        btnAgregarIngresoMain.setOnClickListener(this)
        btnAgregarGastoMain.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val i = view!!.id

        when (i) {
            R.id.btnAgregarIngresoMain -> {
                abrirDialogoAgregar(true)
            }
            R.id.btnAgregarGastoMain -> {
                abrirDialogoAgregar(false)
            }
        }
    }

    private fun escucharIngresos() {
        val escuchadorIngresos = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val adapter = recyclerViewMain.adapter as TransaccionAdapter
                adapter.limpiar()
                // TODO: CLEAR LIST
                //transaccionesList!!.clear()
                var sumaIngresos = 0
                var sumaGastos = 0
                for (data in dataSnapshot.children) {
                    val transaccionData = data.getValue<Transaccion>(Transaccion::class.java)
                    val transaccion = transaccionData?.let { it } ?: continue

                    if (transaccion.tipo == "ingreso") {
                        sumaIngresos += transaccion.cantidad
                    } else {
                        sumaGastos += transaccion.cantidad
                    }

                    adapter.add(transaccion)
                    Log.e("msj", "onDataChange: Message data is updated: " + transaccion.toString())
                }
                val textoIngresos = "$ ${darFormatoNumero(sumaIngresos)}"
                val textoGastos = "$ ${darFormatoNumero(sumaGastos)}"
                val textoSaldo = "$ ${darFormatoNumero(sumaIngresos - sumaGastos)}"

                txtCantidadIngresosMain.text = textoIngresos
                txtCantidadGastosMain.text = textoGastos
                txtCantidadSaldoMain.text = textoSaldo

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        mDatabaseTransacciones!!.addValueEventListener(escuchadorIngresos)
    }

    private fun abrirDialogoAgregar(opcion: Boolean) {
        val agregarIngresoDialog = AgregarIngreso()
        val agregarGastoDialog = AgregarGasto()

        if (opcion) {
            agregarIngresoDialog.show(supportFragmentManager, "Agregar Transaccion")
        } else {
            agregarGastoDialog.show(supportFragmentManager, "Agregar Gasto")
        }
    }

    private fun darFormatoNumero(numero: Int): String {
        return String.format(Locale.US, "%,d", numero).replace(',', '.')
    }
}
