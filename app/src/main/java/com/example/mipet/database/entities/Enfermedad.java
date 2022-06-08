package com.example.mipet.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Enfermedad",
        foreignKeys = {
                @ForeignKey(
                        entity = Mascota.class,
                        parentColumns = "id_pet",
                        childColumns = "id_pet",
                        onDelete = CASCADE,
                        onUpdate = CASCADE
                ),

        }
)
public class Enfermedad {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_enf")
    private Integer idEnfermedad;

    @NonNull
    @ColumnInfo(name = "nombre_enf")
    private String nombreEnfermedad;

    @NonNull
    @ColumnInfo(name="sintoma")
    private String sintoma;

    @NonNull
    @ColumnInfo(name = "gravedad")
    private String gravedad;

    @NonNull
    @ColumnInfo(name = "tratamiento")
    private String tratamiento;

    @NonNull
    @ColumnInfo  (name = "duracion")
    private String duracion;

    @NonNull
    @ColumnInfo (name = "coste")
    private String coste;

    @NonNull
    @ColumnInfo(name = "id_pet")
    private Integer idPet;

    public Enfermedad(@NonNull String nombreEnfermedad, @NonNull String sintoma,
                      @NonNull String gravedad, @NonNull String tratamiento,@NonNull String duracion,
                      @NonNull String coste, @NonNull Integer idPet) {
        this.nombreEnfermedad = nombreEnfermedad;
        this.sintoma = sintoma;
        this.gravedad = gravedad;
        this.tratamiento = tratamiento;
        this.duracion = duracion;
        this.coste = coste;
        this.idPet = idPet;
    }

    public Integer getIdEnfermedad() {
        return idEnfermedad;
    }

    public String getNombreEnfermedad() {
        return nombreEnfermedad;
    }

    public String getSintoma() {
        return sintoma;
    }

    public String getGravedad() {
        return gravedad;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getCoste() {
        return coste;
    }

    public Integer getIdPet() {
        return idPet;
    }

    public void setIdEnfermedad(Integer idEnfermedad) {
        this.idEnfermedad = idEnfermedad;
    }

    public void setNombreEnfermedad(String nombreEnfermedad) {
        this.nombreEnfermedad = nombreEnfermedad;
    }

    public void setSintoma(String sintoma) {
        this.sintoma = sintoma;
    }

    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public void setCoste(String coste) {
        this.coste = coste;
    }

    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }
}

