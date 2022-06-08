package com.example.mipet.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mipet.R;
import com.example.mipet.app.adapter.AdaptadorSpinner;
import com.example.mipet.app.adapter.TipoPet;
import com.example.mipet.app.preferences.PrefManager;
import com.example.mipet.database.entities.Mascota;
import com.example.mipet.database.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class RegistroPet extends AppCompatActivity {

    private EditText namePetField;
    private Spinner spinner;
    private AdaptadorSpinner adapterSp;
    private List<TipoPet> listaTipos;
    private Button registrar;
    private ViewModel appView;
    private Mascota pet;
    private String emailUser, passUser, namePet, tipo;
    private PrefManager pref;
    private Intent intent;
    private ImageButton btnClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pet);
        registrar = findViewById(R.id.insertPet);
        namePetField = (EditText) findViewById(R.id.petName);
        btnClear=findViewById(R.id.btn_clear_pet);
        spinner = (Spinner) findViewById(R.id.spiner);
        cargarSpinner();
        adapterSp = new AdaptadorSpinner(this, listaTipos);
        spinner.setAdapter(adapterSp);
        pref = new PrefManager(this);

        //recogemos los datos del usuario
        intent = getIntent();
        emailUser = pref.getEmail();
        passUser = pref.getPass();
        appView = new ViewModel(this.getApplication());

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namePetField.setText("");
                spinner.setSelection(0);

            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    appView.inserPet(pet);
                    Toast.makeText(getApplicationContext(), " Mascota registrada", Toast.LENGTH_LONG).show();
                    starActivity();

                } else {
                    Toast.makeText(getApplicationContext(), " Rellene todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });


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
                starActivity();
            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }

    public void cargarSpinner() {
        listaTipos = new ArrayList<>();
        listaTipos.add(new TipoPet(R.drawable.perro, "Perro"));
        listaTipos.add(new TipoPet(R.drawable.gato, "Gato"));
        listaTipos.add(new TipoPet(R.drawable.pez, "Pez"));
        listaTipos.add(new TipoPet(R.drawable.pajaro, "Pájaro"));
        listaTipos.add(new TipoPet(R.drawable.serpiente, "Réptil"));
        listaTipos.add(new TipoPet(R.drawable.hamster, "Otros mamíferos"));
    }

    private Boolean validateInput() {
        intent = getIntent();
        emailUser = pref.getEmail();
        passUser = pref.getPass();
        namePet = namePetField.getText().toString();
        tipo = listaTipos.get(spinner.getSelectedItemPosition()).getTipo();
        pet = new Mascota(namePet, tipo, emailUser);
        if (namePet.isEmpty() || tipo.isEmpty()) {
            return false;
        }
        return true;
    }

    public void starActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}