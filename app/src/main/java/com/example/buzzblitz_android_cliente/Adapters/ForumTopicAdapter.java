package com.example.buzzblitz_android_cliente.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Models.UltimoComentarioDTO;
import com.example.buzzblitz_android_cliente.R;
import java.util.List;

public class ForumTopicAdapter extends RecyclerView.Adapter<ForumTopicAdapter.ForumTopicViewHolder> {
    private List<UltimoComentarioDTO> temas;
    private OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onTopicClick(String tema);
    }

    public ForumTopicAdapter(List<UltimoComentarioDTO> temas, OnTopicClickListener listener) {
        this.temas = temas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ForumTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_topic, parent, false);
        return new ForumTopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumTopicViewHolder holder, int position) {
        UltimoComentarioDTO tema = temas.get(position);
        holder.tvTopicTitle.setText(tema.getTema());
        holder.tvTopicDescription.setText(tema.getUltimoComentario() != null ? tema.getUltimoComentario() : "Sin comentarios aún");
        holder.tvNumComments.setText(tema.getNumComentarios() > 0 ? tema.getNumComentarios() + " comentarios" : "0 comentarios");
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onTopicClick(tema.getTema());
        });
    }

    @Override
    public int getItemCount() {
        return temas.size();
    }

    public static class ForumTopicViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopicTitle, tvTopicDescription, tvNumComments;
        public ForumTopicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicTitle = itemView.findViewById(R.id.tvTopicTitle);
            tvTopicDescription = itemView.findViewById(R.id.tvTopicDescription);
            tvNumComments = itemView.findViewById(R.id.tvNumComments);
        }
    }
}