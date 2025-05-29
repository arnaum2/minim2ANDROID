package com.example.buzzblitz_android_cliente.Adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buzzblitz_android_cliente.Models.Info;
import com.example.buzzblitz_android_cliente.R;

import java.util.List;

public class MyRankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TOP = 0;
    private static final int TYPE_USER = 1;

    private final List<Info> rankingList;
    private final String currentUserId;
    private final int userPosition;

    public MyRankingAdapter(List<Info> rankingList, String currentUserId, int userPosition) {
        this.rankingList = rankingList;
        this.currentUserId = currentUserId;
        this.userPosition = userPosition;
    }

    @Override
    public int getItemViewType(int position) {
        if (userPosition > 5 && position == rankingList.size() - 1) {
            return TYPE_USER;
        }
        return TYPE_TOP;
    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ranking_top, parent, false);
            return new TopViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ranking_user, parent, false);
            return new UserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Info item = rankingList.get(position);

        if (holder instanceof TopViewHolder) {
            TopViewHolder topHolder = (TopViewHolder) holder;

            if (item.getUsuario().equals(currentUserId)) {
                topHolder.itemView.setBackgroundResource(R.drawable.bg_highlighted_user);
                topHolder.tvNombreUsuario.setTypeface(null, Typeface.BOLD);
            } else {
                topHolder.itemView.setBackgroundResource(R.drawable.bg_ranking_item);
                topHolder.tvNombreUsuario.setTypeface(null, Typeface.NORMAL);
            }

            // Colores según posición
            int colorRes = R.color.white;
            if (position == 0) colorRes = R.color.gold;
            else if (position == 1) colorRes = R.color.silver;
            else if (position == 2) colorRes = R.color.bronze;
            topHolder.itemView.setBackgroundResource(colorRes);

            topHolder.tvPosicion.setText(String.valueOf(position + 1));
            topHolder.tvNombreUsuario.setText(item.getUsuario());
            topHolder.tvPuntuacion.setText(String.valueOf(item.getMejorPuntuacion()));

        } else if (holder instanceof UserViewHolder) {
            UserViewHolder userHolder = (UserViewHolder) holder;
            userHolder.tvPosicionUser.setText(String.valueOf(userPosition));
            userHolder.tvNombreUsuarioUser.setText("Tú");
            userHolder.tvPuntuacionUser.setText(String.valueOf(item.getMejorPuntuacion()));
        }
    }

    static class TopViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosicion, tvNombreUsuario, tvPuntuacion;

        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosicion = itemView.findViewById(R.id.tvPosicion);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvPuntuacion = itemView.findViewById(R.id.tvPuntuacion);
        }
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosicionUser, tvNombreUsuarioUser, tvPuntuacionUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosicionUser = itemView.findViewById(R.id.tvPosicionUser);
            tvNombreUsuarioUser = itemView.findViewById(R.id.tvNombreUsuarioUser);
            tvPuntuacionUser = itemView.findViewById(R.id.tvPuntuacionUser);
        }
    }
}