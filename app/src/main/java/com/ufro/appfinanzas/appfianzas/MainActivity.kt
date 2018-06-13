package com.ufro.appfinanzas.appfianzas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.agregar_ingreso_layout.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
