package com.herprogramacion.alquileres;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
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

public class Maps2Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    double rango=0.003311418089367;

    SoundPool soundPool;
    int carga;
    MediaPlayer mediaPlayer;



    NotificationCompat.Builder nBuiler;
    public static final int notificacionId=1;
    boolean noti=false;

    List<String> latitud = new ArrayList<String>();
    List<String> longitud = new ArrayList<String>();
    List<String> precio = new ArrayList<String>();
    List<String> ids = new ArrayList<String>();
    String dato;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        carga = soundPool.load(this,R.raw.tono,1);
        mediaPlayer = new MediaPlayer();
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();

        if(noti==true){
            soundPool.play(carga,1,1,0,0,1);
            nBuiler = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                    .setContentTitle("ALQUILERES")
                    .setContentText("Se detecto inmuebles cerca");

            Intent resultIntent = new Intent(this,Maps2Activity.class);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(this, 0, resultIntent, 0);


            nBuiler.setContentIntent(resultPendingIntent);

            NotificationManager nNotification =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nNotification.notify(notificacionId, nBuiler.build());


        }



       /* LatLng sc = new LatLng(lat, lng);
        //  LatLng sydney = new LatLng(-17.777811418089367, -63.18053196295165);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.casa)).position(sc).title(" Bs"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sc));
*/

    }

    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) marcador.remove();


        for (int i = 0; i < longitud.size(); i++) {
            if(lat+rango>Double.valueOf(latitud.get(i))&&lng+rango>Double.valueOf(longitud.get(i))){
                if(lat-rango<Double.valueOf(latitud.get(i))&&lng-rango<Double.valueOf(longitud.get(i))){
                    LatLng sc = new LatLng(Double.valueOf(latitud.get(i)), Double.valueOf(longitud.get(i)));
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.casa)).position(sc).title(precio.get(i)+" Bs"));
                   noti=true;
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

        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Mi Posicion Actual")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.persona)));
        mMap.animateCamera(miUbicacion);



    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);
        }
    }

android.location.LocationListener locListener =new android.location.LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
        actualizarUbicacion(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
};



    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1500,0,locListener);
    }
}
