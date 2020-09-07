package com.example.parcial1android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parcial1android.Controlador.CtlUsuario;

public class MainActivity extends AppCompatActivity {
    private EditText txtUsername;
    private EditText txtPassword;

    CtlUsuario gestionUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsername = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        gestionUsuarios = new CtlUsuario(this);
    }

    public void Ingresar(View v){
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        if(!username.equals("") && !password.equals("") ){
            if(gestionUsuarios.buscar(username,password)!=null){
                Intent intent = new Intent(this , Map_Activity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Por favor rellene todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Registrarse(View v){
        Intent intent = new Intent(this, Registro_Usuario_Activity.class);
        startActivity(intent);
    }
}