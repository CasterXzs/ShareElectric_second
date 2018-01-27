package com.xzs.shareelectric_second.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.entity.Message;

import java.util.List;

/**
 * Created by john on 2018/1/22.
 */

public class MessageAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Message> messages;
    private Context context;
    public MessageAdapter(Context context, List<Message> objects) {
        inflater= LayoutInflater.from(context);
        this.context=context;
        this.messages=objects;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_listview,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.Messageimage=(ImageView)convertView.findViewById(R.id.message_image);
            viewHolder.title=(TextView)convertView.findViewById(R.id.message_title);
            viewHolder.content=(TextView)convertView.findViewById(R.id.message_content);
            convertView.setTag(viewHolder);
        }else{

            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.Messageimage.setImageResource(messages.get(position).getImageId());
        viewHolder.title.setText(messages.get(position).getTitle());
        viewHolder.content.setText(messages.get(position).getContent());
        return convertView;
    }
    class ViewHolder{
        ImageView Messageimage;
        TextView title;
        TextView content;
    }
}
