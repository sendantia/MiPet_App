package com.example.mipet.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mipet.R;
import com.example.mipet.app.activities.MainActivity;
import com.example.mipet.app.activities.PrincipalArticulos;
import com.example.mipet.app.activities.PrincipalCitas;
import com.example.mipet.database.entities.Mascota;

import java.util.List;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetViewHolder> {

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(Mascota myPet);
    }

    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<Mascota> mPets;
    private OnDeleteClickListener onDeleteClickListener;


    public PetListAdapter(Context context, OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.onDeleteClickListener = listener;

    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.fila_recicler_pet, parent, false);
        PetViewHolder viewHolder = new PetViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        if (mPets != null) {
            Mascota pet = mPets.get(position);
            holder.setData(pet.getNombre(), pet.getTipo(), position);
            holder.setListeners();

        }
    }


    @Override
    public int getItemCount() {
        if (mPets != null) {
            return mPets.size();
        } else return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPets(List<Mascota> pets) {
        mPets = pets;
        notifyDataSetChanged();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        private TextView nombre, tipoPet;
        private int mPosition;
        private ImageView tipoIm, btnMenu, eliminar;


        public PetViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombreLista);
            tipoIm = (ImageView) itemView.findViewById(R.id.tipo);
            btnMenu = (ImageView) itemView.findViewById(R.id.btn_cuidados);
            eliminar = (ImageView) itemView.findViewById(R.id.eliminar_pet);
            tipoPet = (TextView) itemView.findViewById(R.id.tipoLista);


        }

        public void setData(String nombrePet, String tipo, int position) {
            nombre.setText(nombrePet);
            tipoPet.setText(tipo);
            findImage();
            mPosition = position;

        }


        public void setListeners() {
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(btnMenu.getContext(), itemView);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.cita:
                                    Intent intent = new Intent(mContext, PrincipalCitas.class);
                                    Integer idPet = mPets.get(mPosition).getIdPet();
                                    intent.putExtra("ENVIAR ID PET", idPet);
                                    ((Activity) mContext).startActivityForResult(intent, MainActivity.UPDATE_ACTIVITY_REQUEST_CODE);
                                    return true;

                                case R.id.articulo:
                                    Intent i = new Intent(mContext, PrincipalArticulos.class);
                                    Integer id = mPets.get(mPosition).getIdPet();
                                    i.putExtra("ENVIAR ID PET", id);
                                    ((Activity) mContext).startActivityForResult(i, MainActivity.UPDATE_ACTIVITY_REQUEST_CODE);

                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.inflate(R.menu.menu_cuidados);
                    popup.show();
                }
            });


            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnDeleteClickListener(mPets.get(mPosition));
                    }
                }
            });
        }

        public void findImage() {
            if (tipoPet.getText().equals("Perro")) {
                tipoIm.setImageResource(R.drawable.perro);
            } else if (tipoPet.getText().equals("Gato")) {
                tipoIm.setImageResource(R.drawable.gato);
            } else if (tipoPet.getText().equals("Pez")) {
                tipoIm.setImageResource(R.drawable.pez);
            } else if (tipoPet.getText().equals("Pájaro")) {
                tipoIm.setImageResource(R.drawable.pajaro);
            } else if (tipoPet.getText().equals("Réptil")) {
                tipoIm.setImageResource(R.drawable.serpiente);
            } else if (tipoPet.getText().equals("Otros mamíferos")) {
                tipoIm.setImageResource(R.drawable.hamster);
            }


        }


    }
}