package com.example.mipet.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mipet.database.entities.Mascota;

import java.util.List;

@Dao
public interface MascotaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPet(Mascota pet);

    @Query("DELETE FROM Mascota")
    void deleteALlPet();
    @Query("SELECT * FROM Mascota")
    LiveData<List<Mascota>> getAllPet();

    @Delete
    void delete (Mascota pet);

    @Query("SELECT * FROM Mascota WHERE email=(:email)")
    LiveData<List<Mascota>>getAllPetByUser(String email);

    @Query("SELECT nombre FROM Mascota WHERE id_pet=(:idPet)")
    String getNamePet(Integer idPet);
}

