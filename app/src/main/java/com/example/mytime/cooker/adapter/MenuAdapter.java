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
import com.example.mytime.cooker.bean.Menu;
import com.example.mytime.cooker.utils.App;

import java.util.List;

/**
 * Created by My time on 2017/12/28
 */

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<Menu> menus;

    public MenuAdapter(Context context, List<Menu> menus) {
        this.context = context;
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menus.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_item, null);
            holder = new ViewHolder();
            holder.menuImg = convertView.findViewById(R.id.menu_item_img);
            holder.name = convertView.findViewById(R.id.menu_item_name);
            holder.ratingBar = convertView.findViewById(R.id.menu_item_ratingBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Menu menu = menus.get(position);
        holder.name.setText(menu.getMenuName());
        holder.ratingBar.setRating(Float.parseFloat(menu.getScore()));
        if (menu.getImgUrl() != null) {
            Glide.with(context)
                    .load(App.URL + menu.getImgUrl())
                    .into(holder.menuImg);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView menuImg;
        private TextView name;
        private RatingBar ratingBar;
    }
}
