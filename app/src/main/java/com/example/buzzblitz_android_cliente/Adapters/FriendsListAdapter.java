package com.example.buzzblitz_android_cliente.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.R;
import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder> {
    private List<String> amigos;
    private OnFriendActionListener listener;

    public interface OnFriendActionListener {
        void onDelete(int position);
    }

    public FriendsListAdapter(List<String> amigos, OnFriendActionListener listener) {
        this.amigos = amigos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        holder.tvFriendName.setText(amigos.get(position));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return amigos.size();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName;
        Button btnDelete;
        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            btnDelete = itemView.findViewById(R.id.btnDeleteFriend);
        }
    }
}