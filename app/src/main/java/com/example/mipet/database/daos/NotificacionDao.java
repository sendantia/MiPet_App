package com.example.mipet.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mipet.database.entities.Enfermedad;
import com.example.mipet.database.entities.Notificacion;

import java.sql.Time;
import java.util.List;

@Dao
public interface NotificacionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNoti(Notificacion notificacion);

    @Query("DELETE FROM Notificacion")
    void deleteALlNoti();

    @Query("DELETE FROM Notificacion WHERE id_cita=(:idCita)")
    void deleteNoti(Integer idCita);

    @Query("SELECT * FROM Notificacion")
    List<Notificacion> getAllNoti();

    @Delete
    void deleteNoti (Notificacion notificacion);

    @Query("SELECT EXISTS(SELECT * FROM Notificacion WHERE id_notificacion=(:idNoti))")
    Boolean isNoti(Integer idNoti);

    @Query("SELECT EXISTS(SELECT * FROM Notificacion WHERE id_cita=(:idCita))")
    Boolean isNotiByIdCita(Integer idCita);

    @Query("SELECT EXISTS(SELECT * FROM Notificacion  inner join Cita " +
            "on Notificacion.id_cita=Cita.id_cita WHERE Cita.clinica=(:nombre) AND Notificacion.fecha_noti=(:fecha))")
    Boolean isNotiCompare(String nombre, String fecha);

    @Query("SELECT hora_noti FROM Notificacion inner join Cita " +
            "on Notificacion.id_cita=Cita.id_cita WHERE Cita.clinica=(:nombre) AND Notificacion.fecha_noti=(:fecha)")
    String timeByNoti(String nombre, String fecha);

    @Query("SELECT EXISTS(SELECT * FROM Notificacion  inner join Cita " +
            "on Notificacion.id_cita=Cita.id_cita WHERE Cita.clinica=(:nombre)" +
            " AND Notificacion.fecha_noti=(:fecha) AND Cita.hora=(:time) AND Cita.motivo=(:motivo) )")
    Boolean isNotification(String nombre, String fecha, Time time, String motivo);


}
