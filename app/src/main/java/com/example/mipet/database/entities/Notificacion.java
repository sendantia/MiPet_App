package com.example.mipet.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notificacion",
        foreignKeys = {
            @ForeignKey(
                    entity = Cita.class,
                    parentColumns = "id_cita",
                    childColumns = "id_cita",
                    onDelete = CASCADE,
                    onUpdate = CASCADE
            ),
        })
public class Notificacion {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="id_notificacion")
    private Integer idNotiticacion;

    @NonNull
    @ColumnInfo(name="activado")
    private Boolean isActive;

    @NonNull
    @ColumnInfo(name="hora_noti")
    private String horaNoti;

    @NonNull
    @ColumnInfo(name="fecha_noti")
    private String fechaNoti;

    @NonNull
    @ColumnInfo(name="id_cita")
    private Integer idCita;

    public Notificacion(@NonNull Boolean isActive, @NonNull String horaNoti,
                        @NonNull String fechaNoti, @NonNull Integer idCita) {
        this.isActive = isActive;
        this.horaNoti = horaNoti;
        this.fechaNoti = fechaNoti;
        this.idCita = idCita;
    }

    public Integer getIdNotiticacion() {
        return idNotiticacion;
    }

    public Boolean getActive() {
        return isActive;
    }

    public String getHoraNoti() {
        return horaNoti;
    }

    public String getFechaNoti() {
        return fechaNoti;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdNotiticacion(Integer idNotiticacion) {
        this.idNotiticacion = idNotiticacion;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setHoraNoti(String horaNoti) {
        this.horaNoti = horaNoti;
    }

    public void setFechaNoti(String fechaNoti) {
        this.fechaNoti = fechaNoti;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }
}
