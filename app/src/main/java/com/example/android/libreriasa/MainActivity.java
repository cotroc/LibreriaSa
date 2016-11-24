package com.example.android.libreriasa;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnListar, btnNuevo, btnEditar, btnBorrar, btnConfig, btnSalir;
    private ConfigSQLite configSQLite;
    private String ip;
    private static final String TAG = "PrincipalActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        btnListar = (Button) findViewById(R.id.btnListar);
        btnNuevo = (Button) findViewById(R.id.btnNuevo);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnBorrar = (Button) findViewById(R.id.btnBorrar);
        btnConfig = (Button) findViewById(R.id.btnConfig);
        btnSalir = (Button) findViewById(R.id.btnSalir);
        ip = this.getIp();
    }

    public void listarActivity(View v) {
        Intent listar = new Intent(this, ListarActivity.class);
        startActivity(listar);
    }

    public void insertarActivity(View v) {
        Intent insertar = new Intent(MainActivity.this, InsertarActivity.class);
        insertar.putExtra("ip", ip);
        startActivity(insertar);
    }

    public void configActivity(View v) {
        Intent Config = new Intent(this, ConfigActivity.class);
        startActivity(Config);
    }

    public String getIp() {
        configSQLite = new ConfigSQLite(this, "DataBase.sql", null, 1);
        Cursor c = configSQLite.selectAll();
        String ip = "";
        if(c.moveToFirst()) {
            ip = c.getString(2);
        }
        configSQLite.close();
        return ip;

    }
}
