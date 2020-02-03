package com.example.mytime.cooker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.CartItemsAdapter;
import com.example.mytime.cooker.bean.Address;
import com.example.mytime.cooker.bean.Cart;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.bean.Order;
import com.example.mytime.cooker.bean.OrderItem;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends AppCompatActivity {

    private static final String TAG = "OrderDetailActivity";
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.order_detail_receiver_name)
    TextView orderDetailReceiverName;
    @Bind(R.id.order_detail_receiver_phone)
    TextView orderDetailReceiverPhone;
    @Bind(R.id.order_detail_receiver_address)
    TextView orderDetailReceiverAddress;
    @Bind(R.id.order_detail_address_layout)
    LinearLayout orderDetailAddressLayout;
    @Bind(R.id.order_detail_item_img)
    ImageView orderDetailItemImg;
    @Bind(R.id.order_detail_item_name)
    TextView orderDetailItemName;
    @Bind(R.id.order_detail_item_num)
    TextView orderDetailItemNum;
    @Bind(R.id.order_detail_item_price)
    TextView orderDetailItemPrice;
    @Bind(R.id.total_price)
    TextView totalPrice;
    @Bind(R.id.do_pay)
    TextView doPay;
    @Bind(R.id.single)
    LinearLayout single;
    @Bind(R.id.many)
    ListView many;

    private String total_price;
    public DecimalFormat df = new DecimalFormat("#.00");
    private List<Address> addresses;
    private List<Cart> buyingCarts;
    private Item item;
    private Context context;
    private int buyNum;
    private int orderType;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        this.context = getApplicationContext();
        orderType = (int) getIntent().getSerializableExtra("single1_or_cart2");
        initData();
    }

    private void initData() {
        titleText.setText("订单页面");
        if (orderType == 1) {
            single.setVisibility(View.VISIBLE);
            many.setVisibility(View.GONE);
            buyNum = Integer.parseInt(Bus.bean.getBuyNum());
            addSingleInfo();
        } else {
            single.setVisibility(View.GONE);
            many.setVisibility(View.VISIBLE);
            totalPrice.setText(""+ Bus.bean.getPrice());
            addCartInfo();
        }
        getAddressInfo();
    }

    @OnClick({R.id.img_back, R.id.do_pay, R.id.order_detail_address_layout})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.do_pay:
                addOrderTable();
                break;
            case R.id.order_detail_address_layout:
                intent = new Intent();
                intent.setClass(context, AddressActivity.class);
                startActivity(intent);
                break;
        }
    }

    //添加购物车购买时商品的信息
    private void addCartInfo() {
        buyingCarts = Bus.bean.getCarts();
        CartItemsAdapter cartItemsAdapter = new CartItemsAdapter(context, buyingCarts);
        many.setAdapter(cartItemsAdapter);
        //设置list的高度
        ListUtil list_util = new ListUtil();
        list_util.setListViewHeightBasedOnChildren(many);
    }

    //添加单个商品购买屎的订单信息
    private void addSingleInfo() {
        item = Bus.bean.getItem();
        orderDetailItemName.setText(item.getName());
        orderDetailItemNum.setText("数量x" + buyNum);
        orderDetailItemPrice.setText("单价：￥" + item.getPrice());
        Glide.with(context)
                .load(item.getImgUrlJson())
                .into(orderDetailItemImg);
        total_price = df.format(buyNum * item.getPrice());
        totalPrice.setText(total_price);
    }

    //获取地址信息
    private void getAddressInfo() {
        FinalHttp getAddress_http = new FinalHttp();
        String getAddress_url = App.URL + "address/search/getAddress?userid=" + 1;
        getAddress_http.get(getAddress_url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json_getAddress_list = parser.parse(t)
                        .getAsJsonObject().get("_embedded").getAsJsonObject()
                        .getAsJsonArray("addresses");
                addresses = Global.getGson()
                        .fromJson(json_getAddress_list, new TypeToken<List<Address>>() {
                        }.getType());

                for (Address address : addresses) {
                    if (address.isDefault()) {
                        orderDetailReceiverName.setText(address.getReceiverName());
                        orderDetailReceiverPhone.setText(address.getTel());
                        orderDetailReceiverAddress.setText(address.getAddressInfo());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    //添加订单
    private void addOrderTable() {
        Order order = new Order();
        order.setAddressId(1);
        order.setUserId(1);         // ****
        order.setTotalPrice(Double.parseDouble((String) totalPrice.getText()));
        order.setIsDeliver((byte) 0);
        order.setIsFinished((byte) 0);

        if (10000 - Double.parseDouble((String) totalPrice.getText()) >= 0) {
            order.setIsPayed((byte) 1);
        } else {
            Toast.makeText(context, "余额不足，请充值", Toast.LENGTH_SHORT).show();
            order.setIsPayed((byte) 0);
        }
        order.setIsReceive((byte) 0);

        String json = new Gson().toJson(order);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = App.URL + "/order/add";

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //new call
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(3);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String id = response.body().string();
                addOrderItemTable(Integer.parseInt(id));
            }
        });
    }

    //添加订单的商品表
    private void addOrderItemTable(int orderId) {
        if (orderType == 1) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(new Item(item.getId()));
            orderItem.setOrder(new Order(orderId));
            orderItem.setItemNum(buyNum);
            orderItem.setOrderId(orderId);

            String orderItemJson = new Gson().toJson(orderItem, OrderItem.class);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String url = App.URL + "order_item/add";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, orderItemJson);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(3);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    handler.sendEmptyMessage(4);
                    Intent intent = new Intent();
                    intent.setClass(context, SuccessActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            List<OrderItem> orderItems = new ArrayList<>();
            for (Cart cart : buyingCarts){
                OrderItem orderItem = new OrderItem();
                orderItem.setItem(new Item(cart.getItem().getId()));
                orderItem.setOrder(new Order(orderId));
                orderItem.setItemNum(cart.getBuyNum());
                orderItem.setOrderId(orderId);
                orderItems.add(orderItem);
            }
            String orderItemJson = new Gson().toJson(orderItems);
            Log.i(TAG, "addOrderItemTable: " + orderItemJson);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String url = App.URL + "order_item/addCarts";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, orderItemJson);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(3);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    handler.sendEmptyMessage(4);
                    finishAndDeleteCarts();
                    Intent intent = new Intent();
                    intent.setClass(context, SuccessActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    //从购物车中下完订单后，从购物车中讲其商品删除
    private void finishAndDeleteCarts() {
        List<Integer> ids = new ArrayList<>();
        for (Cart cart : Bus.bean.getCarts()){
            ids.add(cart.getId());
        }
        String json = new Gson().toJson(ids);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body=RequestBody.create(JSON,json);
        String url = App.URL + "cart";
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Bus.bean.getAddress() == null) {
            return;
        }
        orderDetailReceiverName.setText(Bus.bean.getAddress().getReceiverName());
        orderDetailReceiverPhone.setText(Bus.bean.getAddress().getTel());
        orderDetailReceiverAddress.setText(Bus.bean.getAddress().getAddressInfo());
    }
}
