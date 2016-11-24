package com.example.android.libreriasa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InsertarActivity extends AppCompatActivity implements SimpleUpdatableActivity{

    private EditText etNombre, etCodigo, etCantidad, etId;
    private Button btnInsertar, btnBuscar, btnEditar, btnBorrar;
    private SpinnAdapter adapter;
    private Spinner spnCat;
    private String ip;
    private final static String HTTP = "http://";
    private final static String LIBRO = "/v1/libro/";
    private final static String CAT = "/v1/categoria/";
    private RestDataCatDto categoria;
    private RestDataLibroDto libro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);
        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        this.initComponents();
    }

    private void initComponents() {
        etNombre = (EditText) findViewById(R.id.etNombre);
        etCodigo = (EditText) findViewById(R.id.etCodigo);
        etCantidad = (EditText) findViewById(R.id.etCantidad);
        etId = (EditText) findViewById(R.id.etId);
        btnInsertar = (Button) findViewById(R.id.btnInsertar);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnBorrar = (Button) findViewById(R.id.btnBorrar);
        spnCat = (Spinner) findViewById(R.id.spnCat);
        this.getCategoria();
    }

    private void getCategoria() {
        AsyncRestClient asyncGetCategoria = new AsyncRestClient(this);
        Bundle entrada = new Bundle();
        String url = HTTP + ip + CAT;
        entrada.putString("flag0", "listar");
        entrada.putString("flag1", "Cat");
        entrada.putString("url", url);
        asyncGetCategoria.execute(entrada);

    }

    public void buscarPorId(View v) {
        AsyncRestClient asyncBuscar = new AsyncRestClient(this);
        Bundle b = new Bundle();
        String url = HTTP + ip + LIBRO + Integer.parseInt(etId.getText().toString());
        b.putString("flag0", "buscar");
        b.putString("url", url);
        asyncBuscar.execute(b);
    }

    public void insertarLibro(View v) {

        this.editInsertBook(v, "insertar", 0);
    }

    public void editarLibro(View v) {

        this.editInsertBook(v, "editar", Integer.parseInt(etId.getText().toString()));
    }

    public void editInsertBook(View v, String flag, Integer id){
        AsyncRestClient asyncEditarLibro = new AsyncRestClient(this);
        Bundle entrada = new Bundle();
        String url = HTTP + ip + LIBRO;
        libro = this.libro();
        libro.setId(id);
        entrada.putString("flag0", flag);
        entrada.putString("url", url);
        entrada.putString("libro", Converter.toJson(libro).toString());
        asyncEditarLibro.execute(entrada);
    }

    public void eliminarLibro(View v) {
        AsyncRestClient asyncEliminarLibro = new AsyncRestClient(this);
        Bundle entrada = new Bundle();
        String url = HTTP + ip + LIBRO + Integer.parseInt(etId.getText().toString());
        entrada.putString("flag0", "eliminar");
        entrada.putString("url", url);
        asyncEliminarLibro.execute(entrada);
    }

    public RestDataLibroDto libro() {
        libro = new RestDataLibroDto();
        libro.setId(0);
        libro.setNombre(etNombre.getText().toString());
        libro.setCantidad(Integer.parseInt(etCantidad.getText().toString()));
        libro.setCodigo(etCodigo.getText().toString());
        libro.setCategoria(categoria);
        return libro;
    }

    public void limpiar() {
        etNombre.setText("");
        etCodigo.setText("");
        etCantidad.setText("");
        etId.setText("");
        spnCat.setSelection(0);
    }

    @Override
    public void update(Bundle salida) {
        Toast toast;
        String flag = salida.getString("flag");
        switch(flag) {

            case "buscar":

                try {
                    JSONObject jsonLibro = new JSONObject(salida.getString("libro"));
                    RestDataLibroDto libro = Converter.toLibro(jsonLibro);
                    if(libro.getNombre() == null) {
                        Toast.makeText(this, "No existe el Libro", Toast.LENGTH_SHORT).show();
                    } else {
                        etNombre.setText(libro.getNombre());
                        etCantidad.setText(Integer.toString(libro.getCantidad()));
                        etCodigo.setText(libro.getCodigo());
                        etId.setText(Integer.toString(libro.getId()));
                        spnCat.setSelection(libro.getCategoria().getId()-1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    break;

            case "listaCat":

                ArrayList cat = salida.getStringArrayList("listaCat");
                int size = cat.size();
                final RestDataCatDto[] lCat = new RestDataCatDto[size];
                for(int i = 0; i < cat.size(); i++) {
                    RestDataCatDto cate = (RestDataCatDto) cat.get(i);
                    lCat[i] = cate;
                }
                adapter = new SpinnAdapter(this, android.R.layout.simple_spinner_item, lCat);
                spnCat = (Spinner) findViewById(R.id.spnCat);
                spnCat.setAdapter(adapter);
                spnCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        RestDataCatDto catDto = adapter.getItem(position);
                        categoria = new RestDataCatDto();
                        categoria.setId(catDto.getId());
                        categoria.setNombre(catDto.getNombre());

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {  }
                });
                break;
            case "insertado": case "eliminar":

                toast = Toast.makeText(this, salida.getString("resultado"), Toast.LENGTH_SHORT);
                toast.show();
                this.limpiar();
                break;

            /*case "eliminar":

                toast = Toast.makeText(this, salida.getString("resultado"), Toast.LENGTH_SHORT);
                toast.show();
                this.limpiar();
                break;*/

        }
    }
}
