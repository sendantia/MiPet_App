package com.example.mipet.app.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mipet.R;
import com.example.mipet.database.entities.Articulo;
import com.example.mipet.database.viewmodel.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegistroArticulos extends AppCompatActivity {

    private Bundle bundle;
    private Integer id, periodo;
    private EditText editNameArticle, editPvp;
    private Spinner spUnidad, spTiempo;
    private Button btnRegistrar;
    private ViewModel appView;
    private Articulo articulo;
    private String nombreArticulo, pvpString,unidad,medida,udMedida, fechaString;
    private Float pvp;
    private ImageButton btnLimpiarCampos;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_articulos);

        editNameArticle = findViewById(R.id.nombre_articulo);
        editPvp = findViewById(R.id.pvp_articulo);
        spUnidad = findViewById(R.id.spiner_num);
        spTiempo = findViewById(R.id.spiner_tiempo);
        btnRegistrar = findViewById(R.id.btn_reg_articulo);
        btnLimpiarCampos=findViewById(R.id.btn_clear);
        appView = new ViewModel(this.getApplication());

        //recogemos el id de las preferences
        bundle = getIntent().getExtras();
        id = bundle.getInt("ENVIAR ID PET");

        Integer[] num = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] ud = {"dias", "semanas", "meses", "años"};

        ArrayAdapter<Integer> adapterUd = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, num);
        spUnidad.setAdapter(adapterUd);

        ArrayAdapter<String> adapterT = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ud);
        spTiempo.setAdapter(adapterT);

        btnLimpiarCampos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                editNameArticle.setText("");
                editPvp.setText("");
                spUnidad.setSelection(0);
                spTiempo.setSelection(0);
               /* txtHasta.setVisibility(View.INVISIBLE);
                txtFechaVen.setVisibility(View.INVISIBLE);*/
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (validateName()) {
                    if (validateArticulo()) {
                        appView.insertArticulo(articulo);
                        Toast.makeText(getApplicationContext(), " Articulo registrado registrada", Toast.LENGTH_LONG).show();
                        startActivity();

                    } else {
                        Toast.makeText(getApplicationContext(), " Rellene todos los campos", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


    }

    private Boolean validateArticulo() {
        nombreArticulo = editNameArticle.getText().toString();
        pvpString = editPvp.getText().toString();
        unidad=spUnidad.getSelectedItem().toString();
        medida=spTiempo.getSelectedItem().toString();
        udMedida=unidad +" "+ medida;
        if (pvpString.isEmpty()) {
            return false;
        } else {
            pvp = Float.parseFloat(pvpString);
        }

        fechaString = calculePeriod();
        //calculamos la fecha de vencimiento

        if (nombreArticulo.isEmpty() || pvpString.isEmpty() || udMedida.isEmpty() || id == 0) {
            return false;
        } else {
            articulo = new Articulo(nombreArticulo, udMedida, pvp, id, fechaString);
            //mostramos la fecha
            /*txtHasta.setVisibility(View.VISIBLE);
            txtFechaVen.setVisibility(View.VISIBLE);
            txtFechaVen.setText(fechaString);*/
            return true;
        }


    }

    //calculamos el nº de días
    private String calculePeriod() {
        unidad=spUnidad.getSelectedItem().toString();
        medida=spTiempo.getSelectedItem().toString();
        periodo = Integer.parseInt(unidad);
        calendar = Calendar.getInstance();
        switch (medida){
            case "dias":
                calendar.add(Calendar.DAY_OF_YEAR, periodo);
                break;
            case "semanas":
                int dias=7*periodo;
                calendar.add(Calendar.DAY_OF_YEAR, dias);
                break;
            case "meses":
                calendar.add(Calendar.MONTH, periodo);
                break;
            case "años":
                calendar.add(Calendar.YEAR, periodo);
                break;

        }
        //pasamos la fecha a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        fechaString = dateFormat.format(calendar.getTime());
        return fechaString;

    }

    //Creamos el menú para poder volver a la ventana principal.
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_volver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.volver:
                startActivity();
            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }

    //comprobamos si el artículo ya está registrado
    public Boolean validateName() {
        nombreArticulo = editNameArticle.getText().toString();
        Boolean existe = appView.existArticle(nombreArticulo);
        if (existe) {
            Toast.makeText(getApplicationContext(), "Ya tiene registrado ese artículo", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public void startActivity(){
        Intent intent = new Intent(this, PrincipalArticulos.class);
        intent.putExtra("ENVIAR ID PET", id);
        startActivity(intent);
        finish();
    }

}