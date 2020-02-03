package com.example.mytime.cooker.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.CartAdapter;
import com.example.mytime.cooker.bean.Address;
import com.example.mytime.cooker.bean.Cart;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.CallBack;
import com.example.mytime.cooker.utils.Global;
import com.example.mytime.cooker.utils.ListUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends Activity {

    private static final String TAG = "CartActivity";
    @Bind(R.id.title_text)
    TextView title_Text;
    @Bind(R.id.img_collect)
    ImageView imgCollect;
    @Bind(R.id.list_cart_item)
    ListView list_cartItem;
    @Bind(R.id.total_price)
    TextView total_price;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.btn_sub_orders)
    Button btn_sub_orders;
    @Bind(R.id.cart_seleteAll)
    CheckBox cart_seleteAll;
    @Bind(R.id.cart_delete)
    TextView cart_delete;
    @Bind(R.id.cart_receiver_name)
    TextView cartReceiverName;
    @Bind(R.id.cart_receiver_phone)
    TextView cartReceiverPhone;
    @Bind(R.id.cart_receiver_address_info)
    TextView cartReceiverAddressInfo;
    @Bind(R.id.cart_address_layout)
    LinearLayout cartAddressLayout;

    List<Cart> carts;
    int type_id = 1;
    private static CheckBox cart_seleteAll_another;
    private CartAdapter cart_adapter;
    private Context context;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "移除失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "从购物车中移除成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        this.context = getApplicationContext();

        title_Text.setText("购物车");
        cart_seleteAll_another = cart_seleteAll;

        addCartList();
    }

    //添加购物车列表数据
    private void addCartList() {
        FinalHttp http_getCart = new FinalHttp();
        final String url_getCart = App.URL + "cart/search/getCart?userId=" + type_id;
        http_getCart.get(url_getCart, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json_getCart_list = parser.parse(t)
                        .getAsJsonObject().get("_embedded").getAsJsonObject()
                        .getAsJsonArray("carts");

                System.out.println("-- > " + json_getCart_list);

                carts = Global.getGson()
                        .fromJson(json_getCart_list, new TypeToken<List<Cart>>() {
                        }.getType());

                cart_adapter = new CartAdapter(context, carts);
                list_cartItem.setAdapter(cart_adapter);

                //设置list的高度
                ListUtil list_util = new ListUtil();
                list_util.setListViewHeightBasedOnChildren(list_cartItem);

                // 设置double类型数据保留2位小数
                final NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(2);

                Bus.bean.registerSetPriceCallBack(new CallBack() {
                    @Override
                    public void call() {
                        total_price.setText("总价：￥" + nf.format(Bus.bean.getPrice()));
                    }
                });
                total_price.setText("总价：￥" + Bus.bean.getPrice());
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    @OnClick({R.id.img_back, R.id.btn_sub_orders, R.id.cart_seleteAll, R.id.cart_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.cart_seleteAll:
                CartAdapter.selectAll(cart_seleteAll.isChecked());
                cart_adapter.notifyDataSetChanged();
                break;
            case R.id.cart_delete:
                List<Integer> ids = new ArrayList<>();
                for (Cart cart : carts) {
                    if (cart.isChecked()) {
                        ids.add(cart.getId());
                    }
                }
                String json = new Gson().toJson(ids);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, json);
                String url = App.URL + "cart";
                Request request = new Request.Builder()
                        .url(url)
                        .delete(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        handler.sendEmptyMessage(2);
                        addCartList();
                    }
                });
                break;
            case R.id.btn_sub_orders:
                //提交订单
                List<Cart> purchaseCarts = new ArrayList<>();
                for (Cart cart : carts) {
                    if (cart.isChecked()) {
                        purchaseCarts.add(cart);
                    }
                }
                Bus.bean.setCarts(purchaseCarts);
                Intent intent2 = new Intent();
                intent2.putExtra("single1_or_cart2", 2);
                intent2.setClass(CartActivity.this, OrderDetailActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Bus.bean.getAddress() == null) {
            return;
        }
        cartAddressLayout.setVisibility(View.VISIBLE);
        Address address = Bus.bean.getAddress();
        cartReceiverName.setText("收件人：" + address.getReceiverName());
        cartReceiverPhone.setText("联系电话：" + address.getTel());
        cartReceiverAddressInfo.setText("地址：" + address.getAddressInfo());
    }

    public static void setSelectAllBtnState(boolean state) {
        cart_seleteAll_another.setChecked(state);
    }

}
