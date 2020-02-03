package com.example.mytime.cooker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.OrderItem;

import java.util.List;

/**
 * Created by My time on 2017/12/6
 */

public class OrderDetailItemAdapter extends BaseAdapter {
    private Context context;
    private List<OrderItem> orderItems;

    public OrderDetailItemAdapter(Context context, List<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @Override
    public int getCount() {
        return orderItems.size();
    }

    @Override
    public Object getItem(int position) {
        return orderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.order_item_itemImg);
            holder.itemName = (TextView) convertView.findViewById(R.id.order_item_itemName);
            holder.itemPrice = (TextView) convertView.findViewById(R.id.order_item_itemPrice);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderItem orderItem = orderItems.get(position);
        holder.itemName.setText(orderItem.getItem().getName());
        holder.itemPrice.setText("单价" + orderItem.getItem().getPrice());
        Glide.with(context)
                .load(orderItem.getItem().getImgUrlJson())
                .into(holder.imageView);
        return convertView;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView itemName;
        private TextView itemPrice;
    }
}
