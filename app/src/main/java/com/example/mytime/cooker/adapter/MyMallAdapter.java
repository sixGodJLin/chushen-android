package com.example.mytime.cooker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.utils.App;

import java.util.List;

/**
 * Created by My time on 2017/10/14
 */

public class MyMallAdapter extends BaseAdapter {
    private static final String TAG = "MyMallAdapter";
    private Context context;
    private List<Item> items;

    public MyMallAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.goods_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.goods_item_name);
            holder.shop_name = (TextView) convertView.findViewById(R.id.goods_item_shopname);
            holder.price = (TextView) convertView.findViewById(R.id.goods_item_price);
            holder.sell_num = (TextView) convertView.findViewById(R.id.goods_item_sell_num);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.goods_item_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = items.get(position);
        holder.name.setText(item.getName());
        holder.shop_name.setText(item.getShop().getName());
        holder.price.setText("￥" + item.getPrice());
        holder.sell_num.setText("售量：" + item.getSellNum());
        if (item.getImgUrlJson() != null) {
            String item_url = item.getImgUrlJson().startsWith("http")
                    ? item.getImgUrlJson()
                    : App.URL + item.getImgUrlJson();
            Glide.with(context)
                    .load(item_url)
                    .into(holder.item_pic);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView name;
        private TextView shop_name;
        private TextView price;
        private TextView sell_num;
        private ImageView item_pic;
    }
}
