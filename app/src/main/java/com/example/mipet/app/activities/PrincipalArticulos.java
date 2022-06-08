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
import com.example.mipet.app.adapter.ArticuloListAdapter;
import com.example.mipet.database.entities.Articulo;
import com.example.mipet.database.viewmodel.ViewModel;

import java.util.List;

public class PrincipalArticulos extends AppCompatActivity implements ArticuloListAdapter.OnDeleteClickListener {
    private static final int NEW_ACTIVITY_REQUEST_CODE3 = 3;
    private Bundle bundle;
    private Integer idPetPass;
    private ImageButton imBtnAddArticle;
    private RecyclerView recicler;
    private ArticuloListAdapter articuloListAdapter;
    private ViewModel appView;
    private TextView txtSmsInicial;
    private String namePet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_articulos);
        bundle = getIntent().getExtras();
        idPetPass = bundle.getInt("ENVIAR ID PET");
        txtSmsInicial=findViewById(R.id.ventana_pet);
        imBtnAddArticle=findViewById(R.id.btn_add_article);
        //reclclerview
        recicler = (RecyclerView) findViewById(R.id.recyclerview2);
        articuloListAdapter = new ArticuloListAdapter(this, this);
        recicler.setAdapter(articuloListAdapter);
        recicler.setLayoutManager(new LinearLayoutManager(this));

        appView = new ViewModel(this.getApplication());
        namePet=appView.getNamePet(idPetPass);
        txtSmsInicial.setText("Artículos de: " +namePet);

        appView.getAllArticleByPet(idPetPass).observe(this, new Observer<List<Articulo>>() {
            @Override
            public void onChanged(List<Articulo> articulos) {
                articuloListAdapter.setArticulos(articulos);
            }
        });

        imBtnAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalArticulos.this,RegistroArticulos.class);
                intent.putExtra("ENVIAR ID PET", idPetPass);
                setResult(Activity.RESULT_OK, intent);
                startActivityForResult(intent, NEW_ACTIVITY_REQUEST_CODE3);
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
    public void OnDeleteClickListener(Articulo articulo) {
        // Code for Delete operation
        appView.deleteArticulo(articulo);
    }
}