package com.example.mytime.cooker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.AddressAdapter;
import com.example.mytime.cooker.bean.Address;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.Global;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends Activity {
    private String TAG = "AddressActivity";

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.add_receiver_address)
    Button addReceiverAddress;
    @Bind(R.id.title_text)
    TextView title_text;
    @Bind(R.id.img_collect)
    ImageView imgCollect;
    @Bind(R.id.list_address)
    ListView list_address;

    List<Address> addresses;
    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        this.context = getApplicationContext();
        title_text.setText("收货地址");
        addAddressList();
    }

    //添加地址列表
    private void addAddressList() {
        FinalHttp getAddress_http = new FinalHttp();
        String getAdress_url = App.URL + "address/search/getAddress?userid=" + Bus.bean.getUser().getId();
        getAddress_http.get(getAdress_url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json_getAddress_list = parser.parse(t)
                        .getAsJsonObject().get("_embedded").getAsJsonObject()
                        .getAsJsonArray("addresses");
                addresses = Global.getGson()
                        .fromJson(json_getAddress_list, new TypeToken<List<Address>>() {
                        }.getType());

                AddressAdapter addressAdapter = new AddressAdapter(context, addresses, myClickListener);
                list_address.setAdapter(addressAdapter);

                list_address.setOnItemClickListener(new MyItemOnClickListener());
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    private AddressAdapter.MyClickListener myClickListener = new AddressAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            switch (v.getId()){
                case R.id.add_item_edit:
                    Address address = addresses.get(position);
                    Intent intent = new Intent(context , PatchAddressActivity.class);
                    intent.putExtra("address",address);
                    startActivity(intent);
                    break;
                case R.id.add_item_delete:
                    String url = App.URL + "address/" + addresses.get(position).getId();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .delete()
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
                            addAddressList();
                        }
                    });
                    break;

            }
        }
    };

    @OnClick({R.id.img_back, R.id.add_receiver_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.add_receiver_address:
                Intent intent = new Intent();
                intent.setClass(context, AddAddressActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class MyItemOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Address address = addresses.get(position);
            Log.i(TAG, "onItemClick: " + address);
            Bus.bean.setAddress(address);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addAddressList();
    }
}
