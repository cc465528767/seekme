package com.zzzealous.seekme.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzzealous.seekme.Bean.Message;
import com.zzzealous.seekme.MyApplication;
import com.zzzealous.seekme.R;

import java.util.List;
import java.util.Objects;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Message> mMsgList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout, rightLayout;
        TextView leftMsg, rightMsg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftLayout =itemView.findViewById( R.id.left_layout );
            rightLayout = itemView.findViewById( R.id.right_layout );
            leftMsg = itemView.findViewById( R.id.left_msg );
            rightMsg = itemView.findViewById( R.id.right_msg );

        }
    }
    public MsgAdapter(List<Message> mMsgList) {//构造方法
        this.mMsgList = mMsgList;
    }

    @NonNull
    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.message_item,viewGroup, false );
        return new ViewHolder( view );

    }

    @Override
    public void onBindViewHolder(@NonNull MsgAdapter.ViewHolder viewHolder, int i) {
        Message msg = mMsgList.get(i);
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("loginToken", Activity.MODE_PRIVATE);
        if((msg.getSentid()).equals(sp.getString("userId",null))){
            viewHolder.leftLayout.setVisibility( View.GONE );
            viewHolder.rightLayout.setVisibility( View.VISIBLE );
            viewHolder.rightMsg.setText( msg.getContent() );

        }else{
            viewHolder.leftLayout.setVisibility( View.VISIBLE );
            viewHolder.rightLayout.setVisibility( View.GONE );
           viewHolder.leftMsg.setText( msg.getContent() );

        }


    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }


}
