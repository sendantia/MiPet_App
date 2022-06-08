package com.example.mipet.database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mipet.database.entities.Articulo;
import com.example.mipet.database.entities.Cita;
import com.example.mipet.database.entities.Enfermedad;
import com.example.mipet.database.entities.Mascota;
import com.example.mipet.database.entities.Notificacion;
import com.example.mipet.database.entities.Usuario;
import com.example.mipet.database.repository.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ViewModel extends AndroidViewModel {
    private final Repository mRepository;
    public Usuario user;
    public Cita cita;
    private Integer idPet;
    public String emailUser;

    public final List<Usuario> mUserList;
    public final LiveData<List<Mascota>> mPetList;
    public final LiveData<List<Mascota>> mPetListUser;
    public final List<Cita> mCitaList;
    public final List<Articulo> mArticuloList;
    public final List<Enfermedad>mEnfermedadList;
    public final List<Notificacion>mNotiList;


    public ViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mUserList = mRepository.getAllUser();
        mPetList = mRepository.getAllPet();
        mCitaList=mRepository.getAllCita();
        mPetListUser=mRepository.getAllPetByUser(emailUser);
        mArticuloList=mRepository.getAllArticulo();
        mEnfermedadList=mRepository.getAllEnfermedad();
        mNotiList=mRepository.getAllNoti();


    }
    public LiveData<List<Mascota>>getAllPetByUser(String emailUser){
        this.emailUser = emailUser;
        return mRepository.getAllPetByUser(emailUser);}

    public String getNamePet(Integer idPet){
        return mRepository.getNamePet(idPet);
    }



    public LiveData<List<Cita>>getAllCitaByPet(Integer idPet){
        this.idPet=idPet;
        return mRepository.gettAllCitaByPet(idPet);
    }

    public LiveData<List<Articulo>>getAllArticleByPet(Integer idPet){
        return mRepository.getAllArticleByPet(idPet);
    }

    public String timeNoti(String nombre, String fecha){
        return mRepository.timeNoti(nombre,fecha);
    }


    public void insertUsuario(Usuario user) {
        mRepository.insertUsuario(user);
    }
    public void inserPet(Mascota pet){mRepository.insertPet(pet);}
    public void insertCita(Cita cita){mRepository.insertCita(cita);}
    public void insertArticulo(Articulo articulo){ mRepository.insertArticulo(articulo);}
    public void insertNoti(Notificacion noti){ mRepository.insertNoti(noti);}


    public void deletePet(Mascota pet){ mRepository.deletePet(pet);}
    public void deleteCita(Cita cita){ mRepository.deleteCita(cita);}
    public void deleteArticulo(Articulo articulo){mRepository.deleteArticulo(articulo);}
    public void deleteUser(Usuario user){ mRepository.deleteUser(user);}
    public void deleteNoti(Integer idCita){ mRepository.deleteNoti(idCita);}

    public Boolean loginByName(String email){ return mRepository.loginUserName(email);}
    public Boolean existArticle(String nombre){ return mRepository.existArticle(nombre);}
    public Boolean login(String email,String pass){
        return mRepository.loginUser(email,pass);
    }
    public Boolean isNoti(Integer idCita){ return mRepository.isNoti(idCita);}
    public Boolean isNotiByIdCita(Integer idCita){ return mRepository.isNotiByIdCita(idCita);}
    public Boolean isNotiCompare(String nombre, String fecha){ return mRepository.isNotiCompare(nombre,fecha);}
    public Boolean existCita(String clinica, Date fecha,Integer idPet){ return mRepository.existCita(clinica,fecha,idPet);}
    public Boolean isNotification(String clinica, String fecha, Time time, String motivo){
        return mRepository.isNotification(clinica,fecha,time,motivo);
    }

    public void updatePass(String email, String newPass){
        mRepository.updatePass(email,newPass);
    }
}
