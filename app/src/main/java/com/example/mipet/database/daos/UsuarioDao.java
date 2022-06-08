package com.example.mipet.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mipet.database.entities.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(Usuario user);


    @Query("DELETE FROM Usuario")
    void deleteALlUser();

    @Delete
    void delete (Usuario user);

    @Query("SELECT * FROM Usuario")
    List<Usuario> getAllUser();

    @Query("SELECT EXISTS(SELECT * FROM Usuario WHERE email=(:email) and pass=(:pass))")
    Boolean loginUser(String email,String pass);

    @Query("SELECT EXISTS(SELECT * FROM Usuario WHERE email=(:email))")
    Boolean loginUserName(String email);

    @Query("UPDATE Usuario SET pass=(:newPass) WHERE email=(:email)")
    void updatePass(String email, String newPass);
}
