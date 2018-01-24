package com.example.rafa.games;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Registrar_Tiempos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registrar_Tiempos extends Fragment implements View.OnClickListener{
    Button detalles_xbox1,detalles_xbox2,detalles_xbox3,detalles_xbox4,
    detalles_xbox5,detalles_xbox16,detalles_xbox7,detalles_xbox8,detalles_xbox9,
    detalles_xbox10,detalles_xbox111,detalles_xbox12,detalles_xbox13,detalles_xbox14,detalles_xbox15;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentManager fragmentManager;    //Manejador de los fragmentos para realizar transacciones e incorporarlos a l content_nav_mapa

    public Registrar_Tiempos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Registrar_Tiempos.
     */
    // TODO: Rename and change types and number of parameters
    public static Registrar_Tiempos newInstance(String param1, String param2) {
        Registrar_Tiempos fragment = new Registrar_Tiempos();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tiempos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detalles_xbox1=(Button)view.findViewById(R.id.detallesXbox1);
        detalles_xbox1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        Intent detalles=new Intent(this.getContext(),Detalles_Xbox.class);
        switch (id)
        {

            case R.id.detallesXbox1: startActivity(detalles);  break;
        }
    }
}
