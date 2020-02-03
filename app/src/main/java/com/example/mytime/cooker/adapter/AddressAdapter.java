package com.example.mytime.cooker.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.Address;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.CallBack;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created by My time on 2017/11/1
 */

public class AddressAdapter extends BaseAdapter {
    private static final String TAG = "AddressAdapter";
    private List<Address> addresses;
    private MyClickListener mListener;

    private Context context;

    public AddressAdapter(Context context, List<Address> addresses, MyClickListener listener) {
        this.context = context;
        this.addresses = addresses;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int position) {
        return addresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return addresses.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.address_item, null);
            holder = new ViewHolder();
            holder.add_name = (TextView) convertView.findViewById(R.id.add_item_name);
            holder.add_phone = (TextView) convertView.findViewById(R.id.add_item_phone);
            holder.add_info = (TextView) convertView.findViewById(R.id.add_item_addinfo);
            holder.add_cb = (CheckBox) convertView.findViewById(R.id.add_cb);
            holder.add_edit = (TextView) convertView.findViewById(R.id.add_item_edit);
            holder.add_deletd = (TextView) convertView.findViewById(R.id.add_item_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Address address = addresses.get(position);
        Log.i(TAG, "getView: " + address);
        holder.add_name.setText(address.getReceiverName());
        holder.add_phone.setText(address.getTel());
        holder.add_info.setText(address.getAddressInfo());
        holder.add_cb.setChecked(address.isDefault());

        holder.add_edit.setOnClickListener(mListener);
        holder.add_deletd.setOnClickListener(mListener);
        holder.add_edit.setTag(position);
        holder.add_deletd.setTag(position);
        return convertView;
    }

    class ViewHolder {
        private TextView add_name;
        private TextView add_phone;
        private TextView add_edit;
        private TextView add_deletd;
        private TextView add_info;
        private CheckBox add_cb;
    }

    //修改地址的点击事件
    public static abstract class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_item_edit:
                    myOnClick((Integer) v.getTag(), v);
                    break;
                case R.id.add_item_delete:
                    myOnClick((Integer) v.getTag(), v);
                    break;
            }
        }
        public abstract void myOnClick(int position, View v);
    }
}
