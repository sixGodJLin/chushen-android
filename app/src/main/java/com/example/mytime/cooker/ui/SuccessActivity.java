package com.example.mytime.cooker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytime.cooker.MainActivity;
import com.example.mytime.cooker.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessActivity extends AppCompatActivity {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.ToOrder)
    TextView ToOrder;
    @Bind(R.id.ToMain)
    TextView ToMain;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.bind(this);
        this.context = getApplicationContext();
    }

    @OnClick({R.id.img_back, R.id.ToOrder, R.id.ToMain})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.img_back:
                intent.setClass(context , MainActivity.class);
                startActivity(intent);
                break;
            case R.id.ToOrder:
                intent.setClass(context , OrderActivity.class);
                startActivity(intent);
                break;
            case R.id.ToMain:
                intent.setClass(context , MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
