package com.example.android.libreriasa;

import android.app.ProgressDialog;
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

public class BookCrudActivity extends AppCompatActivity implements SimpleUpdatableActivity{

    private EditText etName, etCode, etCant, etId;
    private Button btnInsert, btnSearch, btnEdit, btnDel;
    private SpinnAdapter adapter;
    private Spinner spnCat;
    private String ip;
    private final static String HTTP = "http://";
    private final static String BOOK = "/v1/libro/";
    private final static String CAT = "/v1/categoria/";
    private ProgressDialog pDialog;
    private CatDto catDto;
    private BookDto bookDto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);
        pDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        this.initComponents();
    }

    private void initComponents() {
        etName = (EditText) findViewById(R.id.etNombre);
        etCode = (EditText) findViewById(R.id.etCodigo);
        etCant = (EditText) findViewById(R.id.etCantidad);
        etId = (EditText) findViewById(R.id.etId);
        btnInsert = (Button) findViewById(R.id.btnInsertar);
        btnSearch = (Button) findViewById(R.id.btnBuscar);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDel = (Button) findViewById(R.id.btnDel);
        spnCat = (Spinner) findViewById(R.id.spnCat);
        this.getCat();
    }

    private void getCat() {
        AsyncRestClient asyncGetCat = new AsyncRestClient(this);
        Bundle entrada = new Bundle();
        String url = HTTP + ip + CAT;
        entrada.putString("flag0", "listar");
        entrada.putString("flag1", "Cat");
        entrada.putString("url", url);
        asyncGetCat.execute(entrada);

    }

    public void findById(View v) {
        AsyncRestClient asyncFind = new AsyncRestClient(this);
        Bundle b = new Bundle();
        String url = HTTP + ip + BOOK +
                Integer.parseInt(etId.getText().toString());
        b.putString("flag0", "buscar");
        b.putString("url", url);
        asyncFind.execute(b);
    }

    public void bookInsert(View v) {

        this.bookEditInsert(v, "insertar", 0);
    }

    public void bookEdit(View v) {

        this.bookEditInsert(v, "editar",
                Integer.parseInt(etId.getText().toString()));
    }

    public void bookEditInsert(View v, String flag, Integer id){
        AsyncRestClient asyncEditInsert = new AsyncRestClient(this);
        Bundle entrada = new Bundle();
        String url = HTTP + ip + BOOK;
        bookDto = this.book();
        bookDto.setId(id);
        entrada.putString("flag0", flag);
        entrada.putString("url", url);
        entrada.putString("book", Converter.toJson(bookDto).toString());
        asyncEditInsert.execute(entrada);
    }

    public void bookDelete(View v) {
        AsyncRestClient asyncDelete = new AsyncRestClient(this);
        Bundle entrada = new Bundle();
        String url = HTTP + ip + BOOK +
                Integer.parseInt(etId.getText().toString());
        entrada.putString("flag0", "eliminar");
        entrada.putString("url", url);
        asyncDelete.execute(entrada);
    }

    public BookDto book() {
        bookDto = new BookDto();
        bookDto.setName(etName.getText().toString());
        bookDto.setCant(Integer.parseInt(etCant.getText().toString()));
        bookDto.setCod(etCode.getText().toString());
        bookDto.setCatDto(catDto);
        return bookDto;
    }

    public void clean() {
        etName.setText("");
        etCode.setText("");
        etCant.setText("");
        etId.setText("");
        spnCat.setSelection(0);
    }

    @Override
    public void update(Bundle output) {
        Toast toast;
        String flag = output.getString("flag");
        switch(flag) {

            case "buscar":

                try {
                    JSONObject jsonLibro = new JSONObject(output.getString("book"));
                    BookDto libro = Converter.toLibro(jsonLibro);
                    if(libro.getName() == null) {
                        Toast.makeText(this, "No existe el Libro", Toast.LENGTH_SHORT).show();
                    } else {
                        etName.setText(libro.getName());
                        etCant.setText(Integer.toString(libro.getCant()));
                        etCode.setText(libro.getCod());
                        etId.setText(Integer.toString(libro.getId()));
                        spnCat.setSelection(libro.getCatDto().getId()-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    break;

            case "listaCat":

                ArrayList cat = output.getStringArrayList("listaCat");
                int size = cat.size();
                final CatDto[] lCat = new CatDto[size];
                for(int i = 0; i < cat.size(); i++) {
                    CatDto cate = (CatDto) cat.get(i);
                    lCat[i] = cate;
                }
                adapter = new SpinnAdapter(this, android.R.layout.simple_spinner_item, lCat);
                spnCat = (Spinner) findViewById(R.id.spnCat);
                spnCat.setAdapter(adapter);
                spnCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        CatDto catDto = adapter.getItem(position);
                        BookCrudActivity.this.catDto = new CatDto();
                        BookCrudActivity.this.catDto.setId(catDto.getId());
                        BookCrudActivity.this.catDto.setName(catDto.getName());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {  }
                });
                break;
            case "insertado": case "eliminar":

                toast = Toast.makeText(this, output.getString("resultado"), Toast.LENGTH_SHORT);
                toast.show();
                this.clean();
                break;
        }
    }

    /*public void progressDialog(String message) {
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage(message);
        pDialog.setCancelable(true);
        pDialog.setMax(100);
        pDialog.show();
    }*/
}
