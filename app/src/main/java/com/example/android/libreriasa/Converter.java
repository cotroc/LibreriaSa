package com.example.android.libreriasa;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cotroc on 11/13/16.
 */

public class Converter {

    public static JSONObject toJson(BookDto libro) {

        JSONObject li = new JSONObject();
        JSONObject cat = new JSONObject();

        try {

            li.put("id", libro.getId());
            li.put("nombre", libro.getNombre());
            li.put("codigo", libro.getCodigo());
            li.put("cantidad", libro.getCantidad());
            cat.put("id", libro.getCategoria().getId());
            cat.put("nombre", libro.getCategoria().getNombre());
            li.put("categoria", cat);


        } catch (JSONException j) {
            j.printStackTrace();
        }

        return li;

    }

    public static BookDto toLibro(JSONObject jsonLibro) {

        BookDto li = new BookDto();
        CatDto cat;

        try {
            li.setId(jsonLibro.getInt("id"));
            li.setNombre(jsonLibro.getString("nombre"));
            li.setCodigo(jsonLibro.getString("codigo"));
            li.setCantidad(Integer.parseInt(jsonLibro.getString("cantidad")));
            JSONObject value = jsonLibro.getJSONObject("categoria");
            cat = Converter.toCategoria(value);
            li.setCategoria(cat);
        } catch (Exception j) {
            j.printStackTrace();
        }

        return li;
    }

    public static CatDto toCategoria(JSONObject jsonCategoria) {
        CatDto cat = new CatDto();
        try{
            cat.setId(jsonCategoria.getInt("id"));
            cat.setNombre(jsonCategoria.getString("nombre"));
        } catch (Exception j) {
            j.printStackTrace();
        }
        return cat;
    }
}

