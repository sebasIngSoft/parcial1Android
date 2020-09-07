package com.example.parcial1android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class Map_Activity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    // Recibe por parametro la posicion exacta donde se pulso y añade un
    // marcador
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}