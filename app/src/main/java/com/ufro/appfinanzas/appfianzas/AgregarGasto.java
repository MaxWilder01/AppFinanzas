package com.ufro.appfinanzas.appfianzas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgregarGasto extends AppCompatDialogFragment implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private EditText txtCantidadGasto;
    private EditText txtComentarioGasto;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios").child((mAuth.getCurrentUser()).getUid());

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.agregar_gasto_layout, null);

        txtCantidadGasto = view.findViewById(R.id.txtCantidadGasto);
        txtComentarioGasto = view.findViewById(R.id.txtComentarioGasto);
        Button btnAgregarGasto = view.findViewById(R.id.btnAgregarGasto);

        btnAgregarGasto.setOnClickListener(this);

        builder.setView(view)
                .setTitle("Agregar Gasto");

        return builder.create();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.btnAgregarGasto: {
                int cantidad = Integer.parseInt(txtCantidadGasto.getText().toString());
                String comentario = txtComentarioGasto.getText().toString();

                if (!Integer.toString(cantidad).equals("") && !comentario.equals("")) {
                    mDatabase.child("transacciones").push().setValue(new Transaccion(cantidad, comentario, "gasto"));

                    this.dismiss();

                } else {
                    Toast.makeText(getActivity(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}