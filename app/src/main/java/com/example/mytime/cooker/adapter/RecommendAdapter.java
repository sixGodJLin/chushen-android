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
import com.example.mytime.cooker.bean.DayRecommend;

import java.util.List;

/**
 * Created by My time on 2017/10/14
 */

public class RecommendAdapter extends BaseAdapter {
    private static final String TAG = "RecommendAdapter";
    private Context context;
    private List<DayRecommend> dayRecommends;

    public RecommendAdapter(Context context , List<DayRecommend> dayRecommends){
        this.context = context;
        this.dayRecommends = dayRecommends;
    }


    @Override
    public int getCount() {
        return dayRecommends.size();
    }
    @Override
    public Object getItem(int position) {
        return dayRecommends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dayRecommends.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.recommend_item,null);
            holder = new ViewHolder();
            holder.recommend_name = (TextView) convertView.findViewById(R.id.recommend_item_name);
            holder.recommend_acoount = (TextView) convertView.findViewById(R.id.recommend_item_account);
            holder.recommend_price = (TextView) convertView.findViewById(R.id.recommend_item_price);
            holder.recommend_pic = (ImageView) convertView.findViewById(R.id.recommend_item_img);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        DayRecommend dayRecommend = dayRecommends.get(position);
        holder.recommend_name.setText(dayRecommend.getItem().getName());
        holder.recommend_acoount.setText(dayRecommend.getAccount());
        holder.recommend_price.setText("￥" + dayRecommend.getItem().getPrice());
        if (dayRecommend.getItem().getImgUrlJson() != null){
            Glide.with(context)
                    .load(dayRecommend.getItem().getImgUrlJson())
                    .into(holder.recommend_pic);
        }

        return convertView;
    }

    class ViewHolder{
        private TextView recommend_name;
        private TextView recommend_acoount;
        private TextView recommend_price;
        private ImageView recommend_pic;
    }
}
