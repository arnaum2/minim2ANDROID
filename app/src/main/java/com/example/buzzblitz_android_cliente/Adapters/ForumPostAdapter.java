package com.example.buzzblitz_android_cliente.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Models.Comentario;
import com.example.buzzblitz_android_cliente.R;
import java.util.List;

public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.ForumPostViewHolder> {
    private List<Comentario> comentarios;
    private OnForumPostActionListener listener;

    public interface OnForumPostActionListener {
        void onEdit(int position, String mensaje);
        void onDelete(int position);
    }

    public ForumPostAdapter(List<Comentario> comentarios, OnForumPostActionListener listener) {
        this.comentarios = comentarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ForumPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_post, parent, false);
        return new ForumPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumPostViewHolder holder, int position) {
        Comentario comentario = comentarios.get(position);
        holder.tvForumPost.setText(comentario.getAutor() + ": " + comentario.getContenido());
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(position, comentario.getContenido()));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public static class ForumPostViewHolder extends RecyclerView.ViewHolder {
        TextView tvForumPost;
        ImageButton btnEdit, btnDelete;
        public ForumPostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvForumPost = itemView.findViewById(R.id.tvForumPost);
            btnEdit = itemView.findViewById(R.id.btnEditForum);
            btnDelete = itemView.findViewById(R.id.btnDeleteForum);
        }
    }
}