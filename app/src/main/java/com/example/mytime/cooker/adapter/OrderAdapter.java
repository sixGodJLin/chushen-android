package com.example.mytime.cooker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.bean.Order;
import com.example.mytime.cooker.bean.OrderItem;
import com.example.mytime.cooker.fragment.HomeFragment;
import com.example.mytime.cooker.utils.ListUtil;
import com.example.mytime.cooker.utils.TimeUtil;

import java.util.List;


/**
 * Created by My time on 2017/11/1
 */

public class OrderAdapter extends BaseAdapter {
    private static final String TAG = "OrderAdapter";
    private List<Order> orders;


    private Context context;
    public OrderAdapter(Context context , List<Order> orders){
        this.context = context;
        this.orders = orders;
    }
    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orders.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item,null);
            holder = new ViewHolder();
            holder.createTime = (TextView) convertView.findViewById(R.id.order_detail_createTime);
            holder.listView = (ListView) convertView.findViewById(R.id.order_detail_items);
            holder.state = (TextView) convertView.findViewById(R.id.order_state);
            holder.buyNumAndPrice = (TextView) convertView.findViewById(R.id.order_item_buyNum_totalPrice);
            holder.notPayedLayout = (LinearLayout) convertView.findViewById(R.id.isNotPayedLayout);
            holder.payedLayout = (LinearLayout) convertView.findViewById(R.id.isPayedLayout);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Order order = orders.get(position);
        List<OrderItem> orderItems = order.getOrderItems();
        int totalItemNum = 0;
        for (OrderItem orderItem : orderItems){
            totalItemNum += orderItem.getItemNum();
        }

        OrderDetailItemAdapter adapter = new OrderDetailItemAdapter(context , orderItems);
        holder.listView.setAdapter(adapter);

        //设置list的高度
        ListUtil list_util = new ListUtil();
        list_util.setListViewHeightBasedOnChildren(holder.listView);

        holder.buyNumAndPrice.setText("共" + totalItemNum
                + "件商品 合计  ¥" + order.getTotalPrice() + "（含运费¥0.00）");

        holder.createTime.setText("下单时间" + TimeUtil.formatTimestamp(order.getCreateTime()));

        if (order.getIsPayed() == 1){
            holder.state.setText("等待发货");
            holder.payedLayout.setVisibility(View.VISIBLE);
            holder.notPayedLayout.setVisibility(View.GONE);
        }else {
            holder.state.setText("等待支付");
            holder.payedLayout.setVisibility(View.GONE);
            holder.notPayedLayout.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder{
        private TextView createTime;
        private TextView state;
        private ListView listView;
        private TextView buyNumAndPrice;
        private LinearLayout notPayedLayout;
        private LinearLayout payedLayout;
    }
}
