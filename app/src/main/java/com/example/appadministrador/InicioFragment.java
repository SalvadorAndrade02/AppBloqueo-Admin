package com.example.appadministrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment extends Fragment {
    RecyclerView recyclerView;
    RegistroAdapter adapter;
    List<DispositivosRegistrados> listaDispositivos;
    TextView tvMensaje;
    //private SearchView searchView;
    DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
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

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);


        // Configurar Views
        recyclerView = view.findViewById(R.id.recyclerView);
        tvMensaje = view.findViewById(R.id.tvMensaje);

        androidx.appcompat.widget.SearchView searchView = view.findViewById(R.id.searchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listaDispositivos = new ArrayList<>();
        adapter = new RegistroAdapter(listaDispositivos, getActivity());
        recyclerView.setAdapter(adapter);
        actualizarMensaje(); // Verificar si hay registros

        // Conectar con Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("DatosDispositivos");
        cargarDatos();

        // Configurar el SearchView para filtrar la lista
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarLista(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarLista(newText);
                return false;
            }
        });
        return view;
    }

    private void filtrarLista(String texto) {
        List<DispositivosRegistrados> listaFiltrada = new ArrayList<>();

        for (DispositivosRegistrados item : listaDispositivos) {
            if (item.getNombreCliente().toLowerCase().contains(texto.toLowerCase()) ||
                    item.getMarcaTelefono().toLowerCase().contains(texto.toLowerCase()) ||
                    item.getEstado().toLowerCase().contains(texto.toLowerCase())) {

                listaFiltrada.add(item);
            }
        }

        adapter.filtrarLista(listaFiltrada);
        actualizarMensaje(); // Verificar si hay registros después de filtrar
    }
    private void actualizarMensaje() {
        if (adapter.getItemCount() == 0) {
            tvMensaje.setVisibility(View.VISIBLE);
        } else {
            tvMensaje.setVisibility(View.INVISIBLE);
        }
    }

    private void cargarDatos() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DatosDispositivos");

        databaseReference.orderByChild("userId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaDispositivos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DispositivosRegistrados DatosDispositivos = snapshot.getValue(DispositivosRegistrados.class);
                    listaDispositivos.add(DatosDispositivos);
                }
                adapter.notifyDataSetChanged();
                actualizarMensaje();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}