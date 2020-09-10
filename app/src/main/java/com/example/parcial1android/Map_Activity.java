package com.example.parcial1android;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
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
import com.example.parcial1android.Modelo.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map_Activity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, SensorEventListener, GoogleMap.OnMarkerClickListener {

    //definicion para el manejo de sensores
    private static final float LIMITE_SENSIBILIDAD_SACUDIDA = 0.50f;
    private static final int LIMITE_TIEMPO_MS_SACUDIDA = 250;

    private static final float LIMITE_SENSIBILIDAD_ROTACION = 4.1f;
    private static final int LIMITE_TIEMPO_MS_ROTACION = 300;

    //Definicion de los Sensor manager de Cada uno de los sensores
    private SensorManager sensorManager;
    private Sensor sensorAcc;
    private Sensor sensorGyro;
    private Sensor sensoProx;

    //Tiempos de Sacudida
    private long tiempoSacudida = 0;
    private long tiempoRotacion = 0;

    //definos los botenes que se va a mostrar y ocultar dependiendo de la desicion
    Button btnSalir,btnEditar,btnListar,btnAlejar;
    EditText txtLugar;

    //de claracion para el map
    private GoogleMap mMap;
    Sensor s;
    SensorManager sensorM;

    //declaraciones para el manejo en metodos
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

        //instaciamiento para el manejo de datos
        ctlUsuario = new CtlUsuario(this);
        ctlUbicacion = new CtlUbicacion(this);
        listaUbicaciones = new ArrayList<>();
        usuario = ctlUsuario.getUsuario();
        recibirUbicacion = ctlUbicacion.getUbicacion();


        btnSalir = (Button) findViewById(R.id.btnSalir);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnListar = (Button) findViewById(R.id.btnListar);
        btnAlejar = (Button) findViewById(R.id.btnAlejar);
        txtLugar = (EditText) findViewById(R.id.txtLugar);
        colores = new float[]{60.0f, 210.0f, 240.0f, 180.0f, 120.0f, 300.0f, 30.0f, 0.0f, 330.0f, 270.0f};

        //Instanciamento de cada uno de los sensores
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensoProx = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        //instanciamiento para la ejecucion del mp
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        listaUbicaciones =  ctlUbicacion.listaUbicacionesUsuario(usuario.getUsername());
        /*Cuando llega de lista puntos*/
        if(recibirUbicacion!=null){
            float zoomLevel = 15.0f; //This goes up to 21
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
        addMarker(coordenadas);
    }

    public void addMarker(LatLng coordenadas) {

        Ubicacion ubicacion = new Ubicacion(0,"","",0,coordenadas.latitude,coordenadas.longitude,usuario.getUsername());
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        float zoomLevel = 15.0f; //This goes up to 21
        if(ctlUbicacion.buscar(usuario.getUsername(),marker.getTitle())!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoomLevel));
            btnEditar.setVisibility(View.VISIBLE);
            btnAlejar.setVisibility(View.VISIBLE);
            btnListar.setVisibility(View.GONE);
            btnSalir.setVisibility(View.GONE);
        }
        return false;
    }

    /*------- Metodos de los Sensores --------------------------------------------------*/
    //cuando el sensor detecta un cambio
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            detectarSacudida(sensorEvent);
        } else {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                detectarRotacion(sensorEvent);
            }
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){
            float valor = Float.parseFloat(String.valueOf(sensorEvent.values[0]));
            if (valor<3){
                double latitudMapa = mMap.getCameraPosition().target.latitude;// latitud
                /* del punto central que se este visualizando*/
                double altitudMapa = mMap.getCameraPosition().target.longitude; // altitud
                /* del punto central que se este visualizando*/
                LatLng pos = new LatLng(latitudMapa, altitudMapa);
                addMarker(pos);
            }
        }
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorGyro, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensoProx, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected  void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void detectarSacudida(SensorEvent event){
        long now = System.currentTimeMillis();

        if ((now - tiempoSacudida) > LIMITE_TIEMPO_MS_SACUDIDA){
            tiempoSacudida = now;

            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            double gForce = Math.sqrt(gX*gX + gY*gY + gZ*gZ);

            if (gForce > LIMITE_SENSIBILIDAD_SACUDIDA){
                if(event.values[0]>=4 && event.values[0]<=7){
                    // mover la camara asi la izqierda
                    mMap.moveCamera(CameraUpdateFactory.scrollBy(event.values[0]*10,event.values[1]*10));

                }
                if(event.values[0]<=-4 && event.values[0]>=-7){
                    // mover la camara asi la derecha
                    mMap.moveCamera(CameraUpdateFactory.scrollBy(event.values[0]*10,event.values[1]*10));


                }
                if (event.values[1]<=(-1) && event.values[1]>=-4){
                    // mover la camara asi arriba
                    mMap.moveCamera(CameraUpdateFactory.scrollBy(event.values[0]*10,event.values[1]*10));

                }
                if (event.values[1]>=7 && event.values[1]<=9 ){
                    // mover la camara asi abajo
                    mMap.moveCamera(CameraUpdateFactory.scrollBy(event.values[0]*10,event.values[1]*10));

                }
            }

        }
    }

    private void detectarRotacion(SensorEvent event){

        long now = System.currentTimeMillis();

        if ((now - tiempoRotacion) > LIMITE_TIEMPO_MS_ROTACION){

            tiempoRotacion = now;

            if (Math.abs(event.values[0]) > LIMITE_SENSIBILIDAD_ROTACION ||
                    Math.abs(event.values[1]) > LIMITE_SENSIBILIDAD_ROTACION ||
                    Math.abs(event.values[2]) > LIMITE_SENSIBILIDAD_ROTACION){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                finish();
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //------------------------------Metodos para ser acciones en la interfas--------------
    public void alejar(View view){
        float zoomLevel = -15.0f;//This goes up to 21
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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        finish();
    }

    public void irAlista(View view){
        Intent intent = new Intent(this, List_Puntos_Activity.class);
        startActivity(intent);
    }

    public void editar(View v){
        Intent intent = new Intent(this, RegistroPunto_Activity.class);
        startActivity(intent);
    }

    public void buscarDireccion(View view){
        String direccion = txtLugar.getText().toString();
        List<Address> addressList = null;
        if(direccion!=null && !direccion.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try {
                //String direccion, Int numero maximo de resultados
                //Devuelve los puntos de las concordancias en el mapa segun el texto ingresado
                addressList = geocoder.getFromLocationName(direccion,1);
            } catch (IOException e) {
                e.printStackTrace();

            }
            Address address = addressList.get(0);
            LatLng posicion = new LatLng(address.getLatitude(),
                    address.getLongitude());

            mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
        }
    }

    // Return null here, so that getInfoContents() is called next.
    public View getInfoWindow(Marker arg0) {
        return null;
    }
}