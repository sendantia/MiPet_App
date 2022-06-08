package com.example.mipet.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="Usuario")
public class Usuario {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private String email;
    @NonNull
    @ColumnInfo(name = "pass")
    private String pass;

    public Usuario(@NonNull String email, @NonNull String pass) {
        this.email = email;
        this.pass = pass;
    }


    public String getEmail() {
        return email;
    }
    public String getPass() {
        return pass;
    }
    public void setEmail(String email) {
        this.email =email;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

}
