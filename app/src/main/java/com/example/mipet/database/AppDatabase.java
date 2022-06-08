package com.example.mipet.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Usuario.class, Mascota.class, Cita.class, Articulo.class, Enfermedad.class, Notificacion.class}
        , version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class  AppDatabase  extends RoomDatabase {

    //Exposición de DAOs
    //creamos un metodo get por cada Dao
    public abstract UsuarioDao getUsuarioDao();
    public abstract MascotaDao getMascotaDao();
    public abstract CitaDao getCitaDao();
    public abstract ArticuloDao getArticuloDao();
    public abstract EnfermedadDao getEnfermedadDao();
    public abstract NotificacionDao getNotificacionDao();


    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //Singelton para evitar tener mas de una instacia abierta al mismo tiempo
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "Gestor_mascotas")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
