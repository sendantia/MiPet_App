package com.example.mipet.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mipet.database.entities.Enfermedad;

import java.util.List;

@Dao
public interface EnfermedadDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEnfermedad(Enfermedad enfermedad);

    @Query("DELETE FROM Enfermedad")
    void deleteALlEnfermedad();

    @Query("SELECT * FROM Enfermedad")
    List<Enfermedad> getAllEnfermedad();

    @Query("SELECT * FROM Enfermedad WHERE id_pet=(:idPet)")
    List<Enfermedad> getAllEnfermedadByPet(Integer idPet);

    @Delete
    void deleteEnfermedad (Enfermedad enfermedad);

}
