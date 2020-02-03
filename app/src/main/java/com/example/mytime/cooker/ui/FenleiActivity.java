package com.example.mytime.cooker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.MyMallAdapter;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Global;
import com.example.mytime.cooker.utils.ListUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FenleiActivity extends Activity {
    private String TAG = "FenleiActivity";
    @Bind(R.id.img_back)
    ImageView img_Back;
    @Bind(R.id.bar_edit_search)
    EditText bar_edit_Search;
    @Bind(R.id.fenlei_activity_list)
    ListView fenlei_List;

    private Map<Integer, Item> itemMap = new HashMap<>();
    private List<Item> items;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenlei);
        ButterKnife.bind(this);

        this.context = getApplicationContext();

        FinalHttp http_getType_list = new FinalHttp();
        final String url_getType_list = App.URL + "type/" + Bus.bean.getType() + "/";
        http_getType_list.get(url_getType_list, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json_getType_list = parser.parse(t)
                        .getAsJsonObject()
                        .getAsJsonArray("items");
                items = Global.getGson()
                        .fromJson(json_getType_list,new TypeToken<List<Item>>() {
                }.getType());

                for (Item item : items) {
                    itemMap.put(item.getId(), item);
                }

                MyMallAdapter item_adapter = new MyMallAdapter(context, items);
                fenlei_List.setAdapter(item_adapter);

                fenlei_List.setOnItemClickListener(new MyItemClickListener());

                //设置list的高度
                ListUtil list_util = new ListUtil();
                list_util.setListViewHeightBasedOnChildren(fenlei_List);
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    //商品列表中Item的点击事件 -->  进入到该商品的详情页面
    private class MyItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item item = itemMap.get(items.get(position).getId());
            Bus.bean.setItem(item);
            Intent intent = new Intent();
            intent.setClass(context, GoodsdetailActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
