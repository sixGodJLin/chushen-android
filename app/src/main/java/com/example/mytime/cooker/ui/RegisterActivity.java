package com.example.mytime.cooker.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.User;
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


public class RegisterActivity extends Activity {
    private static final String TAG ="Register" ;
    @Bind(R.id.register_username)
    EditText edit_username;
    @Bind(R.id.register_password)
    EditText edit_password;
    @Bind(R.id.btn_register)
    Button btnRegister;

    private Context context;
    private User user;
    private TextView title_text;
    private ImageView img_back;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext() , "添加失败" , Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext() , "添加成功" , Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.context = getApplicationContext();
        ButterKnife.bind(this);


        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("注册");

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new MyBackOnClickListener());
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        String username = edit_username.getText().toString();
        String password = edit_password.getText().toString();

        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(username + "用户");
        user.setEnable(true);

        String json = new Gson().toJson(user);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = App.URL + "/user";
        OkHttpClient client = new OkHttpClient();
        RequestBody body=RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
        //new call
        Call call = client.newCall(request);
        //请求调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() >= 200 && response.code() <300){
                    handler.sendEmptyMessage(2);
                }else {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private class MyBackOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
