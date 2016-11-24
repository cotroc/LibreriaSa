package com.example.android.libreriasa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarActivity extends AppCompatActivity implements SimpleUpdatableActivity {

    public GridView listaLibros;
    public Button listar;
    public EditText etBuscar;
    private static final String TAG = "ListarActivity";
    private AsyncRestClient asyncRestClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        this.initComponents();
    }

    @Override
    public void update(Bundle b) {
        String flag = b.getString("flag");
        RestDataLibroDto libro = new RestDataLibroDto();
        ArrayList resu = new ArrayList();

        if(flag.matches("listaLibros")) {
            ArrayList results = b.getStringArrayList("listaLibros");
            for(int i = 0; i < results.size(); i ++) {
                libro = (RestDataLibroDto)results.get(i);
                resu.add("ID: "+ libro.getId() + "\n Nombre: " + libro.getNombre() + "\n Codigo: " + libro.getCodigo() + "\n Cantidad: " + libro.getCantidad() + "\n\n");

            }

        } if(flag.matches("buscar")) {
            Toast toast = Toast.makeText(this, "entra en buscar", Toast.LENGTH_SHORT);
            toast.show();
            try {
               JSONObject jsonLibro = new JSONObject(b.getString("libro"));
                libro = Converter.toLibro(jsonLibro);
                resu.add("Nombre: " + libro.getNombre() + " Codigo: " + libro.getCodigo() + " Cantidad: " + libro.getCantidad());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Adapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resu);
        listaLibros.setAdapter((ListAdapter) adaptador);



    }

    public void listarLibros(View v) {
        Log.i(TAG, "Enviando peticion");
        Bundle b = new Bundle();
        asyncRestClient = new AsyncRestClient(this);
        String s = etBuscar.getText().toString();
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.show();

        if(s.isEmpty()) {
            b.putString("flag0", "listar");
            b.putString("flag1", "Libros");
            b.putString("url", "http://192.168.1.48:8080/v1/libro/");
            asyncRestClient.execute(b);
        } else {
            b.putString("flag0", "buscar");
            b.putString("url", "http://192.168.1.48:8080/v1/libro/" + etBuscar.getText().toString());
            asyncRestClient.execute(b);
        }


    }

    public void initComponents() {
        listaLibros = (GridView) findViewById(R.id.listaLibros);
        listar = (Button) findViewById(R.id.btnListar);
        etBuscar = (EditText) findViewById(R.id.etBuscar);
    }
}
