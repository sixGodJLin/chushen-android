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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.ItemCollectionAdapter;
import com.example.mytime.cooker.adapter.MenuCollectionAdapter;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.bean.ItemCollection;
import com.example.mytime.cooker.bean.Menu;
import com.example.mytime.cooker.bean.MenuCollection;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.App;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class ItemCollectionActivity extends Activity {

    private static final String TAG = "ItemCollectionActivity";
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.title_text)
    TextView title_text;
    @Bind(R.id.item_collection_list)
    ListView collectionList;

    private Context context;
    private int type = 1;

    private List<ItemCollection> itemCollections;
    private List<MenuCollection> menuCollections;
    private ItemCollectionAdapter itemAdapter;
    private MenuCollectionAdapter menuCollectionAdapter;

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
        setContentView(R.layout.activity_item_collection);
        ButterKnife.bind(this);
        this.context = getApplicationContext();

        String collectionType = getIntent().getStringExtra("collectionType");
        if (collectionType.equals("item")){
            title_text.setText("商品收藏");
            initItemList();
        }else if (collectionType.equals("menu")){
            title_text.setText("菜谱收藏");
            initMenuList();
        }
    }

    //加载菜谱收藏列表
    private void initMenuList() {
        FinalHttp getMenuCollectionHttp = new FinalHttp();
        String getMenuCollectionUrl = App.URL + "menu_collection/" + type + "/";
        getMenuCollectionHttp.get(getMenuCollectionUrl, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json = parser.parse(t);

                menuCollections = Global.getGson()
                        .fromJson(json,new TypeToken<List<MenuCollection>>() {
                        }.getType());

                Log.i(TAG, "onSuccess: " + "12345665412231232");
                Log.i(TAG, "onSuccess: " + menuCollections);

                menuCollectionAdapter = new MenuCollectionAdapter(context, menuCollections, myMenuDeleteClickListener);
                collectionList.setAdapter(menuCollectionAdapter);

                collectionList.setOnItemClickListener(new myMenuItemOnclickListener());
            }
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    //加载商品收藏列表
    private void initItemList() {
        FinalHttp getItemCollettion_http = new FinalHttp();
        String getItemcollettion_url = App.URL + "item_collection/" + type + "/";
        getItemCollettion_http.get(getItemcollettion_url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json = parser.parse(t);

                itemCollections = Global.getGson()
                        .fromJson(json,new TypeToken<List<ItemCollection>>() {
                        }.getType());

                itemAdapter = new ItemCollectionAdapter(context,itemCollections,myDeleteClickListener);
                collectionList.setAdapter(itemAdapter);

                collectionList.setOnItemClickListener(new MyItemOnClickListener());
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    //菜谱收藏列表点击事件
    private class myMenuItemOnclickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Menu menu = menuCollections.get(position).getMenu();
            Bus.bean.setMenu(menu);
            Intent intent = new Intent();
            intent.setClass(context, MenuDetailActivity.class);
            startActivity(intent);
        }
    }

    //商品收藏列表点击事件
    private class MyItemOnClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item item = itemCollections.get(position).getItem();
            Bus.bean.setItem(item);
            Intent intent = new Intent();
            intent.setClass(context, GoodsdetailActivity.class);
            startActivity(intent);
        }
    }

    //点击、删除收藏菜谱
    private MenuCollectionAdapter.MyMenuDeleteClickListener myMenuDeleteClickListener = new MenuCollectionAdapter.MyMenuDeleteClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            String deleteType = "menu_collection/";
            delete(deleteType, menuCollections.get(position).getId(),"menu");
        }
    };

    //点击、删除收藏商品
    private ItemCollectionAdapter.MyDeleteClickListener myDeleteClickListener = new ItemCollectionAdapter.MyDeleteClickListener() {
        @Override
        public void myOnClick(final int position, View v) {
            String deleteType = "item_collection/";
            delete(deleteType, itemCollections.get(position).getId(),"item");
        }
    };

    public void delete(String deleteType, int id, final String resume){
        String url = App.URL + deleteType + id;
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
                if (resume.equals("item")){
                    initItemList();
                }else if (resume.equals("menu")){
                    initMenuList();
                }
            }
        });
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
