package com.example.mipet.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mipet.R;
import com.example.mipet.app.adapter.CitaListAdapter;
import com.example.mipet.database.entities.Cita;
import com.example.mipet.database.viewmodel.ViewModel;

import java.util.List;

public class PrincipalCitas extends AppCompatActivity implements CitaListAdapter.OnDeleteClickListener {
    private static final int NEW_ACTIVITY_REQUEST_CODE2 = 2;
    private TextView txtNamePet;
    private Integer idPetPass;
    private Bundle bundle;
    private ImageButton regCita;
    private RecyclerView recicler;
    private CitaListAdapter citaListAdapter;
    private ViewModel appView;
    private String namePet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_citas);
        txtNamePet = findViewById(R.id.txt_name_pet);
        regCita = findViewById(R.id.btn_add_cita);
        bundle = getIntent().getExtras();
        idPetPass = bundle.getInt("ENVIAR ID PET");


        //reclclerview
        recicler = (RecyclerView) findViewById(R.id.recyclerview2);
        citaListAdapter = new CitaListAdapter(this, this);
        recicler.setAdapter(citaListAdapter);
        recicler.setLayoutManager(new LinearLayoutManager(this));

        appView = new ViewModel(this.getApplication());
        namePet=appView.getNamePet(idPetPass);
        txtNamePet.setText("Citas de "  +namePet);
        appView.getAllCitaByPet(idPetPass).observe(this, new Observer<List<Cita>>() {
            @Override
            public void onChanged(List<Cita> citas) {
                citaListAdapter.setClinicas(citas);
            }
        });


        regCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalCitas.this,RegistroCitas.class);
                intent.putExtra("ENVIAR ID PET", idPetPass);
                setResult(Activity.RESULT_OK, intent);
                startActivityForResult(intent, NEW_ACTIVITY_REQUEST_CODE2);
                finish();
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
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("ENVIAR ID PET", idPetPass);
                startActivity(intent);
                finish();
            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void OnDeleteClickListener(Cita cita) {
        // Code for Delete operation
        appView.deleteCita(cita);
    }
}