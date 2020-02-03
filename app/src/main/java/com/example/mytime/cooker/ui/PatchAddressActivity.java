package com.example.mytime.cooker.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class PatchAddressActivity extends Activity {

    private static final String TAG = "PatchAddressActivity";
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.patch_receiver_name)
    EditText patchReceiverName;
    @Bind(R.id.patch_receiver_addressInfo)
    EditText patchReceiverAddressInfo;
    @Bind(R.id.patch_receiver_tel)
    EditText patchReceiverTel;
    @Bind(R.id.patch_receiver_postCode)
    EditText patchReceiverPostCode;
    @Bind(R.id.btn_save)
    Button btnSave;

    private Context context;
    private Address address;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext() , "修改失败" , Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext() , "修改成功" , Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patch_address);
        ButterKnife.bind(this);
        this.context = getApplicationContext();

        initData();
    }

    private void initData() {
        titleText.setText("修改地址");

        address = (Address) getIntent().getSerializableExtra("address");
        patchReceiverName.setText(address.getReceiverName());
        patchReceiverAddressInfo.setText(address.getAddressInfo());
        patchReceiverTel.setText(address.getTel());
        patchReceiverPostCode.setText(""+address.getPostCode());
    }

    @OnClick({R.id.img_back, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_save:
                changeAddressInfo();
                break;
        }
    }

    private void changeAddressInfo() {
        Address newAddress = new Address();

        newAddress.setReceiverName(patchReceiverName.getText().toString());
        newAddress.setAddressInfo(patchReceiverAddressInfo.getText().toString());
        newAddress.setTel(patchReceiverTel.getText().toString());
        newAddress.setPostCode(Integer.parseInt(patchReceiverPostCode.getText().toString()));
        newAddress.setByUserid(address.getByUserid());
        newAddress.setDefault(address.isDefault());

        String json = new Gson().toJson(newAddress);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String url = App.URL + "address/" + address.getId();
        OkHttpClient client = new OkHttpClient();
        RequestBody body=RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .build();
        Call call= client.newCall(request);
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
