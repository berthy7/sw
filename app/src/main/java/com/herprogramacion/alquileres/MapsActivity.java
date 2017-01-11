package com.herprogramacion.alquileres;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<String> latitud = new ArrayList<String>();
    List<String> longitud = new ArrayList<String>();
    List<String> precio = new ArrayList<String>();
    List<String> ids = new ArrayList<String>();
    String dato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
            latitud = (ArrayList<String>) getIntent().getStringArrayListExtra("LATITUD");
            longitud = (ArrayList<String>) getIntent().getStringArrayListExtra("LONGITUD");
            precio = (ArrayList<String>) getIntent().getStringArrayListExtra("PRECIO");
            ids = (ArrayList<String>) getIntent().getStringArrayListExtra("IDS");
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

        // Add a marker in Sydney and move the camera
        for (int i = 0; i < longitud.size(); i++) {
            LatLng sc = new LatLng(Double.valueOf(latitud.get(i)), Double.valueOf(longitud.get(i)));
            //  LatLng sydney = new LatLng(-17.777811418089367, -63.18053196295165);
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.casa)).position(sc).title(precio.get(i)+" Bs"));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sc));
            dato=ids.get(i);
mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
    @Override
    public void onInfoWindowClick(Marker marker) {
        //Toast.makeText(getApplicationContext(),"Has pulsado una marca", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),InmuebleActivity.class);

        // Poner el Id de la imagen como extra en la intención
        intent.putExtra("dato",dato);

        // Aquí pasaremos el parámetro de la intención creada previamente
        startActivity(intent);
    }
});

        }
    }
}

