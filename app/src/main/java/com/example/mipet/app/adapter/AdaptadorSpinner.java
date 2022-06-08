package com.example.mipet.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mipet.R;

import java.util.List;

public class AdaptadorSpinner extends BaseAdapter {
    Context contexto;
    private List<TipoPet> lista;

    public AdaptadorSpinner(Context contexto, List<TipoPet> lista) {
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    //devuelve el número de elementos que se van a visualizar en la lista
    public int getCount() {
        return lista.size();
    }

    @Override
    //Obtiene el elemento de datos asociado con la posición especificada en la lista
    public Object getItem(int i) {
        return i;
    }

    @Override
    //obtiene el ID de fila asociado con la posicion especificada de la lista
    public long getItemId(int i) {
        return i;
    }

    @Override
    //contenedor de las vistas que conforman la vista-fila
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vista = view;
        LayoutInflater inflate = LayoutInflater.from(contexto);
        vista = inflate.inflate(R.layout.spinner_tipo_pet, null);
        ImageView imagen = (ImageView) vista.findViewById((R.id.spIcono));
        TextView tipo = (TextView) vista.findViewById(R.id.spTipoPet);
        imagen.setImageResource(lista.get(position).getImagenTipo());
        tipo.setText(lista.get(position).getTipo());
        return vista;
    }
}

