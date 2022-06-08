package com.example.mipet.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Mascota",
        foreignKeys={
                @ForeignKey(
                        entity = Usuario.class,
                        parentColumns = "email",
                        childColumns ="email",
                        onDelete = CASCADE,
                        onUpdate = CASCADE
                )
        }
)
public class Mascota {

    @PrimaryKey(autoGenerate = true) //para que sea autogenerada
    @NonNull
    @ColumnInfo(name = "id_pet")
    private Integer idPet;
    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;
    @NonNull
    @ColumnInfo(name = "tipo")
    private String tipo;
    @NonNull
    @ColumnInfo(name="email")
    private String email;

    public Mascota(@NonNull String nombre, @NonNull String tipo, @NonNull String email) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.email=email;
    }

    public Integer getIdPet() {
        return idPet;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }


    public String getEmail() {
        return email;
    }

    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
