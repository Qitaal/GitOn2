package com.example.consumerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    Context context;

    List<FavoriteUser> favoriteUsers;

    public UserListAdapter(Context context) {
        this.context = context;
        favoriteUsers = new ArrayList<>();
    }

    public void setFavoriteUsers(List<FavoriteUser> favoriteUsers) {
        this.favoriteUsers = favoriteUsers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new MyViewHolder(layoutInflater.inflate(R.layout.item_user_main, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
            return favoriteUsers.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CircleImageView civUserList;
        ConstraintLayout itemList;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_user_list);
            civUserList = itemView.findViewById(R.id.civ_user_list);
            itemList = itemView.findViewById(R.id.item_list);
        }

        public void bind(int position) {
            tvName.setText(favoriteUsers.get(position).getName());
            Glide.with(context)
                    .load(favoriteUsers.get(position).getAvatar_url())
                    .into(civUserList);
            itemList.setOnClickListener(v -> Toast.makeText(context, favoriteUsers.get(position).getName(), Toast.LENGTH_SHORT).show());
        }
    }
}
