package com.example.rafa.games;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private FloatingActionButton floatBtnHecho;
    private double longitud;
    private double latitud;
    private AlertDialog AlertaEnviar = null;
    private AlertDialog AlertaNoUbicacion = null;
    TextView latitudeTxt,longitudeTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        floatBtnHecho=(FloatingActionButton)findViewById(R.id.FloatingHecho);
        floatBtnHecho.setOnClickListener(this);
        latitudeTxt=(TextView)findViewById(R.id.latitud_registro);
        longitudeTxt=(TextView)findViewById(R.id.longitud_registro);



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
        mMap.setOnMapLongClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location !=null)
        {
            LatLng latlong=new LatLng(location.getLatitude(),location.getLongitude());

            if(latlong.latitude!=0.0 && latlong.longitude!=0.0)
            {
                //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong));
                CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(latlong, 15);
                mMap.moveCamera(zoom);
            }else
            {
                LatLng durango=new LatLng(24.041063, -104.670341);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(durango));
                CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(durango, 15);
                mMap.moveCamera(zoom);
            }
        }else
        {
            LatLng durango=new LatLng(24.041063, -104.670341);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(durango));
            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(durango, 15);
            mMap.moveCamera(zoom);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }


        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View view) {
    int id= view.getId();

    if(id==R.id.FloatingHecho)
    {
        if(longitud!=0.0 && latitud!=0.0)
        {
            alerts();
        }else
        {
            setAlertaNoUbicacion();
        }
    }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        longitud=latLng.longitude;
        latitud=latLng.latitude;
        mMap.addMarker(new MarkerOptions().position(latLng));
    }

    public void alerts()
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("¿Esta seguro que desea establecer su ubicación aquí ?");


        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra("latitude", String.valueOf(latitud));
                        intent.putExtra("longitude", String.valueOf(latitud));
                        setResult(RESULT_OK, intent);
                        dialog.cancel();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertaEnviar=builder1.create();
        AlertaEnviar.show();
    }
    public void setAlertaNoUbicacion()
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("No ha seleccionado su ubicacion: Mantenga presionado un punto en el mapa en el lugar o area donde usted viva " +
                "(Esta información es privada y solo tu puedes verla, su uso es simplemente estadístico para el administrador)");


        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });

        AlertaEnviar=builder1.create();
        AlertaEnviar.show();
    }
}
