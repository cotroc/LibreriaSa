package com.example.android.libreriasa;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ConfigActivity extends AppCompatActivity {

    public Button btnGuardar, btnSerivcios, btnLimpiar;
    public EditText etService, etIp;
    public ListView lvLista;
    public ConfigSQLite configSQLite;
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initComponents();

    }

    public void guardar(View v) {
        configSQLite = new ConfigSQLite(this, "DataBase.sql", null, 1);
        String service = etService.getText().toString();
        String ip = etIp.getText().toString();
        configSQLite.insertar(service, ip);


    }

    public void listar(View v) {
        configSQLite = new ConfigSQLite(this, "DataBase.sql", null, 1);
        Cursor c = configSQLite.selectAll();
        ArrayList<ArrayList> lista = new ArrayList();
        if(c.moveToFirst()) {
            do {
                ArrayList<String> servicio = new ArrayList();
                servicio.add(0, c.getString(0));
                servicio.add(1, c.getString(1));
                servicio.add(2, c.getString(2));

                lista.add(servicio);

            } while (c.moveToNext());
        }
        configSQLite.close();
        Adapter adaptador = new ArrayAdapter(ConfigActivity.this, android.R.layout.simple_list_item_1, lista);
        lvLista.setAdapter((ListAdapter) adaptador);

    }

    public void limpiar(View v) {
        configSQLite = new ConfigSQLite(this, "DataBase.sql", null, 1);
        configSQLite.deleteAll();
    }

    public void initComponents() {
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnSerivcios = (Button) findViewById(R.id.btnServicios);
        btnLimpiar = (Button) findViewById(R.id.btnLimpiar);
        etService = (EditText) findViewById(R.id.etService);
        etIp = (EditText) findViewById(R.id.etIp);
        lvLista = (ListView) findViewById(R.id.lvLista);
    }

}
