package com.webmyne.paylabas.userapp.user_navigation;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.webmyne.paylabas_user.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Contactus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Contactus extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Contactus.
     */
    // TODO: Rename and change types and number of parameters
    public static Contactus newInstance(String param1, String param2) {
        Contactus fragment = new Contactus();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Contactus() {
        // Required empty public constructor
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

View convertview  = inflater.inflate(R.layout.fragment_contactus, container, false);


        // Get a handle to the Map Fragment

        GoogleMap map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng Paylabas_france = new LatLng(48.657152, 6.131071);



        map.setMyLocationEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Paylabas_france, 13));
        map.addMarker(new MarkerOptions()
                .title("Paylabas Headquarters")
                .snippet("6 Allée Pelletier Doisy, \n" +
                        "54600 Villers-lès-Nancy\n" +
                        "France\n" +
                        "Tel:+33(0)3 83 61 44 37")
                .position(Paylabas_france));

        return convertview;
    }

   /* @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.map));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }*/
}
