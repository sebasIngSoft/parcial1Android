package com.example.parcial1android;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.parcial1android.Controlador.CtlUbicacion;
import com.example.parcial1android.Controlador.CtlUsuario;
import com.example.parcial1android.Modelo.Ubicacion;
import com.example.parcial1android.Modelo.Usuario;
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

public class Map_Activity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, SensorEventListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private float zoom;

    Button btnSalir,btnEditar,btnListar,btnAlejar;
    Sensor s;
    SensorManager sensorM;

    Usuario usuario;
    Ubicacion recibirUbicacion;
    float[] colores;
    CtlUbicacion ctlUbicacion;
    CtlUsuario ctlUsuario;
    List<Ubicacion> listaUbicaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_);

        colores = new float[]{60.0f, 210.0f, 240.0f, 180.0f, 120.0f, 300.0f, 30.0f, 0.0f, 330.0f, 270.0f};

        ctlUsuario = new CtlUsuario(this);
        ctlUbicacion = new CtlUbicacion(this);
        listaUbicaciones = new ArrayList<>();

        usuario = ctlUsuario.getUsuario();
        recibirUbicacion = ctlUbicacion.getUbicacion();
        btnSalir = (Button) findViewById(R.id.btnSalir);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnListar = (Button) findViewById(R.id.btnListar);
        btnAlejar = (Button) findViewById(R.id.btnAlejar);

        sensorM= (SensorManager) getSystemService(SENSOR_SERVICE);
        s = sensorM.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorM.registerListener(Map_Activity.this,s,SensorManager.SENSOR_DELAY_NORMAL);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);// visualizar la brujula
        listaUbicaciones =  ctlUbicacion.listaUbicacionesUsuario(usuario.getUsername());
        /*Cuando llega de lista puntos*/
        if(recibirUbicacion!=null){
            float zoomLevel = 5.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(recibirUbicacion.getLatitud(),recibirUbicacion.getLongitud()),zoomLevel));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            btnListar.setVisibility(View.GONE);
            btnSalir.setVisibility(View.GONE);
            btnEditar.setVisibility(View.VISIBLE);
            btnAlejar.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < listaUbicaciones.size();i++){
            for (int x = 0; x < colores.length;x++){
                if(listaUbicaciones.get(i).getColor()==x){
                    mMap.addMarker(new MarkerOptions().position(new LatLng(listaUbicaciones.get(i).getLatitud(),listaUbicaciones.get(i).getLongitud()))
                            .icon(BitmapDescriptorFactory.defaultMarker(colores[x]))
                            .title(listaUbicaciones.get(i).getNombre())// titulo del marcador
                            .snippet(listaUbicaciones.get(i).getDescripcion()));
                }
            }
        }
        mMap.setOnMapClickListener(this);// se asigna el lister asignado al
        // metodo onMapClick del mapa
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(LatLng coordenadas) {
        // Recibe por parametro la posicion exacta donde se pulso y añade un
        // marcador
        addMarker();
    }

    public void addMarker() {
        double latitudMapa = mMap.getCameraPosition().target.latitude;// latitud
        /* del punto central que se este visualizando*/
        double altitudMapa = mMap.getCameraPosition().target.longitude; // altitud
        /* del punto central que se este visualizando*/
        Ubicacion ubicacion = new Ubicacion(0,"","",0,latitudMapa,altitudMapa,usuario.getUsername());
        mMap.addMarker(new MarkerOptions().position(new LatLng(ubicacion.getLatitud(),ubicacion.getLongitud())).icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title("verificando")// titulo del marcador
                .snippet("..."));
        Intent intent = new Intent(this, RegistroPunto_Activity.class);
        intent.putExtra("clsUbicacion",ubicacion);
        startActivity(intent);
    }



    // Recibe por parametro la posicion exacta donde se pulso y añade un
    // marcador
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void alejar(View view){
        float zoomLevel = 2.0f;//This goes up to 21
        Ubicacion xUbicacion;
        if(recibirUbicacion!=null){
            xUbicacion = recibirUbicacion;
        }else{
            xUbicacion = ctlUbicacion.getUbicacion();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(xUbicacion.getLatitud(),xUbicacion.getLongitud()),zoomLevel));
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.resetMinMaxZoomPreference();
        btnListar.setVisibility(View.VISIBLE);
        btnSalir.setVisibility(View.VISIBLE);
        btnEditar.setVisibility(View.GONE);
        btnAlejar.setVisibility(View.GONE);
        ctlUbicacion.setUbicacion(null);
    }

    public void salir(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void irAlista(View view){
        Intent intent = new Intent(this, List_Puntos_Activity.class);
        startActivity(intent);
    }


    /*------- Metodos de los Sensores --------------------------------------------------*/
    //cuando el sensor detecta un cambio
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float valor = Float.parseFloat(String.valueOf(sensorEvent.values[0]));

        if (valor<5){
            /*CameraPosition camPos = mMap.getCameraPosition();
            LatLng coordenadas = camPos.target;
            LatLng pos = new LatLng(coordenadas.latitude, coordenadas.longitude);
            onMapClick(pos);*/
            addMarker();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        float zoomLevel = 5.0f; //This goes up to 21
        if(ctlUbicacion.buscar(usuario.getUsername(),marker.getTitle())!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoomLevel));
            btnEditar.setVisibility(View.VISIBLE);
            btnAlejar.setVisibility(View.VISIBLE);
            btnListar.setVisibility(View.GONE);
            btnSalir.setVisibility(View.GONE);
        }
        return false;
    }
    public void editar(View v){
        Intent intent = new Intent(this, RegistroPunto_Activity.class);
        startActivity(intent);
    }
    // Return null here, so that getInfoContents() is called next.
    public View getInfoWindow(Marker arg0) {
        return null;
    }
}