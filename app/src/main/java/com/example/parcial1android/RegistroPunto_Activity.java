package com.example.parcial1android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parcial1android.Controlador.CtlUbicacion;
import com.example.parcial1android.Controlador.CtlUsuario;
import com.example.parcial1android.Modelo.Ubicacion;

import java.util.ArrayList;

public class RegistroPunto_Activity extends AppCompatActivity {

    private EditText txtNombrePunto,txtDescripcion;
    private Spinner spnColor;
    CtlUbicacion ctlUbicacion;
    CtlUsuario ctlUsuario;
    Ubicacion clsUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_punto_);

        Bundle bundle = getIntent().getExtras();
        clsUbicacion = (Ubicacion) bundle.getSerializable("clsUbicacion");

        ctlUbicacion = new CtlUbicacion(this);
        ctlUsuario = new CtlUsuario(this);
        spnColor = (Spinner) findViewById(R.id.spnColor);
        txtNombrePunto = (EditText) findViewById(R.id.txtNombrePunto);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);

        cargarColores();
    }

    public void cargarColores() {
        String[] opciones ={"HUE_YELLOW","HUE_AZURE","HUE_BLUE","HUE_CYAN","HUE_GREEN","HUE_MAGENTA","HUE_ORANGE","HUE_RED","HUE_ROSE","HUE_VIOLET"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spnColor.setAdapter(adapter);
    }

    public void registrar(View view){
        if (txtNombrePunto.getText().toString().equals("") || txtDescripcion.getText().toString().equals("")){
            Toast.makeText(this,"Por favor complete los campos",Toast.LENGTH_SHORT).show();
        }else{
            clsUbicacion.setNombre(txtNombrePunto.getText().toString());
            clsUbicacion.setDescripcion(txtDescripcion.getText().toString());
            clsUbicacion.setColor(spnColor.getSelectedItemPosition());

            if (ctlUbicacion.guardarObjeto(clsUbicacion)){
                Toast.makeText(this,"Registro Exitoso",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Map_Activity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"No sea completado el registro",Toast.LENGTH_SHORT).show();
            }
        }
    }
}