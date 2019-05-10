package com.zzzealous.seekme.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzzealous.seekme.Bean.User;
import com.zzzealous.seekme.R;

import java.util.List;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
    private List<User> musers;
    List<Boolean> listCheck;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView usersname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usersname=(TextView) itemView.findViewById(R.id.tv_title);
        }
    }
    public SelectAdapter(List<User> users){
        musers=users;
    }

    @NonNull
    @Override
    public SelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_checkbox,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAdapter.ViewHolder viewHolder, int i) {
        User user=musers.get(i);
        viewHolder.usersname.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return musers.size();
    }

    public void refresh(List<User> users){
        this.musers = users;
        notifyDataSetChanged();
    }
}
