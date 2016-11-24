package com.example.android.libreriasa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cotroc on 11/7/16.
 */

public class ConfigSQLite extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Servicios (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, ip INTEGER)";
    SQLiteDatabase db;

    public ConfigSQLite(Context contexto, String nombre,
                         SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Servicios");
        db.execSQL(sqlCreate);
    }

    public void insertar(String a, String b){
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("nombre", a);
        nuevoRegistro.put("ip", b);
        db = getWritableDatabase();
        db.insert("Servicios", null, nuevoRegistro);
        db.close();
    }

    public Cursor selectAll() {
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Servicios", null);
        return c;
    }

    public void deleteAll() {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Servicios");
        db.close();
    }

    public void upgradeDb(SQLiteDatabase db) {
        sqlCreate = "CREATE TABLE Servicios (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, ip INTEGER)";
        this.onUpgrade(db, 1, 2);
    }

    public void close() {
        db.close();
    }
}
