package com.example.appadministrador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgregarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarFragment extends Fragment {
    EditText etIMEI, etPrecio, etNombreCliente, etDomicilio, etEdad, etPeriodo, etPagoSemanal, etSemanasTotal, etMontoTotal;
    Button btnGuardar;
    Spinner spinnerMarca;
    DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgregarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgregarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarFragment newInstance(String param1, String param2) {
        AgregarFragment fragment = new AgregarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar, container, false);

        String[] opcionesMarca = {"Samsung", "Motorola"};

        etIMEI = view.findViewById(R.id.etIMEI);
        spinnerMarca = view.findViewById(R.id.etMarca);
        etPrecio = view.findViewById(R.id.etPrecio);

        // Adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Samsung", "Motorola"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter(adapter);

        etNombreCliente = view.findViewById(R.id.etNombreCliente);
        etDomicilio = view.findViewById(R.id.etDomicilio);
        etEdad = view.findViewById(R.id.etEdad);
        etPeriodo = view.findViewById(R.id.etPeriodo);
        etPagoSemanal = view.findViewById(R.id.etPagoSemanal);
        etSemanasTotal = view.findViewById(R.id.etSemanasTotal);
        etMontoTotal = view.findViewById(R.id.etMontoTotal);

        btnGuardar = view.findViewById(R.id.btnGuardar);

        databaseReference = FirebaseDatabase.getInstance().getReference("DatosDispositivos");

        btnGuardar.setOnClickListener(v -> guardarDatos());

        return view;
    }

    private void guardarDatos() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        String IMEI = etIMEI.getText().toString().trim();
        String marcaTelefono = spinnerMarca.getSelectedItem().toString();
        String Precio = etPrecio.getText().toString();
        String nombreCliente = etNombreCliente.getText().toString();
        String domicilio = etDomicilio.getText().toString();
        String edad = etEdad.getText().toString();
        String periodoPago = etPeriodo.getText().toString();
        String pagoSemanal = etPagoSemanal.getText().toString();
        String semanasTotal = etSemanasTotal.getText().toString();
        String montoTotal = etMontoTotal.getText().toString();

        if (!esIMEIValido(IMEI)) {
            Toast.makeText(requireContext(), "El IMEI debe tener 15 dígitos numéricos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!marcaTelefono.isEmpty() && !Precio.isEmpty() && !nombreCliente.isEmpty() && !domicilio.isEmpty() && !edad.isEmpty() && !periodoPago.isEmpty() && !pagoSemanal.isEmpty() && !semanasTotal.isEmpty() && !montoTotal.isEmpty()) {
            String id = databaseReference.push().getKey();
            DispositivosRegistrados dispositivos = new DispositivosRegistrados(id, userId, IMEI, marcaTelefono, Precio, nombreCliente, domicilio, edad, periodoPago, pagoSemanal, semanasTotal, montoTotal, "activo");
            databaseReference.child(id).setValue(dispositivos);

            // Mostrar un mensaje de éxito
            Toast.makeText(getActivity(), "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();

            // Limpiar los campos del formulario
            etIMEI.setText("");
            etPrecio.setText("");
            etNombreCliente.setText("");
            etDomicilio.setText("");
            etEdad.setText("");
            etPeriodo.setText("");
            etPagoSemanal.setText("");
            etSemanasTotal.setText("");
            etMontoTotal.setText("");

            // Volver al fragmento de inicio con commit()
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new InicioFragment()) // Reemplaza el fragmento actual
                    .commit(); // Asegúrate de llamar a commit para que la transacción se complete
        } else {
            Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean esIMEIValido(String imei) {
        return imei.matches("\\d{15}");
    }
}