package com.example.tarea_4;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etCodigo, etDescripcion, etPrecio, etStock;
    Button btnRegistrar, btnBuscar, btnModificar, btnEliminar;
    AdminSQLiteOpenHelper admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCodigo = findViewById(R.id.etCodigo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);

        admin = new AdminSQLiteOpenHelper(this);

        btnRegistrar.setOnClickListener(v -> registrar());
        btnBuscar.setOnClickListener(v -> buscar());
        btnModificar.setOnClickListener(v -> modificar());
        btnEliminar.setOnClickListener(v -> eliminar());
    }

    private void registrar() {
        String codigo = etCodigo.getText().toString().trim();
        String desc = etDescripcion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();

        // Validación completa antes de parsear
        if (codigo.isEmpty() || desc.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Complete TODOS los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        int stock;
        try {
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio o stock inválido (solo números)", Toast.LENGTH_LONG).show();
            return;
        }

        // Validaciones extras opcionales pero buenas
        if (precio <= 0) {
            Toast.makeText(this, "Precio debe ser mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }
        if (stock < 0) {
            Toast.makeText(this, "Stock no puede ser negativo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (admin.registrar(codigo, desc, precio, stock)) {
            Toast.makeText(this, "Producto registrado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al registrar (código ya existe?)", Toast.LENGTH_LONG).show();
        }
    }

    private void buscar() {
        String codigo = etCodigo.getText().toString().trim();
        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese código", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = admin.buscar(codigo);
        if (cursor.moveToFirst()) {
            etDescripcion.setText(cursor.getString(1));
            etPrecio.setText(String.valueOf(cursor.getDouble(2)));
            etStock.setText(String.valueOf(cursor.getInt(3)));
            Toast.makeText(this, "Producto encontrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    private void modificar() {
        String codigo = etCodigo.getText().toString().trim();
        String desc = etDescripcion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();

        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese el código para modificar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si no se modifican precio/stock, puedes permitir vacío, pero por seguridad validamos
        double precio = 0;
        int stock = 0;
        boolean precioModificado = !precioStr.isEmpty();
        boolean stockModificado = !stockStr.isEmpty();

        try {
            if (precioModificado) precio = Double.parseDouble(precioStr);
            if (stockModificado) stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio o stock inválido (solo números)", Toast.LENGTH_LONG).show();
            return;
        }

        // Si no se cambió precio/stock, mantener los valores actuales (pero para simplicidad asumimos que se ingresan)
        if (admin.modificar(codigo, desc, precioModificado ? precio : 0, stockModificado ? stock : 0)) {
            Toast.makeText(this, "Producto modificado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al modificar (código no existe?)", Toast.LENGTH_LONG).show();
        }
    }

    private void eliminar() {
        String codigo = etCodigo.getText().toString().trim();
        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese código", Toast.LENGTH_SHORT).show();
            return;
        }

        if (admin.eliminar(codigo)) {
            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_LONG).show();
        }
    }

    private void limpiarCampos() {
        etCodigo.setText("");
        etDescripcion.setText("");
        etPrecio.setText("");
        etStock.setText("");
    }
}