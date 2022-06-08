package com.example.mipet.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mipet.R;
import com.example.mipet.app.activities.PrincipalCitas;
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
        void OnDeleteClickListener(Cita mCita);
    }

    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<Cita> mCitas;
    private OnDeleteClickListener onDeleteClickListener;

    public CitaListAdapter(Context context, CitaListAdapter.OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.fila_recicler_cita, parent, false);
        CitaViewHolder viewHolder = new CitaViewHolder(itemView, mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {

        if (mCitas != null) {
            Cita cita = mCitas.get(position);
            holder.setData(cita.getClinica(), cita.getFecha(), cita.getHora(), cita.getMotivo(), holder.swNotification, holder.textNotificacion, position);
            /*holder.swNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()==true){
                        compoundButton.isChecked();
                    }else{
                        compoundButton.isChecked();
                    }

                }
            });*/
            holder.setListeners();

        } else {
            //en caso de que no haya datos.
            holder.textLugar.setText(R.string.no_note);
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
        private TextView textLugar, textFecha, textHora, textMotivo, textNotificacion;
        private int mPosition;
        private ImageView btnEliminar;
        final Calendar c = Calendar.getInstance();
        private Context context;
        private SwitchCompat swNotification;
        private Boolean isChecked, isNotificacion;
        private int idCita;
        private String fechaS, horaS;
        private ViewModel appView;
        private Notificacion noti;

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
            //le damos formato a la fecha
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String fechaS = formatter.format(fecha);
            textFecha.setText(fechaS);
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
            String horaS = formatterTime.format(hora);
            textHora.setText(horaS);
            //textFecha.setText(formatter.format(fecha.toString()));
            //textHora.setText(hora.toString());
            textMotivo.setText(motivo);
            //comprobamos si la cita ya tiene notificacion activada
            swNotification.setChecked(appView.isNotification(clinica, fechaS,hora,motivo));
            if (appView.isNotification(clinica, fechaS,hora,motivo)) {
                isChecked = true;
                textNotificacion.setVisibility(View.VISIBLE);
                textNotificacion.setText("Hora: " + appView.timeNoti(clinica, fechaS));
            } else {
                isChecked = false;
            }
            mPosition = position;


        }

        public void setListeners() {
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnDeleteClickListener(mCitas.get(mPosition));
                    }
                }
            });

            swNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //si el checked es activado
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
        }

        //metodo para activar la notificacion
        public void checkSwitch(View view) {
            int a = 1;
            Date fecha = mCitas.get(mPosition).getFecha();
            java.util.Date utilDate = new java.util.Date(fecha.getTime());
            Time t = mCitas.get(mPosition).getHora();
            int hourCita = t.getHours();
            int minCita = t.getMinutes();
            utilDate.setHours(hourCita);
            utilDate.setMinutes(minCita);
            //comprobamos si la fecha ya ha pasado
            if (c.getTime().after(utilDate)) {
                Toast.makeText(mContext.getApplicationContext(), " La cita ya ha pasado", Toast.LENGTH_LONG).show();
                swNotification.setChecked(false);
                isChecked = false;
            } else {
                swNotification.setChecked(false);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(mContext, R.style.CustomDatePickerDialog, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {

                        //recogemos  la fecha de la cita seleccionada
                        //parseamos de Date.Sql a Date.Util
                        java.util.Date utilDate2 = new java.util.Date(fecha.getTime());

                        c.set(Calendar.HOUR_OF_DAY, hour);
                        c.set(Calendar.MINUTE, min);

                        //recogemos la hora que puso el usuario para que suene la alarma
                        //horaAlarma = new Time(hour, min, 00);
                        utilDate2.setHours(hour);
                        utilDate2.setMinutes(min);

                        //para recoger la hora y mostrarla en el textView
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


                        //guardamos la fecha en preferencias
                        SharedPreferences sharedPreferences =
                                context.getSharedPreferences("fechaNotificacion", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("fechaAlarma", utilDate2.getTime());
                        editor.commit();

                        Utils.setAlarm(a, utilDate2.getTime(), mContext);
                        Toast.makeText(mContext.getApplicationContext(), " Noticficacion activada", Toast.LENGTH_LONG).show();
                        isChecked = true;
                        textNotificacion.setText("Hora: " + horaS);
                        textNotificacion.setVisibility(View.VISIBLE);


                    }

                }, hour, minute, false);
                mTimePicker.setTitle(R.string.select_time);
                mTimePicker.show();
            }


        }


    }


}


