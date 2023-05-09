package com.example.ejercicio2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {



    EditText editText;
    Button addButton,deleteButton;
    TableLayout tableLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        editText = findViewById(R.id.editTextTextPersonName);
        deleteButton = findViewById(R.id.button2);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Borrar ciudades de un país");

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String pais = input.getText().toString();

                        SQLiteDatabase mydatabase = openOrCreateDatabase("app", MODE_PRIVATE, null);
                        mydatabase.execSQL("DELETE FROM Capitales WHERE Pais='" + pais + "'");
                        buscarCapital("");
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();
            }
        });



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No se utiliza
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String capital = charSequence.toString();
                buscarCapital(capital);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No se utiliza
            }
        });

        addButton = findViewById(R.id.button);
        tableLayout = findViewById(R.id.tableLayout);

        // Agregar encabezados de tabla
        TableRow tableRowHeader = new TableRow(this);
        tableRowHeader.setBackgroundColor(Color.parseColor("#3F51B5"));
        tableRowHeader.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView capitalHeader = new TextView(this);
        capitalHeader.setTextColor(Color.WHITE);
        capitalHeader.setText("Capital");
        capitalHeader.setPadding(5, 5, 5, 5);
        tableRowHeader.addView(capitalHeader);

        TextView paisHeader = new TextView(this);
        paisHeader.setTextColor(Color.WHITE);
        paisHeader.setText("País");
        paisHeader.setPadding(5, 5, 5, 5);
        tableRowHeader.addView(paisHeader);

        TextView poblacionHeader = new TextView(this);
        poblacionHeader.setTextColor(Color.WHITE);
        poblacionHeader.setText("Población");
        poblacionHeader.setPadding(5, 5, 5, 5);
        tableRowHeader.addView(poblacionHeader);

        tableLayout.addView(tableRowHeader);


        //Creamos y obtenemos DB y Tabla al iniciar app
        SQLiteDatabase mydatabase = openOrCreateDatabase("app", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Capitales (id INTEGER PRIMARY KEY AUTOINCREMENT, Capital VARCHAR, Pais VARCHAR, Poblacion VARCHAR);");
        Cursor cursor = mydatabase.rawQuery("SELECT * FROM Capitales", null);

        if (cursor.moveToFirst()) {
            do {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                TextView capitalText = new TextView(this);
                String capital = cursor.getString(cursor.getColumnIndexOrThrow("Capital"));
                capitalText.setText(capital);
                capitalText.setPadding(5, 5, 5, 5);
                tableRow.addView(capitalText);

                TextView paisText = new TextView(this);
                String pais = cursor.getString(cursor.getColumnIndexOrThrow("Pais"));
                paisText.setText(pais);
                paisText.setPadding(5, 5, 5, 5);
                tableRow.addView(paisText);

                TextView poblacionText = new TextView(this);
                String poblacion = cursor.getString(cursor.getColumnIndexOrThrow("Poblacion"));
                poblacionText.setText(poblacion);
                poblacionText.setPadding(5, 5, 5, 5);
                tableRow.addView(poblacionText);

                TextView idText = new TextView(this);
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                idText.setText(id);
                idText.setVisibility(View.GONE); // Ocultar el valor del id en la vista
                tableRow.addView(idText);


                tableLayout.addView(tableRow);


                setListener(tableRow);


            } while (cursor.moveToNext());
        }
        cursor.close();
        mydatabase.close();





        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Agregar ciudad");

                // Layout personalizado para el diálogo de alerta
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText inputCapital = new EditText(MainActivity.this);
                inputCapital.setHint("Capital");
                layout.addView(inputCapital);

                final EditText inputPais = new EditText(MainActivity.this);
                inputPais.setHint("País");
                layout.addView(inputPais);

                final EditText inputPoblacion = new EditText(MainActivity.this);
                inputPoblacion.setHint("Población");
                inputPoblacion.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(inputPoblacion);

                builder.setView(layout);

                // Acción para el botón Agregar del diálogo de alerta
                builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener los valores ingresados en los campos
                        String capital = inputCapital.getText().toString().trim();
                        String pais = inputPais.getText().toString().trim();
                        String poblacionStr = inputPoblacion.getText().toString().trim();

                        // Validar que los campos no estén vacíos
                        if (TextUtils.isEmpty(capital) || TextUtils.isEmpty(pais) || TextUtils.isEmpty(poblacionStr)) {
                            Toast.makeText(MainActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Convertir la población a un valor numérico
                        int poblacion = Integer.parseInt(poblacionStr);

                        // Insertar los datos en la base de datos
                        SQLiteDatabase database = openOrCreateDatabase("app", MODE_PRIVATE, null);
                        String sql = "INSERT INTO Capitales (Capital, Pais, Poblacion) VALUES (?, ?, ?)";
                        database.execSQL(sql, new String[]{capital, pais, String.valueOf(poblacion)});

                        // Refrescar la tabla para mostrar los nuevos datos
                        refreshTable();
                    }
                });

                builder.setNegativeButton("Cancelar", null);

                builder.show();
            }
        });



    }



    //Refrescamos tabla con valores
    private void refreshTable() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews(); // Eliminar todas las filas existentes en la tabla

        // Crear una nueva fila para los encabezados de la tabla
        TableRow headerRow = new TableRow(MainActivity.this);
        headerRow.setBackgroundColor(Color.parseColor("#3F51B5"));

        TextView capitalHeader = new TextView(MainActivity.this);
        capitalHeader.setText("Capital");
        capitalHeader.setTextColor(Color.WHITE);
        capitalHeader.setPadding(5, 5, 5, 5);
        headerRow.addView(capitalHeader);

        TextView countryHeader = new TextView(MainActivity.this);
        countryHeader.setText("País");
        countryHeader.setTextColor(Color.WHITE);
        countryHeader.setPadding(5, 5, 5, 5);
        headerRow.addView(countryHeader);

        TextView populationHeader = new TextView(MainActivity.this);
        populationHeader.setText("Población");
        populationHeader.setTextColor(Color.WHITE);
        populationHeader.setPadding(5, 5, 5, 5);
        headerRow.addView(populationHeader);



        tableLayout.addView(headerRow); // Agregar la fila de encabezados a la tabla

        // Obtener los datos de la tabla y agregar una fila para cada registro
        SQLiteDatabase mydatabase = openOrCreateDatabase("app", MODE_PRIVATE, null);
        Cursor cursor = mydatabase.rawQuery("SELECT * FROM Capitales", null);

        if (cursor.moveToFirst()) {
            do {
                String capital = cursor.getString(cursor.getColumnIndexOrThrow("Capital"));
                String country = cursor.getString(cursor.getColumnIndexOrThrow("Pais"));
                String population = cursor.getString(cursor.getColumnIndexOrThrow("Poblacion"));

                TableRow row = new TableRow(MainActivity.this);

                TextView capitalText = new TextView(MainActivity.this);
                capitalText.setText(capital);
                capitalText.setTextColor(Color.BLACK);
                capitalText.setPadding(5, 5, 5, 5);
                row.addView(capitalText);

                TextView countryText = new TextView(MainActivity.this);
                countryText.setText(country);
                countryText.setTextColor(Color.BLACK);
                countryText.setPadding(5, 5, 5, 5);
                row.addView(countryText);

                TextView populationText = new TextView(MainActivity.this);
                populationText.setText(population);
                populationText.setTextColor(Color.BLACK);
                populationText.setPadding(5, 5, 5, 5);
                row.addView(populationText);

                TextView idText = new TextView(this);
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                idText.setText(id);
                idText.setVisibility(View.GONE); // Ocultar el valor del id en la vista
                row.addView(idText);

                tableLayout.addView(row); // Agregar la fila a la tabla

                setListener(row);


            } while (cursor.moveToNext());
        }

        cursor.close();
        mydatabase.close();
    }


    private void setListener(TableRow row){
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los datos de la fila
                String capital = ((TextView) row.getChildAt(0)).getText().toString();
                String pais = ((TextView) row.getChildAt(1)).getText().toString();
                String poblacion = ((TextView) row.getChildAt(2)).getText().toString();

                row.setBackgroundColor(Color.parseColor("#A6B6D1"));

                // Crear un AlertDialog con los datos de la fila y las opciones para modificar o eliminar
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(capital);
                builder.setMessage("País: " + pais + "\n" + "Población: " + poblacion);
                builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Crear el formulario dentro del AlertDialog
                        LinearLayout layout = new LinearLayout(MainActivity.this);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        EditText capitalEditText = new EditText(MainActivity.this);
                        capitalEditText.setText(capital);
                        capitalEditText.setHint("Capital");
                        layout.addView(capitalEditText);

                        EditText paisEditText = new EditText(MainActivity.this);
                        paisEditText.setText(pais);
                        paisEditText.setHint("País");
                        layout.addView(paisEditText);

                        EditText poblacionEditText = new EditText(MainActivity.this);
                        poblacionEditText.setText(poblacion);
                        poblacionEditText.setHint("Población");
                        layout.addView(poblacionEditText);

                        // Mostrar el formulario en el AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Editar registro");
                        builder.setView(layout);
                        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Obtener los nuevos datos del formulario
                                String newCapital = capitalEditText.getText().toString();
                                String newPais = paisEditText.getText().toString();
                                String newPoblacion = poblacionEditText.getText().toString();

// Actualizar la fila en la tabla
                                ((TextView) row.getChildAt(0)).setText(newCapital);
                                ((TextView) row.getChildAt(1)).setText(newPais);
                                ((TextView) row.getChildAt(2)).setText(newPoblacion);

// Actualizar los datos en la base de datos
                                SQLiteDatabase mydatabase = openOrCreateDatabase("app", MODE_PRIVATE, null);
                                int id = Integer.parseInt(((TextView) row.getChildAt(3)).getText().toString());

                                String updateQuery = "UPDATE Capitales SET Capital = '" + newCapital + "', Pais = '" + newPais + "', Poblacion = '" + newPoblacion + "' WHERE id = " + id + ";";
                                mydatabase.execSQL(updateQuery);

                                refreshTable();
                                row.setBackgroundColor(Color.WHITE);
                                Toast.makeText(getApplicationContext(), "Registro editado correctamente.", Toast.LENGTH_SHORT).show();

                            }
                        });
                        builder.setNegativeButton("Cancelar", null);
                        builder.show();
                    }
                });

                builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Obtener el id de la fila seleccionada
                        int id = Integer.parseInt(((TextView) row.getChildAt(3)).getText().toString());

                        // Eliminar la fila de la tabla
                        tableLayout.removeView(row);

                        // Eliminar la capital correspondiente de la base de datos
                        SQLiteDatabase mydatabase = openOrCreateDatabase("app", MODE_PRIVATE, null);
                        mydatabase.execSQL("DELETE FROM Capitales WHERE id = " + id + ";");

                        row.setBackgroundColor(Color.WHITE);

                        Toast.makeText(getApplicationContext(), "Registro eliminado correctamente.", Toast.LENGTH_SHORT).show();

                    }
                });


                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        row.setBackgroundColor(Color.WHITE);



                    }
                });



                // Mostrar el AlertDialog
                builder.show();
            }
        });

    }


    private void buscarCapital(String capitalBuscada){

// Abrir la base de datos en modo lectura
        SQLiteDatabase mydatabase = openOrCreateDatabase("app", MODE_PRIVATE, null);
        Cursor cursor = mydatabase.rawQuery("SELECT * FROM Capitales WHERE Capital LIKE '%" + capitalBuscada + "%'", null);

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews(); // Eliminar todas las filas existentes en la tabla

        // Crear una nueva fila para los encabezados de la tabla
        TableRow headerRow = new TableRow(MainActivity.this);
        headerRow.setBackgroundColor(Color.parseColor("#3F51B5"));

        TextView capitalHeader = new TextView(MainActivity.this);
        capitalHeader.setText("Capital");
        capitalHeader.setTextColor(Color.WHITE);
        capitalHeader.setPadding(5, 5, 5, 5);
        headerRow.addView(capitalHeader);

        TextView countryHeader = new TextView(MainActivity.this);
        countryHeader.setText("País");
        countryHeader.setTextColor(Color.WHITE);
        countryHeader.setPadding(5, 5, 5, 5);
        headerRow.addView(countryHeader);

        TextView populationHeader = new TextView(MainActivity.this);
        populationHeader.setText("Población");
        populationHeader.setTextColor(Color.WHITE);
        populationHeader.setPadding(5, 5, 5, 5);
        headerRow.addView(populationHeader);


        tableLayout.addView(headerRow); // Agregar la fila de encabezados a la tabla

// Recorrer los resultados de la consulta y mostrarlos en la tabla
        if (cursor.moveToFirst()) {

            do {


                TableRow tableRow = new TableRow(MainActivity.this);

                TextView capitalText = new TextView(this);
                capitalText.setTextColor(Color.BLACK);
                String capital = cursor.getString(cursor.getColumnIndexOrThrow("Capital"));
                capitalText.setText(capital);
                capitalText.setPadding(5, 5, 5, 5);
                tableRow.addView(capitalText);

                TextView paisText = new TextView(this);
                paisText.setTextColor(Color.BLACK);
                String pais = cursor.getString(cursor.getColumnIndexOrThrow("Pais"));
                paisText.setText(pais);
                paisText.setPadding(5, 5, 5, 5);
                tableRow.addView(paisText);

                TextView poblacionText = new TextView(this);
                poblacionText.setTextColor(Color.BLACK);
                String poblacion = cursor.getString(cursor.getColumnIndexOrThrow("Poblacion"));
                poblacionText.setText(poblacion);
                poblacionText.setPadding(5, 5, 5, 5);
                tableRow.addView(poblacionText);

                TextView idText = new TextView(this);
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                idText.setText(id);
                idText.setVisibility(View.GONE);
                tableRow.addView(idText);

                tableLayout.addView(tableRow);
                setListener(tableRow);
            } while (cursor.moveToNext());
        }
        else {
        }

    }



}