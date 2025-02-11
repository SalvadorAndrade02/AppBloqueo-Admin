package com.example.appadministrador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class EditarRegistroActivity extends AppCompatActivity {

    EditText etIMEI, etPrecio, etNombre, etDomicilio, etEdad, etPeriodo, etPagoSemanal, etSemanasTotales, etMonto;
    Button btnGuardar;
    Spinner spinnerMarca;
    String[] opcionesMarca = {"Samsung", "Motorola"};
    DatabaseReference databaseReference;
    String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_registro);

        etIMEI = findViewById(R.id.etEditarIMEI);
        spinnerMarca = findViewById(R.id.spinnerMarca);
        etPrecio = findViewById(R.id.etEditarPrecio);
        etNombre = findViewById(R.id.etEditarNombre);
        etDomicilio = findViewById(R.id.etEditarDomicilio);
        etEdad = findViewById(R.id.etEditarEdad);
        etPeriodo = findViewById(R.id.etEditarPeriodo);
        etPagoSemanal = findViewById(R.id.etEditarPagoSemanal);
        etSemanasTotales = findViewById(R.id.etEditarSemanasTotales);
        etMonto = findViewById(R.id.etEditarMonto);
        btnGuardar = findViewById(R.id.btnGuardarEdicion);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesMarca);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("DatosDispositivos");

        // Recuperar el ID real del registro desde el Intent
        String dispositivoId  = getIntent().getStringExtra("id");

        // Obtener datos del Intent
        String imei = getIntent().getStringExtra("IMEI");
        // Obtener la marca actual y preseleccionarla en el Spinner
        String marcaActual = getIntent().getStringExtra("marcaTelefono");
        if (marcaActual != null) {
            int posicion = Arrays.asList(opcionesMarca).indexOf(marcaActual);
            if (posicion >= 0) {
                spinnerMarca.setSelection(posicion);
            }
        }
        String precio = getIntent().getStringExtra("Precio");
        String nombre = getIntent().getStringExtra("nombreCliente");
        String domicilio = getIntent().getStringExtra("domicilio");
        String edad = getIntent().getStringExtra("edad");
        String periodo = getIntent().getStringExtra("PeriodoPago");
        String pagoseman = getIntent().getStringExtra("PagoSemanal");
        String semantotal = getIntent().getStringExtra("SemanasTotales");
        String monto = getIntent().getStringExtra("MontoTotal");

        // Mostrar datos en los EditText
        etIMEI.setText(imei);
        spinnerMarca.setAdapter(adapter);
        etPrecio.setText(precio);
        etNombre.setText(nombre);
        etDomicilio.setText(domicilio);
        etEdad.setText(edad);
        etPeriodo.setText(periodo);
        etPagoSemanal.setText(pagoseman);
        etSemanasTotales.setText(semantotal);
        etMonto.setText(monto);

        btnGuardar.setOnClickListener(v -> guardarCambios());

    }
    private void guardarCambios() {
        String dispositivoId  = getIntent().getStringExtra("id");
        if (dispositivoId == null || dispositivoId.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró el ID del registro", Toast.LENGTH_SHORT).show();
            return;
        }

        String nuevoImei = etIMEI.getText().toString().trim();
        String nuevoMarca = spinnerMarca.getSelectedItem().toString();
        String nuevoPrecio = etPrecio.getText().toString();
        String nuevoNombre = etNombre.getText().toString();
        String nuevoDomicilio = etDomicilio.getText().toString();
        String nuevoEdad = etEdad.getText().toString();
        String nuevoPeriodo = etPeriodo.getText().toString();
        String nuevoPagoseman = etPagoSemanal.getText().toString();
        String nuevoSemantotal = etSemanasTotales.getText().toString();
        String nuevoMonto = etMonto.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DatosDispositivos").child(dispositivoId);
        if (!esIMEIValido(nuevoImei)) {
            Toast.makeText(this, "El IMEI debe tener 15 dígitos numéricos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nuevoImei.isEmpty() && !nuevoMarca.isEmpty() && !nuevoPrecio.isEmpty() && !nuevoNombre.isEmpty() &&
                !nuevoDomicilio.isEmpty() && !nuevoEdad.isEmpty() && !nuevoPeriodo.isEmpty() &&
                !nuevoPagoseman.isEmpty() && !nuevoSemantotal.isEmpty() && !nuevoMonto.isEmpty()) {

            // Mantener el userId al actualizar los datos
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userId = snapshot.child("userId").getValue(String.class); // Obtener el userId original

                        HashMap<String, Object> datosActualizados = new HashMap<>();
                        datosActualizados.put("imei", nuevoImei);
                        datosActualizados.put("marcaTelefono", nuevoMarca);
                        datosActualizados.put("precio", nuevoPrecio);
                        datosActualizados.put("nombreCliente", nuevoNombre);
                        datosActualizados.put("domicilio", nuevoDomicilio);
                        datosActualizados.put("edad", nuevoEdad);
                        datosActualizados.put("periodoPago", nuevoPeriodo);
                        datosActualizados.put("pagoSemanal", nuevoPagoseman);
                        datosActualizados.put("semanasTotal", nuevoSemantotal);
                        datosActualizados.put("montoTotal", nuevoMonto);
                        datosActualizados.put("userId", userId); // Asegurar que no se pierda el userId

                        databaseReference.updateChildren(datosActualizados)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(EditarRegistroActivity.this, "Registro actualizado", Toast.LENGTH_SHORT).show();
                                    // Enviar resultado para indicar que hubo un cambio
                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(EditarRegistroActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                                );
                    } else {
                        Toast.makeText(EditarRegistroActivity.this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditarRegistroActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean esIMEIValido(String imei) {
        return imei.matches("\\d{15}"); // Expresión regular: 15 dígitos numéricos
    }

}