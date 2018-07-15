package com.ufro.appfinanzas.appfianzas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AgregarIngreso extends AppCompatDialogFragment implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private EditText txtCantidadIngreso;
    private EditText txtComentarioIngreso;
    private InformacionTotales informacionTotales;
    private int ingresoTotal;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios").child((mAuth.getCurrentUser()).getUid()).child("transacciones");

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.agregar_ingreso_layout, null);

        txtCantidadIngreso = view.findViewById(R.id.txtCantidadIngreso);
        txtComentarioIngreso = view.findViewById(R.id.txtComentarioIngreso);
        Button btnAgregarIngreso = view.findViewById(R.id.btnAgregarIngreso);

        btnAgregarIngreso.setOnClickListener(this);

        builder.setView(view)
                .setTitle("Agregar Ingreso");

        return builder.create();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.btnAgregarIngreso: {
                int cantidad = Integer.parseInt(txtCantidadIngreso.getText().toString());
                String comentario = txtComentarioIngreso.getText().toString();

                if (!Integer.toString(cantidad).equals("") && !comentario.equals("")) {

                    mDatabase.child("totales").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            informacionTotales = snapshot.getValue(InformacionTotales.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //ingresoTotal = (informacionTotales == null) ? 0 : informacionTotales.getTotalIngresos();
                    String id = mDatabase.push().getKey();

                    Transaccion gasto = new Transaccion(id, cantidad, comentario, "ingreso");

                    mDatabase.child(id).setValue(gasto);

                    //Log.i("hola", Integer.toString(ingresoTotal));
                    //mDatabase.child("transacciones").push().setValue(new Transaccion(cantidad, comentario, "ingreso"));
                    //mDatabase.child("totales").child("total_ingresos").setValue(ingresoTotal + cantidad);

                    this.dismiss();

                } else {
                    Toast.makeText(getActivity(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        }
    }



    /*private InformacionTotales escucharTotales() {

        ValueEventListener escuchadorTotales = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    informacionTotales = dataSnapshot.getValue(InformacionTotales.class);
                    Log.i("hola", informacionTotales.toString());
                } else {
                    informacionTotales = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.child("totales").addValueEventListener(escuchadorTotales);
        return informacionTotales;
    }*/
}
