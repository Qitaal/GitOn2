package com.example.giton2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.giton2.R;
import com.example.giton2.database.FavoriteUser;
import com.example.giton2.model.search.ItemsItem;
import com.example.giton2.view.detailuser.DetailUserActivity;
import com.example.giton2.view.favorite.FavoriteFragment;
import com.example.giton2.view.favorite.FavoriteFragmentDirections;
import com.example.giton2.view.main.MainActivity;
import com.example.giton2.view.main.MainActivity$SearchFragmentDirections;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    private final Context context;
    private List<ItemsItem> listUser;
    private final String className;
    private List<FavoriteUser> favoriteUserList;

    public UserListAdapter(Context context, String className) {
        this.context = context;
        this.className = className;
        listUser = new ArrayList<>();
    }

    public void setListUser(List<ItemsItem> listUser) {
        this.listUser = listUser;
    }

    public void setFavoriteUserList(List<FavoriteUser> favoriteUserList) {
        this.favoriteUserList = favoriteUserList;
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
        if (className.equals(FavoriteFragment.class.getSimpleName())){
            if (favoriteUserList != null){
                return favoriteUserList.size();
            }
        }else {
            if (listUser != null)
                return listUser.size();
        }
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
            if (className.equals(FavoriteFragment.class.getSimpleName())){
                tvName.setText(favoriteUserList.get(position).getName());
                Glide.with(context)
                        .load(favoriteUserList.get(position).getAvatar_url())
                        .into(civUserList);
                itemList.setOnClickListener(v -> {
                    FavoriteFragmentDirections.ActionFavoriteFragmentToDetailUserActivity actionFavoriteFragmentToDetailUserActivity = FavoriteFragmentDirections.actionFavoriteFragmentToDetailUserActivity();
                    actionFavoriteFragmentToDetailUserActivity.setName(favoriteUserList.get(position).getName());
                    Navigation.findNavController(v).navigate(actionFavoriteFragmentToDetailUserActivity);
                });
            } else {
                tvName.setText(listUser.get(position).getLogin());
                Glide.with(context)
                        .load(listUser.get(position).getAvatarUrl())
                        .into(civUserList);
                itemList.setOnClickListener(v -> {
                    if (className.equals(MainActivity.SearchFragment.class.getSimpleName())){
                        MainActivity$SearchFragmentDirections.ActionSearchFragmentToDetailUserActivity actionSearchFragmentToDetailUserActivity = MainActivity$SearchFragmentDirections.actionSearchFragmentToDetailUserActivity();
                        actionSearchFragmentToDetailUserActivity.setName(listUser.get(position).getLogin());
                        Navigation.findNavController(v).navigate(actionSearchFragmentToDetailUserActivity);
                    }
                    else if (className.equals(DetailUserActivity.FollowingFollowersFragment.class.getSimpleName())){
                        Toast.makeText(context, "User : " + listUser.get(position).getLogin(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
