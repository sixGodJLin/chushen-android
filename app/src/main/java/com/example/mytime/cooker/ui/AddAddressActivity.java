package com.example.mytime.cooker.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.Address;
import com.example.mytime.cooker.utils.App;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAddressActivity extends Activity {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.add_receiver_name)
    EditText addReceiverName;
    @Bind(R.id.add_receiver_addressInfo)
    EditText addReceiverAddressInfo;
    @Bind(R.id.add_receiver_postCode)
    EditText addReceiverPostCode;
    @Bind(R.id.add_receiver_tel)
    EditText addReceiverTel;
    @Bind(R.id.btn_register)
    Button btnRegister;

    private Address address;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext() , "保存失败" , Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext() , "保存成功" , Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        titleText.setText("新增收获地址");
    }

    @OnClick({R.id.img_back, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_register:
                saveAddress();
                break;
        }
    }

    private void saveAddress() {
        address = new Address();
        address.setByUserid(1);
        address.setReceiverName(addReceiverName.getText().toString());
        address.setAddressInfo(addReceiverAddressInfo.getText().toString());
        address.setPostCode(Integer.parseInt(addReceiverPostCode.getText().toString()));
        address.setTel(addReceiverTel.getText().toString());
        address.setDefault(false);

        String addressJson = new Gson().toJson(address);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = App.URL + "/address";
        OkHttpClient client = new OkHttpClient();
        RequestBody body=RequestBody.create(JSON,addressJson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
            }
        });
    }
}
