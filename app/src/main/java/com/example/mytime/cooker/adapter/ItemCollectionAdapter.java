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
import com.example.mytime.cooker.bean.ItemCollection;

import java.util.List;

/**
 * Created by My time on 2017/10/14
 */

public class ItemCollectionAdapter extends BaseAdapter {
    private static final String TAG = "MyMallAdapter";
    private Context context;
    private List<ItemCollection> collections;
    private MyDeleteClickListener myDeleteClickListener;

    public ItemCollectionAdapter(Context context , List<ItemCollection> collections,MyDeleteClickListener myDeleteClickListener){
        this.context = context;
        this.collections = collections;
        this.myDeleteClickListener = myDeleteClickListener;
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return collections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return collections.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.collection_item,null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.collection_item_name);
            holder.price = (TextView) convertView.findViewById(R.id.collection_price);
            holder.sell_num = (TextView) convertView.findViewById(R.id.collection_sellNum);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.collection_item_img);
            holder.delete = (ImageView) convertView.findViewById(R.id.collection_delete);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemCollection collection = collections.get(position);
        holder.name.setText(collection.getItem().getName());
        holder.price.setText("￥"+collection.getItem().getPrice());
        holder.sell_num.setText("售量："+ collection.getItem().getSellNum());
        if (collection.getItem().getImgUrlJson() != null){
            Glide.with(context)
                    .load(collection.getItem().getImgUrlJson())
                    .into(holder.item_pic);
        }

        holder.delete.setOnClickListener(myDeleteClickListener);
        holder.delete.setTag(position);

        return convertView;
    }
    public static abstract class MyDeleteClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
            Log.i(TAG, "onClick: " + v.getTag() + "123" + v);
        }
        public abstract void myOnClick(int position, View v);
    }


    class ViewHolder{
        private TextView name;
        private TextView price;
        private TextView sell_num;
        private ImageView item_pic;
        private ImageView delete;
    }
}
