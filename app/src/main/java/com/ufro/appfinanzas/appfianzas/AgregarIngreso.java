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

public class AgregarIngreso extends AppCompatDialogFragment implements View.OnClickListener{

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    EditText txtCantidadIngreso;
    EditText txtComentarioIngreso;
    Button btnAgregarIngreso;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios").child(mAuth.getCurrentUser().getUid()).child("ingresos");

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.agregar_ingreso_layout, null);

        txtCantidadIngreso = view.findViewById(R.id.txtCantidadIngreso);
        txtComentarioIngreso = view.findViewById(R.id.txtComentarioIngreso);
        btnAgregarIngreso = view.findViewById(R.id.btnAgregarIngreso);

        btnAgregarIngreso.setOnClickListener(this);
        builder.setView(view)
                .setTitle("Agregar Ingreso");

        return builder.create();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        String uid = (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getUid() : null;

        if (uid == null) return;

        switch (i) {
            case R.id.btnAgregarIngreso: {
                int cantidad = Integer.parseInt(txtCantidadIngreso.getText().toString());
                String comentario = txtComentarioIngreso.getText().toString();

                if (!Integer.toString(cantidad).equals("") && !comentario.equals("")) {
                    mDatabase.push().setValue(new Ingreso(cantidad, comentario));
                    this.dismiss();

                } else {
                    Toast.makeText(getActivity(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}