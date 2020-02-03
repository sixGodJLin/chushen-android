package com.example.mytime.cooker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.Cart;
import com.example.mytime.cooker.ui.CartActivity;
import com.example.mytime.cooker.utils.Bus;

import java.util.List;

/**
 * Created by My time on 2017/10/28
 */

public class CartAdapter extends BaseAdapter {
    private static final String TAG = "CartAdapter";
    private Context context;
    private static List<Cart> carts;

    public CartAdapter(Context context, List<Cart> carts) {
        this.context = context;
        CartAdapter.carts = carts;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_list_item, null);
            holder = new ViewHolder();
            holder.item_pic = (ImageView) convertView.findViewById(R.id.cart_item_img);
            holder.itme_name = (TextView) convertView.findViewById(R.id.cart_item_name);
            holder.buy_num = (TextView) convertView.findViewById(R.id.cart_item_buynum);
            holder.price = (TextView) convertView.findViewById(R.id.cart_item_price);
            holder.cb_item = (CheckBox) convertView.findViewById(R.id.cart_item_cb);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.cart_item_delete);
            holder.img_add = (ImageView) convertView.findViewById(R.id.cart_item_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Cart cart = carts.get(position);
        holder.itme_name.setText(cart.getItem().getName());
        holder.buy_num.setText("" + cart.getBuyNum());
        holder.price.setText("￥" + cart.getItem().getPrice());
        holder.cb_item.setChecked(cart.isChecked());
        Bus.bean.setPrice(calcTotalPrice());

        Glide.with(context)
                .load(cart.getItem().getImgUrlJson())
                .into(holder.item_pic);

        final ViewHolder finalHolder = holder;
        // 减少数量
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(finalHolder.buy_num.getText().toString()) - 1;
                if (num < 0) {
                    return;
                }
                finalHolder.buy_num.setText("" + num);
                //计算总价
                cart.setBuyNum(num);
                Bus.bean.setPrice(calcTotalPrice());
            }
        });
        // 增加数量
        holder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(finalHolder.buy_num.getText().toString()) + 1;
                finalHolder.buy_num.setText("" + num);
                //计算总价
                cart.setBuyNum(num);
                Bus.bean.setPrice(calcTotalPrice());
            }
        });


        //选中商品
        holder.cb_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cart.setChecked(isChecked);
                if (!isChecked) {
                    CartActivity.setSelectAllBtnState(false);
                }else {
                    boolean isAllSelect = true;
                    for (Cart cart : carts){
                        if (!cart.isChecked()){
                            Log.i(TAG, "onCheckedChanged: " + cart.isChecked());
                            isAllSelect = false;
                        }
                    }
                    if (isAllSelect) {
                        CartActivity.setSelectAllBtnState(true);
                    }
                }
                Bus.bean.setPrice(calcTotalPrice());
            }
        });

        return convertView;
    }

    // 全选
    public static void selectAll(boolean isAll){
        for (Cart cart : carts){
            cart.setChecked(isAll);
        }
    }

    private double calcTotalPrice() {
        double price = 0;
        for (Cart cart : carts) {
            if (cart.isChecked()){
                price += cart.getBuyNum() * cart.getItem().getPrice();
            }
        }
        return price;
    }


    class ViewHolder {
        private ImageView item_pic;
        private TextView itme_name;
        private TextView buy_num;
        private TextView price;
        private CheckBox cb_item;
        private ImageView img_delete;
        private ImageView img_add;
    }
}
