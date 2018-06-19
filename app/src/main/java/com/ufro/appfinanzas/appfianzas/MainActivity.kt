package com.ufro.appfinanzas.appfianzas

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var ingresosList: ArrayList<Ingreso>? = null
    private var mDatabase: DatabaseReference? = null
    private var adapter: IngresoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference.child("usuarios").child((mAuth.currentUser)!!.uid).child("ingresos")

        escucharIngresos()

        recyclerViewMain.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        ingresosList = ArrayList()

        adapter = IngresoAdapter(ingresosList!!)

        recyclerViewMain.adapter = adapter

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
                ingresosList!!.clear()
                for (data in dataSnapshot.children) {
                    val ingresoData = data.getValue<Ingreso>(Ingreso::class.java)
                    val ingreso = ingresoData?.let { it } ?: continue

                    ingresosList!!.add(ingreso)
                    Log.e("msj", "onDataChange: Message data is updated: " + ingreso.toString())
                }
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        mDatabase!!.addValueEventListener(escuchadorIngresos)
    }

    private fun abrirDialogoAgregar(opcion: Boolean) {
        val agregarIngresoDialog = AgregarIngreso()
        val agregarGastoDialog = AgregarGasto()

        if (opcion) {
            agregarIngresoDialog.show(supportFragmentManager, "Agregar Ingreso")
        } else {
            agregarGastoDialog.show(supportFragmentManager, "Agregar Gasto")
        }
    }
}
