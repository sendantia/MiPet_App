package com.example.mipet.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mipet.database.entities.Cita;

import java.sql.Date;
import java.util.List;

@Dao
public interface CitaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCita(Cita cita);

    @Query("DELETE FROM Cita")
    void deleteALlCita();
    @Query("SELECT * FROM Cita")
    List<Cita> getAllCita();

    @Delete
    void delete (Cita cita);

    @Query("SELECT * FROM Cita WHERE id_pet=(:idPet)")
    LiveData<List<Cita>> getAllCitaByPet(Integer idPet);

    @Query("SELECT EXISTS( SELECT * FROM Cita WHERE clinica=(:clinica) AND fecha=(:fecha) AND id_pet=(:idPet))")
    Boolean existCita(String clinica, Date fecha, Integer idPet);
}

