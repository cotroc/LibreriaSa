package com.example.android.libreriasa;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnList, btnNew, btnConfig, btnQuit;
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

    @Override
    protected void onStart(){
        super.onStart();
        this.checkIp();
    }

/*    @Override
    protected void onResume(){
        super.onResume();
        ip = this.getIp();
        this.onStart();
    }*/

    private void initComponents() {
        btnList = (Button) findViewById(R.id.btnList);
        btnNew = (Button) findViewById(R.id.btnNew);
        btnConfig = (Button) findViewById(R.id.btnConfig);
        btnQuit = (Button) findViewById(R.id.btnQuit);
        ip = this.getIp();
    }

    public void listActivity(View v) {
        this.checkIp();
        intent = new Intent(this, ListActivity.class);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    public void BookCrudActivity(View v) {
        this.checkIp();
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

    public void checkIp() {
        ip = this.getIp();
        if(this.ip.matches("")) {
            btnList.setEnabled(false);
            btnNew.setEnabled(false);
            Toast.makeText(this, "Configurar Servidor", Toast.LENGTH_SHORT).show();
        } else {
            btnList.setEnabled(true);
            btnNew.setEnabled(true);
        }
    }
}
