package com.example.parcial1android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.parcial1android.Controlador.CtlUbicacion;
import com.example.parcial1android.Controlador.CtlUsuario;
import com.example.parcial1android.Modelo.Ubicacion;
import com.example.parcial1android.Modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class List_Puntos_Activity extends AppCompatActivity {
    private ListView listaPuntos;

    CtlUbicacion gestionUbicaciones;
    CtlUsuario gestionUsuario;
    private Usuario usuario;
    List<Ubicacion> listaUbicaciones;
    List<String> listaUbicacionesString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__puntos_);


        listaPuntos = (ListView) findViewById(R.id.lstPuntos);

        gestionUbicaciones = new CtlUbicacion(this);
        gestionUsuario = new CtlUsuario(this);
        usuario = gestionUsuario.getUsuario();

        if(gestionUbicaciones.listarUbicacionesString(usuario.getUsername()).isEmpty()){
            listaUbicaciones = new ArrayList<>();
            listaUbicacionesString = new ArrayList<>();
        }else{
            listaUbicaciones = gestionUbicaciones.listaUbicacionesUsuario(usuario.getUsername());
            listaUbicacionesString = gestionUbicaciones.listarUbicacionesString(usuario.getUsername());
        }
        Listar();
    }

    public void Listar(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listaUbicacionesString);
        listaPuntos.setAdapter(adapter);
        listaPuntos.setOnItemClickListener
                (new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                        gestionUbicaciones.buscar(listaUbicaciones.get(posicion).getUsername_fk(), listaUbicaciones.get(posicion).getNombre());
                        ir();
                    }
                });
    }

    public void Mapa(View v){
        ir();
    }

    public void ir(){
        Intent intent = new Intent(this, Map_Activity.class);
        startActivity(intent);
    }
}