package com.example.buzzblitz_android_cliente.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Models.Mensaje;
import com.example.buzzblitz_android_cliente.R;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Mensaje> mensajes;
    private OnMessageActionListener listener;

    public interface OnMessageActionListener {
        void onEdit(int position, String mensaje);
        void onDelete(int position);
    }

    public MessageAdapter(List<Mensaje> mensajes, OnMessageActionListener listener) {
        this.mensajes = mensajes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);
        holder.tvMessage.setText(mensaje.getAutor() + ": " + mensaje.getContenido());
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(position, mensaje.getContenido()));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ImageButton btnEdit, btnDelete;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}