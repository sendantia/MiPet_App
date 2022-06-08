package com.example.mipet.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mipet.R;
import com.example.mipet.database.entities.Articulo;

import java.util.List;

public class ArticuloListAdapter  extends RecyclerView.Adapter<ArticuloListAdapter.ArticuloViewHolder> {

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(Articulo mArticulos);
    }

    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<Articulo> mArticulos;
    private OnDeleteClickListener onDeleteClickListener;

    public ArticuloListAdapter(Context context, OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.fila_recicler_articulo, parent, false);
        ArticuloViewHolder viewHolder = new ArticuloViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloViewHolder holder, int position) {
        if (mArticulos != null) {
            Articulo articulo = mArticulos.get(position);
            holder.setData(articulo.getNombreArticulo(), articulo.getPvp(), articulo.getFechaVencimiento(), articulo.getRenovacion(), position);
            holder.setListeners();

        } else {
            //en caso de que no haya datos.
            holder.textnombreArt.setText(R.string.no_note);
        }

    }

    @Override
    public int getItemCount() {
        if (mArticulos != null) {
            return mArticulos.size();
        } else return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setArticulos(List<Articulo> articulos) {
        mArticulos = articulos;
        notifyDataSetChanged();
    }

    public class ArticuloViewHolder extends RecyclerView.ViewHolder {
        private TextView textnombreArt, textPvp, textfecha, textduracion;
        private int mPosition;
        private ImageView btnEliminar;

        public ArticuloViewHolder(View itemView){

            super(itemView);
            textnombreArt=itemView.findViewById(R.id.nombre_articulo);
            textPvp=itemView.findViewById(R.id.pvp);
            textfecha=itemView.findViewById(R.id.fecha_renovacion);
            textduracion=itemView.findViewById(R.id.duracion);
            btnEliminar=itemView.findViewById(R.id.eliminar_articulo);
        }

        public void setData(String nombre, Float pvp, String fecha, String duracion,int position) {
            textnombreArt.setText(nombre);
            textPvp.setText(pvp.toString()+"€");
            textfecha.setText(fecha.toString());
            textduracion.setText(duracion);
            mPosition = position;
        }

        public void setListeners() {
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnDeleteClickListener(mArticulos.get(mPosition));
                    }
                }
            });
        }
    }

}
