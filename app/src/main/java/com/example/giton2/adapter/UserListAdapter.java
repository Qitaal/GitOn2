package com.example.giton2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.giton2.R;
import com.example.giton2.model.search.ItemsItem;
import com.example.giton2.view.detailuser.DetailUserActivity;
import com.example.giton2.view.main.MainActivity$SearchFragmentDirections;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    private final Context context;
    private List<ItemsItem> listUser;

    public UserListAdapter(Context context) {
        this.context = context;
        listUser = new ArrayList<>();
    }


    public void setListUser(List<ItemsItem> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new MyViewHolder(layoutInflater.inflate(R.layout.item_user_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (listUser != null)
            return listUser.size();
        return 0;
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
            tvName.setText(listUser.get(position).getLogin());
            Glide.with(context)
                    .load(listUser.get(position).getAvatarUrl())
                    .into(civUserList);
            itemList.setOnClickListener(v -> {
                if (context.getClass().getSimpleName().equals("MainActivity")){
                    MainActivity$SearchFragmentDirections.ActionSearchFragmentToDetailUserActivity actionSearchFragmentToDetailUserActivity = MainActivity$SearchFragmentDirections.actionSearchFragmentToDetailUserActivity();
                    actionSearchFragmentToDetailUserActivity.setName(listUser.get(position).getLogin());
                    Navigation.findNavController(v).navigate(actionSearchFragmentToDetailUserActivity);
                } else {
                    context.startActivity(new Intent(context, DetailUserActivity.class).putExtra("USER", listUser.get(getAdapterPosition()).getLogin()));
                }
            });
        }
    }
}
