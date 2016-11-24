package com.example.android.libreriasa;

/**
 * Created by Cotroc on 11/8/16.
 */

public class BookDto {

    private int id;
    private String nombre;
    private String codigo;
    private int cantidad;
    private CatDto categoria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public CatDto getCategoria() {
        return categoria;
    }

    public void setCategoria(CatDto categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", cantidad=" + cantidad + '\'' +
                ", categoria='" + categoria  + '\'' +
                '}';
    }


}
