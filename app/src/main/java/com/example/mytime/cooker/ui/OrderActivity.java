package com.example.mytime.cooker.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.OrderAdapter;
import com.example.mytime.cooker.bean.Order;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.Global;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends AppCompatActivity {

    private static final String TAG = "OrderActivity";
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.img_collect)
    ImageView imgCollect;
    @Bind(R.id.order_list)
    ListView orderList;

    private Context context;

    private List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        this.context = getApplicationContext();
        titleText.setText("订单");

        initData();
    }

    private void initData() {
        FinalHttp http = new FinalHttp();
        String url = App.URL + "order/user/1/page/0/size/10/";
        http.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement getOrder_json = parser.parse(t)
                        .getAsJsonObject().getAsJsonArray("content");
                Log.i(TAG, "onSuccess: " + getOrder_json);

                orders = Global.getGson()
                        .fromJson(getOrder_json, new TypeToken<List<Order>>() {
                        }.getType());
                OrderAdapter adapter = new OrderAdapter(context, orders);
                orderList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
