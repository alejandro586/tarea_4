package com.example.tarea_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "BaseDeDatos";
    private static final int VERSION = 1;
    private static final String TABLA_PRODUCTOS = "productos";

    public AdminSQLiteOpenHelper(Context context) {
        super(context, NOMBRE_BD, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_PRODUCTOS + " (" +
                "codigo TEXT PRIMARY KEY, " +
                "descripcion TEXT, " +
                "precio REAL, " +
                "stock INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PRODUCTOS);
        onCreate(db);
    }

    // Registrar
    public boolean registrar(String codigo, String desc, double precio, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo", codigo);
        values.put("descripcion", desc);
        values.put("precio", precio);
        values.put("stock", stock);
        long res = db.insert(TABLA_PRODUCTOS, null, values);
        db.close();
        return res != -1;
    }

    // Buscar
    public Cursor buscar(String codigo) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLA_PRODUCTOS + " WHERE codigo=?", new String[]{codigo});
    }

    // Modificar
    public boolean modificar(String codigo, String desc, double precio, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descripcion", desc);
        values.put("precio", precio);
        values.put("stock", stock);
        int res = db.update(TABLA_PRODUCTOS, values, "codigo=?", new String[]{codigo});
        db.close();
        return res > 0;
    }

    // Eliminar
    public boolean eliminar(String codigo) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLA_PRODUCTOS, "codigo=?", new String[]{codigo});
        db.close();
        return res > 0;
    }
}