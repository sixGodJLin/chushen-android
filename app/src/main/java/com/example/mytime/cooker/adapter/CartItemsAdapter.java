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
import com.example.mytime.cooker.bean.Cart;

import java.util.List;

/**
 * Created by My time on 2017/12/6
 */

public class CartItemsAdapter extends BaseAdapter {
    private Context context;
    private List<Cart> carts;

    public CartItemsAdapter(Context context , List<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    @Override
    public int getCount() {
        return carts.size();
    }

    @Override
    public Object getItem(int position) {
        return carts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return carts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_buy_items, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.cart_buy_itemImg);
            holder.itemName = (TextView) convertView.findViewById(R.id.cart_buy_itemName);
            holder.buyNum = (TextView) convertView.findViewById(R.id.cart_buyNum);
            holder.itemPrice = (TextView) convertView.findViewById(R.id.cart_buy_itemPrice);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Cart cart = carts.get(position);
        holder.itemName.setText(cart.getItem().getName());
        holder.itemPrice.setText("单价：￥" + cart.getItem().getPrice());
        holder.buyNum.setText("数量x" + cart.getBuyNum());

        Glide.with(context)
                .load(cart.getItem().getImgUrlJson())
                .into(holder.imageView);
        return convertView;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView itemName;
        private TextView buyNum;
        private TextView itemPrice;
    }
}
