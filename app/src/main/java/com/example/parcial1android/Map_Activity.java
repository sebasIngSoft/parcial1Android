package com.example.parcial1android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.parcial1android.Controlador.CtlUbicacion;
import com.example.parcial1android.Controlador.CtlUsuario;
import com.example.parcial1android.Modelo.Ubicacion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map_Activity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, SensorEventListener {

    private GoogleMap mMap;
    private String username,nombreUbicacion;
    private float zoom;

    Button btnSalir,btnEliminar,btnEditar,btnListar,btnAlejar;
    Sensor s;
    SensorManager sensorM;

    private EditText txtUbicacion;
    float[] colores;
    CtlUbicacion ctlUbicacion;
    CtlUsuario ctlUsuario;
    List<Ubicacion> listaUbicaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_);

        Bundle bundle = getIntent().getExtras();
        nombreUbicacion = bundle.getString("nombreUbicacion");


        colores = new float[]{60.0f, 210.0f, 240.0f, 180.0f, 120.0f, 300.0f, 30.0f, 0.0f, 330.0f, 270.0f};

        ctlUsuario = new CtlUsuario(this);
        ctlUbicacion = new CtlUbicacion(this);

        username = ctlUsuario.getUsuario().getUsername();
        btnSalir = (Button) findViewById(R.id.btnSalir);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnListar = (Button) findViewById(R.id.btnListar);
        btnAlejar = (Button) findViewById(R.id.btnAlejar);

        sensorM= (SensorManager) getSystemService(SENSOR_SERVICE);
        s = sensorM.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorM.registerListener(Map_Activity.this,s,SensorManager.SENSOR_DELAY_NORMAL);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        /*Cuando llega de lista puntos*/
        if(!nombreUbicacion.equals("")){
            Ubicacion ubicacion = ctlUbicacion.buscar(ctlUsuario.getUsuario().getUsername(),nombreUbicacion);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ubicacion.getLatitud(),ubicacion.getLongitud()),10));
            mMap.getUiSettings().setZoomControlsEnabled(false);
            btnListar.setVisibility(View.GONE);btnSalir.setVisibility(View.GONE);btnEditar.setVisibility(View.VISIBLE);btnEliminar.setVisibility(View.VISIBLE);btnAlejar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onMapClick(LatLng coordenadas) {
        // Recibe por parametro la posicion exacta donde se pulso y añade un
        // marcador
        Intent intent = new Intent(this, RegistroPunto_Activity.class);
        intent.putExtra("latitud",coordenadas.latitude);
        intent.putExtra("longitud",coordenadas.longitude);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        listaUbicaciones =  ctlUbicacion.listaUbicacionesUsuario(username);
        mMap.getUiSettings().setCompassEnabled(true);// visualizar la brujula
        for (int i = 0; i < listaUbicaciones.size();i++){
            for (int x = 0; x < colores.length;x++){
                if(listaUbicaciones.get(i).getColor()==colores[x]){
                    mMap.addMarker(new MarkerOptions().position(new LatLng(listaUbicaciones.get(i).getLatitud(),listaUbicaciones.get(i).getLongitud())).icon(
                            BitmapDescriptorFactory
                                    .defaultMarker(colores[x])).title(listaUbicaciones.get(i).getNombre())// titulo del marcador
                            .snippet(listaUbicaciones.get(i).getDescripcion()));
                }
            }
        }
        mMap.setOnMapClickListener(this);// se asigna el lister asignado al
        // metodo onMapClick del mapa
    }

    // Recibe por parametro la posicion exacta donde se pulso y añade un
    // marcador
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void alejar(View view){
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.resetMinMaxZoomPreference();
        btnListar.setVisibility(View.VISIBLE);btnSalir.setVisibility(View.VISIBLE);btnEditar.setVisibility(View.GONE);btnEliminar.setVisibility(View.GONE);btnAlejar.setVisibility(View.GONE);
    }

    public void salir(View view){
        System.exit(0);
    }

    public void irAlista(View view){
        Intent intent = new Intent(this, List_Puntos_Activity.class);
        startActivity(intent);
    }

    public void guardarUbicacion(){
        CameraPosition camPos = mMap.getCameraPosition();
        LatLng coordenadas = camPos.target;
        double latitud = coordenadas.latitude;
        double longitud = coordenadas.longitude;

        mMap.addMarker(new MarkerOptions().position(new LatLng(latitud,longitud)).icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title("punto X")// titulo del marcador
                .snippet("Sin informacion"));
    }


    /*------- Metodos de los Sensores --------------------------------------------------*/
    //cuando el sensor detecta un cambio
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float valor = Float.parseFloat(String.valueOf(sensorEvent.values[0]));

        if (valor<5){
            guardarUbicacion();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}