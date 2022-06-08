package com.example.mipet.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Time;

@Entity(tableName = "Cita",
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

public class Cita {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_cita")
    private Integer idCita;

    @NonNull
    @ColumnInfo(name = "fecha")
    private Date fecha;

    @NonNull
    @ColumnInfo(name = "hora")
    private Time hora;

    @NonNull
    @ColumnInfo(name = "clinica")
    private String clinica;

    @NonNull
    @ColumnInfo(name = "motivo")
    private String motivo;

    @NonNull
    @ColumnInfo(name = "id_pet")
    private Integer idPet;


    public Cita(@NonNull Date fecha, @NonNull Time hora, @NonNull String clinica,
                @NonNull String motivo, @NonNull Integer idPet) {
        this.fecha = fecha;
        this.hora = hora;
        this.clinica = clinica;
        this.idPet = idPet;
        this.motivo = motivo;

    }


    public Integer getIdCita() {
        return idCita;
    }


    public Date getFecha() {
        return fecha;
    }


    public Time getHora() {
        return hora;
    }

    public String getClinica() {
        return clinica;
    }

    public Integer getIdPet() {
        return idPet;
    }


    public String getMotivo() {return motivo;}

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public void setClinica(String clinica) {
        this.clinica = clinica;
    }

    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public void setMotivo(String motivo) {this.motivo = motivo;}
}
