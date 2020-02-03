package com.example.mytime.cooker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.ItemComment;

import java.util.List;

/**
 * Created by My time on 2017/11/4
 */

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<ItemComment> comments;

    public CommentAdapter(Context context , List<ItemComment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return comments.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item,null);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.comment_item_username);
            holder.content = (TextView) convertView.findViewById(R.id.comment_item_content);
            holder.img_comment = (ImageView) convertView.findViewById(R.id.img_comment);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemComment comment = comments.get(position);
        if (comment.getItemCommentByCommentId() != null){

        }
        holder.username.setText(comment.getUser().getUsername());
        holder.content.setText(comment.getContent());
        return convertView;
    }

    class ViewHolder{
        private TextView username;
        private ImageView img_comment;
        private TextView content;
    }
}
