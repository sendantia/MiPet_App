package com.example.mipet.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Articulo",
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
public class Articulo {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_articulo")
    private Integer idArticulo;

    @NonNull
    @ColumnInfo(name = "nombre_articulo")
    private String nombreArticulo;

    @NonNull
    @ColumnInfo (name = "renovacion")
    private String renovacion;

    @NonNull
    @ColumnInfo (name = "precio")
    private float pvp;

    @NonNull
    @ColumnInfo(name = "id_pet")
    private Integer idPet;

    @NonNull
    @ColumnInfo(name ="fecha_vencimiento")
    private String fechaVencimiento;

    public Articulo(@NonNull String nombreArticulo, @NonNull String renovacion, @NonNull float pvp, @NonNull Integer idPet, @NonNull String fechaVencimiento) {
        this.nombreArticulo = nombreArticulo;
        this.renovacion = renovacion;
        this.pvp = pvp;
        this.idPet = idPet;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public String getRenovacion() {
        return renovacion;
    }

    public float getPvp() {
        return pvp;
    }

    public Integer getIdPet() {
        return idPet;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public void setRenovacion(String renovacion) {
        this.renovacion = renovacion;
    }

    public void setPvp(float pvp) {
        this.pvp = pvp;
    }

    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

}
