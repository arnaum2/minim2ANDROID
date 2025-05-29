package com.example.buzzblitz_android_cliente.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buzzblitz_android_cliente.Models.SolicitudAmistad;
import com.example.buzzblitz_android_cliente.R;
import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {
    private List<SolicitudAmistad> solicitudes;
    private OnFriendRequestActionListener listener;

    public interface OnFriendRequestActionListener {
        void onAccept(int position);
        void onReject(int position);
    }

    public FriendRequestAdapter(List<SolicitudAmistad> solicitudes, OnFriendRequestActionListener listener) {
        this.solicitudes = solicitudes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new FriendRequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        SolicitudAmistad solicitud = solicitudes.get(position);
        if (listener != null) {
            holder.tvRequestInfo.setText("De: " + solicitud.getDe());
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.btnAccept.setOnClickListener(v -> listener.onAccept(position));
            holder.btnReject.setOnClickListener(v -> listener.onReject(position));
        } else {
            holder.tvRequestInfo.setText("Para: " + solicitud.getPara() + " (Pendiente)");
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return solicitudes.size();
    }

    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequestInfo;
        Button btnAccept, btnReject;
        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestInfo = itemView.findViewById(R.id.tvRequestInfo);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}