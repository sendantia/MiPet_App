package com.example.mipet.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mipet.R;
import com.example.mipet.app.preferences.PrefManager;
import com.example.mipet.app.utils.Utils;
import com.example.mipet.database.entities.Cita;
import com.example.mipet.database.entities.Notificacion;
import com.example.mipet.database.viewmodel.ViewModel;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CitaListAdapter extends RecyclerView.Adapter<CitaListAdapter.CitaViewHolder> {

    public interface OnDeleteClickListener {
        void OnDeleteClickListenerCita(Cita mCita);
    }

    private final LayoutInflater layoutInflater;
    private final Context mContext;
    private List<Cita> mCitas;
    private final OnDeleteClickListener onDeleteClickListener;

    public CitaListAdapter(Context context, CitaListAdapter.OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.fila_recicler_cita, parent, false);
        CitaViewHolder viewHolderCita = new CitaViewHolder(itemView, mContext);
        return viewHolderCita;
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {

        if (mCitas != null) {
            Cita cita = mCitas.get(position);
            holder.setData(cita.getClinica(), cita.getFecha(), cita.getHora(), cita.getMotivo(), holder.swNotification, holder.textNotificacion, position);
            holder.setListeners();

        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mCitas != null) {
            return mCitas.size();
        } else return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setClinicas(List<Cita> citas) {
        mCitas = citas;
        notifyDataSetChanged();
    }

    public class CitaViewHolder extends RecyclerView.ViewHolder {
        private final TextView textLugar;
        private final TextView textFecha;
        private final TextView textHora;
        private TextView textMotivo;
        private TextView textNotificacion;
        private int mPosition;
        private ImageView btnEliminar;
        private Context context;
        private SwitchCompat swNotification;
        private Boolean isChecked;
        private int idCita;
        private String fechaS, horaS;
        private ViewModel appView;
        private Notificacion noti;
        private int idAlarma = 1;

        public CitaViewHolder(View itemView, Context context) {
            super(itemView);
            textLugar = itemView.findViewById(R.id.label_lugar);
            textFecha = itemView.findViewById(R.id.label_fecha);
            textHora = itemView.findViewById(R.id.label_hora);
            textMotivo = itemView.findViewById(R.id.label_motivo);
            btnEliminar = itemView.findViewById(R.id.eliminar_cita);
            swNotification = itemView.findViewById(R.id.sw_notification);
            textNotificacion = itemView.findViewById(R.id.txt_notificacion);
            appView = new ViewModel(((Activity) mContext).getApplication());
            isChecked = false;
            this.context = context;


        }


        public void setData(String clinica, Date fecha, Time hora, String motivo, SwitchCompat swNotification, TextView textNotificacion, int position) {

            textLugar.setText(clinica);

            //le damos formato a la fecha y hora
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String fechaS = formatter.format(fecha);
            textFecha.setText(fechaS);
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
            String horaS = formatterTime.format(hora);
            textHora.setText(horaS);
            textMotivo.setText(motivo);

            //comprobamos si la cita ya tiene notificacion activada
            swNotification.setChecked(appView.isNotification(clinica, fechaS, hora, motivo));
            if (appView.isNotification(clinica, fechaS, hora, motivo)) {
                isChecked = true;
                textNotificacion.setVisibility(View.VISIBLE);
                textNotificacion.setText("Hora: " + appView.timeNoti(clinica, fechaS));
            } else {
                isChecked = false;
            }
            mPosition = position;


        }

        @SuppressLint("ClickableViewAccessibility")
        public void setListeners() {
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnDeleteClickListenerCita(mCitas.get(mPosition));
                    }
                }
            });

            swNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //si el checked ES activado y no está activado
                    if (swNotification.isChecked() && !isChecked) {
                        checkSwitch(view);

                    } else {
                        swNotification.setChecked(false);
                        isChecked = false;
                        appView.deleteNoti(mCitas.get(mPosition).getIdCita());
                        textNotificacion.setVisibility(View.INVISIBLE);
                    }


                }


            });

            swNotification.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (swNotification.isChecked() && !isChecked) {
                        checkSwitch(v);
                        return true;
                    } else {
                        swNotification.setChecked(false);
                        isChecked = false;
                        appView.deleteNoti(mCitas.get(mPosition).getIdCita());
                        textNotificacion.setVisibility(View.INVISIBLE);
                        return false;
                    }
                }
            });
            //si el usuario desliza, evitar que la clase SwitchCompat reciba MotionEvent.ACTION_MOVE eventos
            swNotification.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return event.getActionMasked() == MotionEvent.ACTION_MOVE;
                }
            });
        }

        //metodo para activar la notificacion
        public void checkSwitch(View view) {
            //obtengo la fecha en sql
            Date fecha = mCitas.get(mPosition).getFecha();
            Calendar c = Calendar.getInstance();

            //la pasamos a util en milisegundos
            java.util.Date utilDate = new java.util.Date(fecha.getTime());
            //comprobamos si la fecha + hora + min de la cita, ya ha pasado para no poder activar la notificación
            //obtenemos la hora sql para tener la hora y minuto
            Time t = mCitas.get(mPosition).getHora();
            int hourCita = t.getHours();
            int minCita = t.getMinutes();
            //añadimos la hora y el min a la fecha inicial
            utilDate.setHours(hourCita);
            utilDate.setMinutes(minCita);
            //comprobamos si la fecha ya ha pasado
            if (c.getTime().after(utilDate)) {
                Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.cita_pasada), Toast.LENGTH_LONG).show();
                swNotification.setChecked(false);
                isChecked = false;
            } else {
                swNotification.setChecked(false);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(mContext, R.style.CustomDatePickerDialog, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        Calendar c2 = Calendar.getInstance();

                        //recogemos  la fecha de la cita seleccionada
                        //parseamos de Date.Sql a Date.Util
                        java.util.Date utilDate2 = new java.util.Date(fecha.getTime());

                        //recogemos la hora que puso el usuario para que suene la alarma
                        //y se la añdimos  a la fecha
                        c2.set(Calendar.HOUR_OF_DAY, hour);
                        c2.set(Calendar.MINUTE, min);
                        utilDate2.setHours(hour);
                        utilDate2.setMinutes(min);

                        //comprobamos si la hora seleccionada es menor a la hora actual
                        //o si la hora seleccionada es mayor a la de la cita
                        if (c.getTime().before(utilDate2) == false || utilDate2.after(utilDate) == true) {
                            Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.no_corresponde), Toast.LENGTH_LONG).show();
                        } else {

                            Time t = new Time(utilDate2.getHours(), utilDate2.getMinutes(), 00);
                            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
                            horaS = formatterTime.format(t);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            fechaS = formatter.format(utilDate2);

                            //recogemos el id cita para hacer un insert de notificacion:
                            idCita = mCitas.get(mPosition).getIdCita();
                            noti = new Notificacion(isChecked, horaS, fechaS, idCita);
                            appView.insertNoti(noti);
                            swNotification.setChecked(true);


                            //guardamos la fecha en preferencias y el id de la alarma

                            SharedPreferences sharedPreferences =
                                    context.getSharedPreferences("fechaNotificacion", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong("fechaAlarma", utilDate2.getTime());
                            editor.commit();

                            //accedemos al id guardado, y lanzamos la alarma con ese id
                            sharedPreferences = context.getSharedPreferences("fechaNotificacion", Context.MODE_PRIVATE);
                            int nuevoId = sharedPreferences.getInt("numAlarma", idAlarma);
                            editor.putInt("numAlarma", nuevoId);
                            editor.commit();

                            //creamos la notificación
                            Utils.setAlarm(nuevoId, utilDate2.getTime(), mContext);
                            Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.notificacon_si), Toast.LENGTH_LONG).show();

                            //accedemos al id guardado, sumamos uno para que sea diferente al anterior
                            //guardamos en shared con ese id modificado
                            sharedPreferences = context.getSharedPreferences("fechaNotificacion", Context.MODE_PRIVATE);
                            nuevoId = sharedPreferences.getInt("numAlarma", idAlarma) + 1;
                            editor.putInt("numAlarma", nuevoId);
                            editor.commit();

                            isChecked = true;
                            textNotificacion.setText("Hora: " + horaS);
                            textNotificacion.setVisibility(View.VISIBLE);


                        }
                    }

                }, hour, minute, false);
                mTimePicker.setTitle(R.string.select_time);
                mTimePicker.show();
            }


        }


    }


}


