package com.example.mipet.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PrefManager {
    Context context;
    private static final String AES = "AES";
    private static final String SHA = "SHA-256";
    private String passEncrypt;

    public PrefManager(Context context) {
        this.context = context;
    }

    //para guardar nuevos datos de escritura
    public void saveLoginDetails(String email, String password) {
        //Obtenemos el SharedPreferences
        //MODE_PRIVATE: predeterminado, el archivo solo se puede usar en donde se creo
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        //accedemos a su editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //encriptamos las preferencias
        try {

            passEncrypt = encriptar(email, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //añadimos o modificamos las preferencias
        editor.putString("Email", email);
        editor.putString("Password", passEncrypt);
        //llamamos a commit para guardar los cambios
        editor.commit();

    }

    public String getEmail() {
        //añadir un valor por defecto en el caso de que no exista
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("" +
                        "LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Email",
                "");
    }

    public String getPass() {
        //añadir un valor por defecto en el caso de que no exista
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("" +
                        "LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Password",
                "");
    }

    public boolean isUserLogedOut() {
        //comprueba que los datos sean correctos
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("Email",
                "").isEmpty();
        boolean isPasswordEmpty =
                sharedPreferences.getString("Password",
                        "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    public void setLogOut() {
        //borra todos los datos
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
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
        //pasamos el usaurio a bites con el estandar utf8
        byte[] key = usuario.getBytes("UTF-8");
        //llamamos al metodo del sha para que nos completa el calculo de hash
        key = sha.digest(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, AES);
        return secretKey;


    }

}

