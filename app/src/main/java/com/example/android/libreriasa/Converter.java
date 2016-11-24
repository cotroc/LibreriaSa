package com.example.android.libreriasa;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cotroc on 11/13/16.
 */

public class Converter {

    public static JSONObject toJson(BookDto bookDto) {

        JSONObject book = new JSONObject();
        JSONObject cat = new JSONObject();

        try {

            book.put("id", bookDto.getId());
            book.put("nombre", bookDto.getName());
            book.put("codigo", bookDto.getCod());
            book.put("cantidad", bookDto.getCant());
            cat.put("id", bookDto.getCatDto().getId());
            cat.put("nombre", bookDto.getCatDto().getName());
            book.put("categoria", cat);


        } catch (JSONException j) {
            j.printStackTrace();
        }

        return book;

    }

    public static BookDto toLibro(JSONObject jsonLibro) {

        BookDto li = new BookDto();
        CatDto cat;

        try {
            li.setId(jsonLibro.getInt("id"));
            li.setName(jsonLibro.getString("nombre"));
            li.setCod(jsonLibro.getString("codigo"));
            li.setCant(Integer.parseInt(jsonLibro.getString("cantidad")));
            JSONObject value = jsonLibro.getJSONObject("categoria");
            cat = Converter.toCategoria(value);
            li.setCatDto(cat);
        } catch (Exception j) {
            j.printStackTrace();
        }

        return li;
    }

    public static CatDto toCategoria(JSONObject jsonCategoria) {
        CatDto cat = new CatDto();
        try{
            cat.setId(jsonCategoria.getInt("id"));
            cat.setName(jsonCategoria.getString("nombre"));
        } catch (Exception j) {
            j.printStackTrace();
        }
        return cat;
    }
}

