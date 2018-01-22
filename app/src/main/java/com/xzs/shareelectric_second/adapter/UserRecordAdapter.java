package com.xzs.shareelectric_second.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.entity.UserRecordEntity;

import java.util.List;

/**
 * Created by Lenovo on 2018/1/19.
 */

public class UserRecordAdapter extends RecyclerView.Adapter<UserRecordAdapter.ViewHolder> {


    private Context mContext;
    private List<UserRecordEntity> mUserRecordEntityList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView userrecord_cardview;
        TextView userrecord_tv_usedtime;
        TextView userrecord_tv_usedmoney;
        TextView userrecord_tv_machinenumber;

        public ViewHolder(View view){
            super(view);
            userrecord_cardview=(CardView)view;
            userrecord_tv_usedtime=(TextView)view.findViewById(R.id.userrecord_tv_usedtime);
            userrecord_tv_usedmoney=(TextView)view.findViewById(R.id.userrecord_tv_usedmoney);
            userrecord_tv_machinenumber=(TextView)view.findViewById(R.id.userrecord_tv_machinenumber);
        }
    }

    public UserRecordAdapter(List<UserRecordEntity> userRecordEntityList){
        mUserRecordEntityList=userRecordEntityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_record,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserRecordEntity userRecordEntity=mUserRecordEntityList.get(position);
        holder.userrecord_tv_usedtime.setText(userRecordEntity.getUsedtime());
        holder.userrecord_tv_usedmoney.setText(userRecordEntity.getUsedmoney());
        holder.userrecord_tv_machinenumber.setText(userRecordEntity.getMachinenumber());
    }

    @Override
    public int getItemCount() {
        return mUserRecordEntityList.size();
    }
}
