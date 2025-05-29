package com.example.buzzblitz_android_cliente.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Models.UltimoMensajeDTO;
import com.example.buzzblitz_android_cliente.R;
import java.util.List;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.UserViewHolder> {
    private List<UltimoMensajeDTO> chats;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(String userId);
    }

    public UserChatAdapter(List<UltimoMensajeDTO> chats, OnUserClickListener listener) {
        this.chats = chats;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_chat, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UltimoMensajeDTO chat = chats.get(position);
        holder.tvUserName.setText(chat.getUsuario());
        holder.tvLastMessage.setText(chat.getUltimoMensaje() != null ? chat.getUltimoMensaje() : "");
        holder.btnOpenChat.setOnClickListener(v -> {
            if (listener != null) listener.onUserClick(chat.getUsuario());
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvLastMessage;
        Button btnOpenChat;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            btnOpenChat = itemView.findViewById(R.id.btnOpenChat);
        }
    }
}