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
import android.widget.Toast;

import java.util.ArrayList;

public class ConfigActivity extends AppCompatActivity {

    public Button btnSave, btnServices, btnErase;
    public EditText etService, etIp;
    public ListView lvList;
    public ConfigSQLite configSQLite;
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initComponents();

    }

    public void save(View v) {
        if(etIp.getText().toString().isEmpty()) {
            this.message("Falta Ip");
        } else {
            configSQLite = new ConfigSQLite(this, "DataBase.sql", null, 1);
            String service = etService.getText().toString();
            String ip = etIp.getText().toString();
            configSQLite.insertar(service, ip);
            this.clean();
            this.message("Configuracion guardada");
        }
    }

    public void listServer(View v) {
        configSQLite = new ConfigSQLite(this, "DataBase.sql", null, 1);
        Cursor c = configSQLite.selectAll();
        ArrayList<ArrayList> list = new ArrayList();
        if(c.moveToFirst()) {
            do {
                ArrayList<String> servicio = new ArrayList();
                servicio.add(0, c.getString(0));
                servicio.add(1, c.getString(1));
                servicio.add(2, c.getString(2));

                list.add(servicio);

            } while (c.moveToNext());
        }
        configSQLite.close();
        Adapter adapter = new ArrayAdapter(ConfigActivity.this, android.R.layout.simple_list_item_1, list);
        lvList.setAdapter((ListAdapter) adapter);

    }

    public void eraseDb(View v) {
        configSQLite = new ConfigSQLite(this, "DataBase.sql", null, 1);
        configSQLite.deleteAll();
        this.clean();
    }

    public void initComponents() {
        btnSave = (Button) findViewById(R.id.btnGuardar);
        btnServices = (Button) findViewById(R.id.btnServicios);
        btnErase = (Button) findViewById(R.id.btnErase);
        etService = (EditText) findViewById(R.id.etService);
        etIp = (EditText) findViewById(R.id.etIp);
        lvList = (ListView) findViewById(R.id.lvLista);
    }

    private void clean(){
        etService.setText("");
        etIp.setText("");
        lvList.setAdapter(null);
    }

    private void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
