package com.example.mipet.app.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mipet.R;
import com.example.mipet.database.entities.Cita;
import com.example.mipet.database.viewmodel.ViewModel;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class RegistroCitas extends AppCompatActivity implements View.OnClickListener {
    private DatePickerDialog datePickerDialog;
    private EditText editNombre, editMotivo;
    private Button btnRegistrarCita;
    private ImageButton btnDate, btnTime, btnClear;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Bundle bundle;
    private Integer id;
    private String nombreClinica, motivo, fechaString, horaString;
    private Date fecha;
    private Time hora;
    private Cita cita;
    private ViewModel appView;
    final Calendar c = Calendar.getInstance();
    private TextView txtFecha, txtHora;
    private java.util.Date utilDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_citas);
        editNombre = findViewById(R.id.nombre_clinica);
        txtFecha = findViewById(R.id.fecha_cita);
        txtHora = findViewById(R.id.hora_cita);
        editMotivo = findViewById(R.id.motivo_cita);
        btnRegistrarCita = findViewById(R.id.btn_reg_cita);
        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        btnClear = findViewById(R.id.btn_clear_cita);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);

        //recogemos el id
        bundle = getIntent().getExtras();
        id = bundle.getInt("ENVIAR ID PET");

        appView = new ViewModel(this.getApplication());

        btnRegistrarCita.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (validateCita()) {
                    appView.insertCita(cita);
                    Toast.makeText(getApplicationContext(), getString(R.string.cita_registrada), Toast.LENGTH_LONG).show();
                    starActivity();

                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                editNombre.setText("");
                txtFecha.setText("");
                txtHora.setText("");
                editMotivo.setText("");
            }
        });

    }


    @Override
    public void onClick(View v) {

        if (v == btnDate) {

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(this, R.style.DatePickerDialogTheme,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            //creamos la fecha con las variables del datePicker (clase util)
                            fecha = new Date((year - 1900), monthOfYear, dayOfMonth);

                            fechaString = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            txtFecha.setText(fechaString);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTime) {

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CustomDatePickerDialog,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            //creamos la hora con las variables del TimePicker (clase util)
                            hora = new Time(hourOfDay, minute, 00);
                            horaString = hourOfDay + ":" + minute;
                            txtHora.setText(horaString);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    //Creamos el menú para poder volver a la ventana principal.
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_volver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.volver:
                starActivity();
            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Boolean validateCita() {
        nombreClinica = editNombre.getText().toString();
        //comprobamos si el usuario introdujo la fecha y hora
        //comprobamos que la fecha y hora no son anteriores al día actual
        try {
            //recogemos la fecha p
            utilDate = new java.util.Date(fecha.getTime());
            //le añadimos a la  fecha la hora
            int hourCita = hora.getHours();
            int minCita = hora.getMinutes();
            utilDate.setHours(hourCita);
            utilDate.setMinutes(minCita);

        } catch (NullPointerException e) {
            Toast.makeText(this.getApplicationContext(), getString(R.string.fecha_hora_sin_datos), Toast.LENGTH_LONG).show();
            return false;
        }
        if (c.getTime().after(utilDate)) {
            Toast.makeText(this.getApplicationContext(), getString(R.string.fecha_pasada), Toast.LENGTH_LONG).show();
            return false;
        }

        //obtenemos la fecha (sql) haciendo la conversión de la fecha (util del datePicker) en milisegundos
        java.sql.Date fechaSql = new java.sql.Date(fecha.getTime());
        //recogemos la hora:
        java.sql.Time horaSql = new java.sql.Time(hora.getTime());

        motivo = editMotivo.getText().toString();
        bundle = getIntent().getExtras();
        id = bundle.getInt("ENVIAR ID PET");
        //comprobamos si la cita ya esta registrada
        Boolean isCita = appView.existCita(nombreClinica, fechaSql, id);
        if (!isCita) {
            cita = new Cita(fechaSql, horaSql, nombreClinica, motivo, id);
            if (nombreClinica.isEmpty() || motivo.isEmpty()) {

                Toast.makeText(getApplicationContext(), getString(R.string.falta_campos), Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.cita_existe), Toast.LENGTH_LONG).show();
            return false;
        }


    }

    public void starActivity() {
        Intent intent = new Intent(this, PrincipalCitas.class);
        intent.putExtra("ENVIAR ID PET", id);
        startActivity(intent);
        finish();
    }

}