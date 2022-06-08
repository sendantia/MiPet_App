package com.example.mipet.app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mipet.R;
import com.example.mipet.app.adapter.PetListAdapter;
import com.example.mipet.app.preferences.PrefManager;
import com.example.mipet.database.entities.Mascota;
import com.example.mipet.database.entities.Usuario;
import com.example.mipet.database.viewmodel.ViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PetListAdapter.OnDeleteClickListener{

    public static final int UPDATE_ACTIVITY_REQUEST_CODE = 1;
    private String emailUser, passUser, saludo;
    private TextView txtSaludo;
    private RecyclerView reciclerView;
    private PetListAdapter petListAdapter;
    private ViewModel appView;
    private PrefManager pref;
    private Intent intent;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSaludo = findViewById(R.id.prueba);


        //recogemos los datos del usuario
        //PARA EL USUARIO , PREFERENCIAS
        pref = new PrefManager(this);
        intent = getIntent();
        emailUser = pref.getEmail();
        passUser = pref.getPass();
        saludo=emailUser.substring(0,emailUser.indexOf("@"));
        txtSaludo.setText(saludo);
        user=new Usuario(emailUser,passUser);

        //reclclerview
        reciclerView = (RecyclerView) findViewById(R.id.recyclerview);
        petListAdapter = new PetListAdapter(this, this);
        reciclerView.setAdapter(petListAdapter);
        reciclerView.setLayoutManager(new LinearLayoutManager(this));

        //abrimos conexion con BBDD
        appView = new ViewModel(this.getApplication());


        //seleccionamos todas las mascotas de cada usuario por el id
        appView.getAllPetByUser(emailUser).observe(this, new Observer<List<Mascota>>() {
            @Override
            public void onChanged( List<Mascota> pets) {
                petListAdapter.setPets(pets);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sali_con_login:
                intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case R.id.cerrar_sesion:
                pref.setLogOut();
                intent = new Intent(this, UserLogin.class);
                startActivity(intent);
                finish();


                return true;

            case R.id.borrarUser:
                //iniciamos un dialogo
                AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
                myBuild.setTitle(getString(R.string.delete_user_dialog));
                myBuild.setMessage(getString(R.string.acept_delete));
                myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        appView.deleteUser(user);
                        pref.setLogOut();
                        Toast.makeText(getApplicationContext(),"El usuario ha sido borrado", Toast.LENGTH_LONG).show();
                        startActivityUser();

                    }
                });

                myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Borrado cancelado", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog dialog = myBuild.create();
                dialog.show();


                return true;
            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }

    public void addPet(View view) {
        Intent intent = new Intent(this, RegistroPet.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void OnDeleteClickListener(Mascota myPet) {
        // Code for Delete operation
        //appView.deletePet(myPet);
        aceptDelete(myPet);
    }

    //dialogo para asegurarse antes de borrar la mascota
    public void aceptDelete(Mascota myPet){
        AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
        myBuild.setTitle(getString(R.string.delete_pet));
        myBuild.setMessage(getString(R.string.acept_delete_pet));
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                appView.deletePet(myPet);
            }
        });

        myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Borrado cancelado", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = myBuild.create();
        dialog.show();

    }
    public void startActivityUser(){
        intent = new Intent(this, UserLogin.class);
        startActivity(intent);
        finish();
    }

}