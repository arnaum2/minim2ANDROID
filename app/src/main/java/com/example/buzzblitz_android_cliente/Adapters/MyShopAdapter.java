package com.example.buzzblitz_android_cliente.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buzzblitz_android_cliente.Models.Objeto;
import com.example.buzzblitz_android_cliente.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyShopAdapter extends RecyclerView.Adapter<MyShopAdapter.ViewHolder> {

    private final List<Objeto> objetos;
    private static final String TAG = "ShopAdapter";
    private OnItemClickListener itemClickListener;
    private SharedPreferences sharedPreferences;

    public interface OnItemClickListener {
        void onBuyClick(int position, View view);
    }

    public MyShopAdapter(List<Objeto> objetos, Context context, SharedPreferences sharedPreferences) {
        this.objetos = objetos;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_objeto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Objeto objeto = objetos.get(position);
        Context context = holder.itemView.getContext();

        Set<String> purchasedItems = sharedPreferences.getStringSet("purchasedItems", new HashSet<>());
        boolean isPurchased = purchasedItems.contains(objeto.getId());

        holder.btnComprar.setVisibility(isPurchased ? View.GONE : View.VISIBLE);
        holder.tvEstadoCompra.setVisibility(isPurchased ? View.VISIBLE : View.GONE);

        holder.tvNombre.setText(objeto.getNombre());
        holder.tvPrecio.setText("Precio: " + objeto.getPrecio());
        holder.tvTipo.setText("Tipo: " + (objeto.getTipo() == 1 ? "Arma" : "Skin"));
        holder.tvDescripcion.setText("Descripción: " + objeto.getDescripcion());

        String imageUrl = "http://10.0.2.2:8080/img/" + objeto.getImagen();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.img_25)
                .error(R.drawable.img_25)
                .into(holder.imgObjeto);

        holder.btnComprar.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onBuyClick(holder.getAdapterPosition(), holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objetos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnComprar;
        public TextView tvEstadoCompra;
        public ImageView imgObjeto;
        public TextView tvNombre, tvPrecio, tvTipo, tvDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgObjeto = itemView.findViewById(R.id.imgObjeto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            btnComprar = itemView.findViewById(R.id.btnComprar);
            tvEstadoCompra = itemView.findViewById(R.id.tvEstadoCompra);
        }
    }

    public void actualizarTarrosMiel(int nuevosTarros) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentTarrosMiel", nuevosTarros);
        editor.apply();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
