package com.example.android.libreriasa;

/**
 * Created by Cotroc on 11/12/16.
 */

public class CatDto {

    private int id;
    private String name;

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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", nombre='" + name + '\'' +
                '}';
    }

}
