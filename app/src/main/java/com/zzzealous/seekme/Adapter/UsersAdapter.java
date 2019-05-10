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

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<User> musers;

    private OnItemClickListener mOnItemClickListener=null;



    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView usersname;
        TextView catalog;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usersname=(TextView) itemView.findViewById(R.id.users_name);

        }
    }
    public UsersAdapter(List<User> users){
        musers=users;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_itms,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
      //view.setOnClickListener(this);
        viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersAdapter.ViewHolder viewHolder, int i) {
        viewHolder.getLayoutPosition();
        User user=musers.get(i);


        //viewHolder.usersname.setText(user.getName());
        String catalog = musers.get(i).getFirstLetter();

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(i == getPositionForSection(catalog)){
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
        }else {
            viewHolder.catalog.setVisibility(View.GONE);
        }

        viewHolder.usersname.setText(musers.get(i).getName());

        if (mOnItemClickListener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 这里利用回调来给RecyclerView设置点击事件
                    mOnItemClickListener.onItemClick(v,viewHolder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return musers.size();
    }

    public void refresh(List<User> users){
        this.musers = users;
        notifyDataSetChanged();
    }
    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = musers.get(i).getFirstLetter();
            if (catalog != null && catalog.equalsIgnoreCase(sortStr) ) {
                return i;
            }
        }
        return -1;
    }
   /* @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }

    }*/

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
}
