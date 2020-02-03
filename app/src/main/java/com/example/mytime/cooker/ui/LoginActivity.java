package com.example.mytime.cooker.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytime.cooker.MainActivity;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.User;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.edit_username)
    EditText edit_Username;
    @Bind(R.id.edit_password)
    EditText edit_Password;
    @Bind(R.id.btn_login)
    Button btn_Login;
    @Bind(R.id.btn_register)
    Button btn_Register;

    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                final String username = edit_Username.getText().toString();
                final String password = edit_Password.getText().toString();

                FinalHttp http = new FinalHttp();

                String url = App.URL + "user/search/login?username=" + username + "&password=" + password + "&projection=inlineUser";
                http.get(url, new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String t) {
                        saveUserInfo();
                        User user = new Gson().fromJson(t,
                                new TypeToken<User>() {
                                }.getType());
                        Bus.bean.setUser(user);
                        intoMainActivity();
                    }

                    @Override
                    public void onFailure(Throwable t, String strMsg) {
                        super.onFailure(t, strMsg);
                        Log.i(TAG, "onFailure: -----" + t);
                        Toast.makeText(getApplication(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                    }

                    private void saveUserInfo() {
                        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.commit();
                    }
                });

                break;
            case R.id.btn_register:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void intoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
