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

public class MyWeaponsAdapter extends RecyclerView.Adapter<MyWeaponsAdapter.ViewHolder> {
    private final List<Objeto> weapons;
    private final Context context;

    public MyWeaponsAdapter(List<Objeto> weapons, Context context) {
        this.weapons = weapons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_objetoweapons, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(weapons.isEmpty()) {
            holder.tvNombre.setText(context.getString(R.string.sin_items));
            holder.imgObjeto.setVisibility(View.GONE);
            return;
        }

        Objeto objeto = weapons.get(position);
        holder.tvNombre.setText(objeto.getNombre());
        holder.tvPrecio.setText("Precio: " + objeto.getPrecio());
        holder.tvTipo.setText("Tipo: Arma");
        holder.tvDescripcion.setText(objeto.getDescripcion());

        String imageUrl = "http://10.0.2.2:8080/img/" + objeto.getImagen();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.img_25)
                .error(R.drawable.img_25)
                .into(holder.imgObjeto);
    }

    @Override
    public int getItemCount() {
        return weapons.size();
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
