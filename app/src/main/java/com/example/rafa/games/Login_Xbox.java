package com.example.rafa.games;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login_Xbox extends AppCompatActivity implements View.OnClickListener{
    Button btnIngresar;
    TextView txtRegistrar,txtRecuperar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__xbox);
        btnIngresar=(Button)findViewById(R.id.ingresar_btn);
        txtRegistrar=(TextView)findViewById(R.id.registrar_usuario_txt);
        txtRecuperar=(TextView)findViewById(R.id.recuperar_password_txt);
        btnIngresar.setOnClickListener(this);
        txtRegistrar.setOnClickListener(this);
        txtRecuperar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        Intent nuevo;
        switch (id)
        {
            case R.id.ingresar_btn:
                nuevo=new Intent(this,principal.class);
                startActivity(nuevo);
                break;
            case R.id.registrar_usuario_txt:
                nuevo=new Intent(this,Registrar_Usuario.class);
                startActivity(nuevo);
                break;
        }
    }
}
