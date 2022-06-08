package com.example.mipet.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mipet.database.AppDatabase;
import com.example.mipet.database.daos.ArticuloDao;
import com.example.mipet.database.daos.CitaDao;
import com.example.mipet.database.daos.EnfermedadDao;
import com.example.mipet.database.daos.MascotaDao;
import com.example.mipet.database.daos.NotificacionDao;
import com.example.mipet.database.daos.UsuarioDao;
import com.example.mipet.database.entities.Articulo;
import com.example.mipet.database.entities.Cita;
import com.example.mipet.database.entities.Enfermedad;
import com.example.mipet.database.entities.Mascota;
import com.example.mipet.database.entities.Notificacion;
import com.example.mipet.database.entities.Usuario;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class Repository {
    private UsuarioDao mUserDao;
    private MascotaDao mPetDao;
    private CitaDao mCitaDao;
    private ArticuloDao mArticuloDao;
    private EnfermedadDao mEnfermedadDao;
    private NotificacionDao mNotiDao;

    private List<Usuario> mUserList;
    private final LiveData<List<Mascota>> mPetList;
    private final LiveData<List<Mascota>> mPetListUser;
    private List<Cita> mCitaList;
    private LiveData<List<Cita>> mCitaByPet;
    private List<Articulo> mArticuloList;
    private LiveData<List<Articulo>> mArticleByPet;
    private List<Enfermedad>mEnfermedadList;
    private List<Notificacion>mNotiList;
    private Usuario user;
    public String emailUser;
    private Integer idPet;


    public Repository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        //accedemos a los dao de la clase database
        mUserDao = db.getUsuarioDao();
        mPetDao = db.getMascotaDao();
        mCitaDao = db.getCitaDao();
        mArticuloDao = db.getArticuloDao();
        mEnfermedadDao=db.getEnfermedadDao();
        mNotiDao=db.getNotificacionDao();


        //accedemos a los metodos de los DAOs
        mUserList = mUserDao.getAllUser();
        mPetList = mPetDao.getAllPet();
        mCitaList = mCitaDao.getAllCita();
        mArticuloList = mArticuloDao.getAllArticulo();
        mEnfermedadList=mEnfermedadDao.getAllEnfermedad();
        mPetListUser = mPetDao.getAllPetByUser(emailUser);
        mCitaByPet = mCitaDao.getAllCitaByPet(idPet);
        mArticleByPet = mArticuloDao.getAllArticuloByPet(idPet);
        mNotiList=mNotiDao.getAllNoti();


    }

    // Room ejecuta todas las consultas en un hilo separado.
    // Observe LiveData notificará al observador cuando los datos hayan cambiado.
    public List<Usuario> getAllUser() {
        return mUserList;
    }

    public LiveData<List<Mascota>> getAllPet() {
        return mPetList;
    }

    public LiveData<List<Mascota>> getAllPetByUser(String emailUser) {
        return mPetDao.getAllPetByUser(emailUser);
    }

    public String getNamePet(Integer idPet){
        return mPetDao.getNamePet(idPet);
    }

    public List<Cita> getAllCita() {
        return mCitaList;
    }
    public List<Enfermedad> getAllEnfermedad(){ return mEnfermedadList;}

    public LiveData<List<Articulo>> getAllArticleByPet(Integer idPet) {
        return mArticuloDao.getAllArticuloByPet(idPet);
    }

    public LiveData<List<Cita>> gettAllCitaByPet(Integer idPet) {
        return mCitaDao.getAllCitaByPet(idPet);
    }

    public List<Articulo> getAllArticulo() {
        return mArticuloList;
    }

    public List<Notificacion>getAllNoti(){ return mNotiList;}

    public String timeNoti(String nombre,String fecha){
        return mNotiDao.timeByNoti(nombre,fecha);
    }


    // Debe llamar a esto en un subproceso que no
    // sea de interfaz de usuario o su aplicación generará una excepción.

    //Room asegura que no esta realizando ninguna operacion de ejecucion prolongada
    //en el subproceso principal, bloqueando la interfaz de usuario.


    public void insertUsuario(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
                    mUserDao.insertUser(usuario);
                }
        );
    }


    public Boolean loginUser(String emailUser, String passUser) {
        return mUserDao.loginUser(emailUser, passUser);
    }

    public Boolean loginUserName(String emailUser) {
        return mUserDao.loginUserName(emailUser);
    }

    public Boolean existArticle(String nameArticle){ return mArticuloDao.existArticle(nameArticle);}

    public Boolean isNoti(Integer idNoti){ return mNotiDao.isNoti(idNoti);}

    public Boolean isNotiByIdCita(Integer idCita){ return mNotiDao.isNotiByIdCita(idCita);}

    public Boolean isNotiCompare(String nombre, String fecha){ return mNotiDao.isNotiCompare(nombre, fecha);}

    public Boolean existCita(String clinica, Date fecha, Integer idPet){ return mCitaDao.existCita(clinica,fecha,idPet);}

    public Boolean isNotification(String clinica, String fecha, Time time, String motivo){
        return mNotiDao.isNotification(clinica,fecha,time,motivo);
    }

    public void updatePass(String email, String pass) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.updatePass(email, pass);
        });
    }


    public void insertPet(Mascota mascota) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
                    mPetDao.insertPet(mascota);
                }
        );
    }

    public void insertCita(Cita cita) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
                    mCitaDao.insertCita(cita);
                }
        );
    }

    public void insertArticulo(Articulo articulo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
                    mArticuloDao.insertArticulo(articulo);
                }
        );
    }

    public void insertNoti(Notificacion noti) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
                    mNotiDao.insertNoti(noti);
                }
        );
    }

    public void deletePet(Mascota pet) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mPetDao.delete(pet);
        });
    }

    public void deleteCita(Cita cita) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mCitaDao.delete(cita);
        });
    }

    public void deleteArticulo(Articulo articulo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mArticuloDao.deleteArticulo(articulo);
        });
    }

    public void deleteUser(Usuario user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.delete(user);
        });
    }

    public void deleteNoti(Integer idCita){
        AppDatabase.databaseWriteExecutor.execute(()->{
            mNotiDao.deleteNoti(idCita);
        });
    }
}
