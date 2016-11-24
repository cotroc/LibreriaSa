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
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
    }

    private void initComponents() {
        btnListar = (Button) findViewById(R.id.btnList);
        btnNuevo = (Button) findViewById(R.id.btnNew);
        btnEditar = (Button) findViewById(R.id.btnEdit);
        btnBorrar = (Button) findViewById(R.id.btnDel);
        btnConfig = (Button) findViewById(R.id.btnConfig);
        btnSalir = (Button) findViewById(R.id.btnQuit);
        ip = this.getIp();
    }

    public void listarActivity(View v) {
        intent = new Intent(this, ListActivity.class);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    public void insertarActivity(View v) {
        intent = new Intent(this, BookCrudActivity.class);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    public void configActivity(View v) {
        intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
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
