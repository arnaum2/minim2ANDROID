// MySkinAdapter.java
package com.example.buzzblitz_android_cliente.Adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.buzzblitz_android_cliente.Models.Objeto;
import com.example.buzzblitz_android_cliente.R;

import java.util.List;

public class MySkinAdapter extends RecyclerView.Adapter<MySkinAdapter.ViewHolder> {
    private final List<Objeto> skins;
    private static final String TAG = "SkinsAdapter";
    private Context context;

    public MySkinAdapter(List<Objeto> objetos, Context context) {
        this.skins = objetos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_objetoskins, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(skins.isEmpty()) {
            holder.tvNombre.setText(context.getString(R.string.sin_items));
            holder.imgObjeto.setVisibility(View.GONE);
            return;
        }
        Objeto objeto = skins.get(position);
        Context context = holder.itemView.getContext();

        holder.tvNombre.setText(objeto.getNombre());
        holder.tvPrecio.setText("Precio: " + objeto.getPrecio());
        holder.tvTipo.setText("Tipo: " + (objeto.getTipo() == 1 ? "Arma" : "Skin"));
        holder.tvDescripcion.setText("Descripción: " + objeto.getDescripcion());

        Log.d(TAG, "Posición: " + position);
        Log.d(TAG, "Nombre objeto: " + objeto.getNombre());
        Log.d(TAG, "Nombre imagen: " + objeto.getImagen());

        String imageUrl = "http://10.0.2.2:8080/img/" + objeto.getImagen();
        Log.d(TAG, "URL completa: " + imageUrl);

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.img_25)
                .error(R.drawable.img_25)
                .into(holder.imgObjeto);
    }

    @Override
    public int getItemCount() {
        return skins.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgObjeto;
        TextView tvNombre, tvPrecio, tvTipo, tvDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgObjeto = itemView.findViewById(R.id.imgObjeto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}
