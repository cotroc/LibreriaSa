package com.example.android.libreriasa;

/**
 * Created by Cotroc on 11/8/16.
 */

public class BookDto {

    private int id;
    private String name;
    private String cod;
    private int cant;
    private CatDto catDto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public CatDto getCatDto() {
        return catDto;
    }

    public void setCatDto(CatDto catDto) {
        this.catDto = catDto;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", nombre='" + name + '\'' +
                ", codigo='" + cod + '\'' +
                ", cantidad=" + cant + '\'' +
                ", categoria='" + catDto + '\'' +
                '}';
    }


}
