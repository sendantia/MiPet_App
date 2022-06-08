package com.example.mipet.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mipet.database.entities.Articulo;

import java.util.List;

@Dao
public interface ArticuloDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertArticulo(Articulo articulo);

    @Query("DELETE FROM Articulo")
    void deleteALlArticulo();

    @Query("SELECT * FROM Articulo")
    List<Articulo> getAllArticulo();

    @Query("SELECT * FROM Articulo WHERE id_pet=(:idPet)")
    LiveData<List<Articulo>> getAllArticuloByPet(Integer idPet);

    @Delete
    void deleteArticulo (Articulo articulo);

    @Query("SELECT EXISTS(SELECT * FROM Articulo WHERE nombre_articulo=(:nombre))")
    Boolean existArticle(String nombre);
}

