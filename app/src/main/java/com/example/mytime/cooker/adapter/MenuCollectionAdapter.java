package com.example.mytime.cooker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.MenuCollection;

import java.util.List;

/**
 * Created by My time on 2017/10/14
 */

public class MenuCollectionAdapter extends BaseAdapter {
    private Context context;
    private List<MenuCollection> menuCollections;
    private MyMenuDeleteClickListener myMenuDeleteClickListener;

    public MenuCollectionAdapter(Context context , List<MenuCollection> menuCollections, MyMenuDeleteClickListener myMenuDeleteClickListener){
        this.context = context;
        this.menuCollections = menuCollections;
        this.myMenuDeleteClickListener = myMenuDeleteClickListener;
    }

    @Override
    public int getCount() {
        return menuCollections.size();
    }

    @Override
    public Object getItem(int position) {
        return menuCollections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menuCollections.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_collection_item,null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.menuCollection_name);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.menuCollection_ratBar);
            holder.image = (ImageView) convertView.findViewById(R.id.menuCollection_img);
            holder.delete = (ImageView) convertView.findViewById(R.id.menuCollection_delete);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        MenuCollection menuCollection = menuCollections.get(position);
        holder.name.setText(menuCollection.getMenu().getMenuName());
        holder.ratingBar.setRating(Float.parseFloat(menuCollection.getMenu().getScore()));
        Glide.with(context)
                .load(menuCollection.getMenu().getImgUrl())
                .into(holder.image);

        holder.delete.setOnClickListener(myMenuDeleteClickListener);
        holder.delete.setTag(position);

        return convertView;
    }
    public static abstract class MyMenuDeleteClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }


    class ViewHolder{
        private ImageView image;
        private TextView name;
        private RatingBar ratingBar;
        private ImageView delete;
    }
}
