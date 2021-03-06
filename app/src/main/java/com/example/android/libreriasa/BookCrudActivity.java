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
        entrada.putString("flag0", "Listar");
        entrada.putString("flag1", "Cat");
        entrada.putString("url", url);
        asyncGetCat.execute(entrada);
    }

    public void findById(View v) {
        if(this.existsId()) {
            AsyncRestClient asyncFind = new AsyncRestClient(this);
            Bundle b = new Bundle();
            String url = HTTP + ip + BOOK + etId.getText().toString();
            b.putString("flag0", "Buscar");
            b.putString("url", url);
            asyncFind.execute(b);
        } else {
            this.noIdMessage();
        }
    }

    public void bookInsert(View v) {
        this.bookEditInsert(v, "Nuevo", 0);
    }

    public void bookEdit(View v) {
        if(this.existsId()) {
            this.bookEditInsert(v, "Editar",
                    Integer.parseInt(etId.getText().toString()));
        } else {
            this.noIdMessage();
        }
    }

    public void bookEditInsert(View v, String flag, Integer id){
        bookDto = this.book();
        if(bookDto == null) {
            Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT).show();
        } else {
            AsyncRestClient asyncEditInsert = new AsyncRestClient(this);
            Bundle entrada = new Bundle();
            String url = HTTP + ip + BOOK;
            bookDto.setId(id);
            entrada.putString("flag0", flag);
            entrada.putString("url", url);
            entrada.putString("book", Converter.toJson(bookDto).toString());
            asyncEditInsert.execute(entrada);
        }
    }

    public void bookDelete(View v) {
        if(existsId()) {
            AsyncRestClient asyncDelete = new AsyncRestClient(this);
            Bundle entrada = new Bundle();
            String url = HTTP + ip + BOOK +
                    Integer.parseInt(etId.getText().toString());
            entrada.putString("flag0", "Eliminar");
            entrada.putString("url", url);
            asyncDelete.execute(entrada);
        } else {
            this.noIdMessage();
        }
    }

    @Override
    public void update(Bundle output) {
        String flag = output.getString("flag");
        switch(flag) {
            case "buscar":
                this.resolveBuscar(output);
                break;
            case "listaCat":
                this.resolveListCat(output);
                break;
            case "insertado": case "eliminar":
                Toast.makeText(this, output.getString("resultado"), Toast.LENGTH_SHORT).show();
                this.clean();
                break;
        }
        if(!flag.matches("listaCat")) {
            pDialog.dismiss();
        }
    }

    private void resolveBuscar(Bundle output) {
        try {
            JSONObject jsonLibro = new JSONObject(output.getString("book"));
            BookDto libro = Converter.toLibro(jsonLibro);
            if(libro.getName() == null) {
                Toast.makeText(this, "No existe Libro", Toast.LENGTH_SHORT).show();
            } else {
                etId.setEnabled(false);
                etName.setText(libro.getName());
                etCant.setText(Integer.toString(libro.getCant()));
                etCode.setText(libro.getCod());
                etId.setText(Integer.toString(libro.getId()));
                spnCat.setSelection(libro.getCatDto().getId()-1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void resolveListCat(Bundle output){
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
    }

    @Override
    public void progress(String message) {
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("Procesando");
        pDialog.setMessage(message);
        pDialog.setCancelable(true);
        pDialog.setMax(100);
        pDialog.show();
    }

    private BookDto book() {
        String name = etName.getText().toString();
        String  cant = etCant.getText().toString();
        String cod = etCode.getText().toString();

        if(!name.isEmpty() && !cant.isEmpty() && !cod.isEmpty()) {
            bookDto = new BookDto();
            bookDto.setName(name);
            bookDto.setCant(Integer.parseInt(cant));
            bookDto.setCod(cod);
            bookDto.setCatDto(catDto);
        }
        return bookDto;
    }

    private void clean() {
        etName.setText("");
        etCode.setText("");
        etCant.setText("");
        etId.setEnabled(true);
        etId.setText("");
        spnCat.setSelection(0);
    }

    private boolean existsId(){
        boolean id = true;
            if(etId.getText().toString().isEmpty())
                id = false;
        return id;
    }

    private void noIdMessage(){
        Toast.makeText(this, "Falta id", Toast.LENGTH_SHORT).show();
    }
}
