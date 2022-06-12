package com.example.mipet.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mipet.R;

public class PoliticaPrivacidad extends AppCompatActivity {
    private static final int NEW_ACTIVITY_REQUEST_CODE4 = 4;
    private Bundle bundle;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidad);
        bundle = getIntent().getExtras();
        email = bundle.getString("user");
        password = bundle.getString("pass");
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
                Intent intent = new Intent(this, UserLogin.class);
                intent.putExtra("user", email);
                intent.putExtra("pass", password);
                startActivityForResult(intent, NEW_ACTIVITY_REQUEST_CODE4);
                startActivity(intent);
                finish();
            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }
}