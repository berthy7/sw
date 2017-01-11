package com.herprogramacion.alquileres;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsIActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String Latitud;
    String Longitud;
    Double LATITUD;
    Double LONGITUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_i);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //hiloconexion = new ObtenerWebService();
        // String cadenallamada = IP + "?idalumno=0";
        //  hiloconexion.execute(cadenallamada, "1");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        if (extras != null) {
        //    latitud = (ArrayList<String>) getIntent().getStringArrayListExtra("LATITUD");
          //  longitud = (ArrayList<String>) getIntent().getStringArrayListExtra("LONGITUD");
            //precio = (ArrayList<String>) getIntent().getStringArrayListExtra("PRECIO");
            Latitud =extras.getString("LATITUD");
            Longitud =extras.getString("LONGITUD");

            LATITUD= (Double.valueOf(Latitud));
            LONGITUD= (Double.valueOf(Longitud));
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sc = new LatLng(LATITUD, LONGITUD);
        //  LatLng sydney = new LatLng(-17.777811418089367, -63.18053196295165);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.casa)).position(sc));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sc));
        // Add a marker in Sydney and move the camera
      //  LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
