package com.example.mipet.app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mipet.R;
import com.example.mipet.app.preferences.PrefManager;
import com.example.mipet.database.entities.Usuario;
import com.example.mipet.database.viewmodel.ViewModel;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UserLogin extends AppCompatActivity {
    private static final int NEW_ACTIVITY_REQUEST_CODE1 = 1;
    private static final String AES = "AES";
    private static final String SHA = "SHA-256";
    private EditText emailField, passField;
    private Button btnInsert, btnLogin, btnInsertNewPass, btnForgetPass, btnVolver;
    private ImageButton ImbtnVerPass;
    private ViewModel appView;
    private String emailUser, password, passEncrypt;
    private Usuario user;
    private Boolean isUser;
    private Bundle bundle;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        emailField = findViewById(R.id.user_email);
        passField = findViewById(R.id.user_pass);
        btnInsert = findViewById(R.id.insert);
        btnLogin = findViewById(R.id.entrar);
        btnInsertNewPass = findViewById(R.id.btn_forget_pass);
        ImbtnVerPass = findViewById(R.id.ver_pass);
        btnForgetPass = findViewById(R.id.forget_pass);
        btnVolver = findViewById(R.id.btn_volver);
        appView = new ViewModel(this.getApplication());

        //si tenemos que aceptar los terminos
        //cuando vuelva queremos siga el email y contrase??a
        try {
            bundle = getIntent().getExtras();
            emailUser = bundle.getString("user");
            password = bundle.getString("pass");
            emailField.setText(emailUser);
            passField.setText(password);


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //hacemos visibles los botones
        btnInsert.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);

        passField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            //EditorInfo:describe varios atributos de un objeto de edicci??n de texto
            //     con el que se est?? comunicando un m??todo de entrada
            //onEditorAction: se llama cuando se realiza una acci??n
            //  devuelve true si has hecho la acci??n
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attemptLogin() == true) {
                    acepTerms();

                }
            }
        });

        if (!new PrefManager(this).isUserLogedOut()) {
            startHomeActivity();
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attemptLogin() == true) {
                    comprobation();
                }

            }
        });

        //para ver la contrase??a al presionar el imageButton
        ImbtnVerPass.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        passField.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        passField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnInsert.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
                btnInsertNewPass.setVisibility(View.VISIBLE);
                btnVolver.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Escriba el email y la nueva contrase??a", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void insertarUsuario() {
        emailUser = emailField.getText().toString();
        password = passField.getText().toString();
        //comprobamos si el email ya existe en la BBDD
        isUser = appView.loginByName(emailUser);
        if (isUser == false) {
            try {
                passEncrypt = encriptar(emailUser, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            user = new Usuario(emailUser, passEncrypt);
            appView.insertUsuario(user);
            saveLoginDetails(emailUser, password);
            Toast.makeText(getApplicationContext(), " Usuario registrado", Toast.LENGTH_LONG).show();
            startHomeActivity();
        } else {
            Toast.makeText(getApplicationContext(), " El usuario ya existe", Toast.LENGTH_LONG).show();
        }


    }

    private void comprobation() {
        //en el metodo saveLogin ya encriptamos la contrase??a
        //pasamos a la siguiente actividad
        emailUser = emailField.getText().toString();
        password = passField.getText().toString();
        //comprobamos si el usuario existe en la BBDD
        //encriptando la contrase??a
        try {
            passEncrypt = encriptar(emailUser, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isUser = appView.login(emailUser, passEncrypt);

        if (isUser == true) {
            //en el metodo savelogin de shared ya encriptamos la contarse??a
            saveLoginDetails(emailUser, password);
            Toast.makeText(getApplicationContext(), " Bienvenid@", Toast.LENGTH_LONG).show();
            startHomeActivity();


        } else {
            Toast.makeText(getApplicationContext(), "usuario o contrase??a incorrecta", Toast.LENGTH_LONG).show();
        }
    }

    private boolean attemptLogin() {
        // posibles errores al introducir los datos
        emailField.setError(null);
        passField.setError(null);

        emailUser = emailField.getText().toString();
        password = passField.getText().toString();
        user = new Usuario(emailUser, password);
        boolean cancel = false;
        View focusView = null;

        // Verifique una contrase??a v??lida, si el usuario ingres?? una.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passField.setError(getString(R.string.error_invalid_password));
            focusView = passField;
            cancel = true;
        }
        // Busque una direcci??n de correo electr??nico v??lida.
        if (TextUtils.isEmpty(emailUser)) {
            emailField.setError(getString(R.string.error_field_required));
            focusView = emailField;
            cancel = true;
        } else if (!isEmailValid(emailUser)) {
            emailField.setError(getString(R.string.error_invalid_email));
            focusView = emailField;
            cancel = true;
        }
        if (cancel) {
            // Hubo un error; no intentes iniciar sesi??n y enfoca el primero
            //campo de formulario con error.
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveLoginDetails(String email, String password) {
        new PrefManager(this).saveLoginDetails(email, password);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }


    private String encriptar(String datos, String usuario) throws Exception {
        SecretKeySpec secretKey = generateKey(usuario);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptadosByte = cipher.doFinal(datos.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosByte, Base64.DEFAULT);
        return datosEncriptadosString;
    }

    private SecretKeySpec generateKey(String usuario) throws Exception {
        //GENERAR UN SHA PARA LA CLAVE (ABREVIATURA ALGORITMO DE HASH SEGURO)
        MessageDigest sha = MessageDigest.getInstance(SHA);
        //pasamos el usuario a bites con el estandar utf8
        byte[] key = usuario.getBytes("UTF-8");
        //llamamos al metodo del sha para que nos completa el calculo de hash
        key = sha.digest(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, AES);
        return secretKey;
    }

    //1??comprobamos que la contrase??a ha sido introducida y correctamente
    //2?? comprobamos si el email se encuentra registrado
    //3?? si existe encriptamos la contrase??a
    //4?? insertamos el usuario
    public void onClickForget(View v) {
        emailUser = emailField.getText().toString();
        password = passField.getText().toString();
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passField.setError(getString(R.string.error_invalid_password));
        } else {
            Boolean userExist = appView.loginByName(emailUser);
            if (userExist) {
                try {
                    passEncrypt = encriptar(emailUser, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                appView.updatePass(emailUser, passEncrypt);
                Toast.makeText(getApplicationContext(), getString(R.string.pass_update), Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.email_no), Toast.LENGTH_LONG).show();

            }
            btnInsert.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btnInsertNewPass.setVisibility(View.GONE);
            btnVolver.setVisibility(View.GONE);
        }


    }

    //metodo para que salga un ventana para aceptar terminos
    public void acepTerms() {
        emailUser = emailField.getText().toString();
        password = passField.getText().toString();
        isUser = appView.loginByName(emailUser);
        if (!isUser) {
            AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
            myBuild.setTitle(getString(R.string.politica));
            myBuild.setMessage(getString(R.string.acepta));
            myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    insertarUsuario();
                }
            });

            myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_condiciones), Toast.LENGTH_LONG).show();
                }
            });
            myBuild.setNeutralButton(getString(R.string.condiciones), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(UserLogin.this, PoliticaPrivacidad.class);
                    intent.putExtra("user", emailUser);
                    intent.putExtra("pass", password);
                    startActivityForResult(intent, NEW_ACTIVITY_REQUEST_CODE1);
                    finish();
                }
            });
            AlertDialog dialog = myBuild.create();
            dialog.show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.ya_registrado), Toast.LENGTH_LONG).show();
        }
    }

    //metodo para que salgan los botones si le damos a olvidar contrase??a
    public void onClickVolver(View v) {
        btnInsert.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        btnInsertNewPass.setVisibility(View.GONE);
        btnVolver.setVisibility(View.GONE);
    }


}