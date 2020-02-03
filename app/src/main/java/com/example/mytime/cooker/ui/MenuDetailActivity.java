package com.example.mytime.cooker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.MenuAdapter;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.bean.Menu;
import com.example.mytime.cooker.bean.MenuCollection;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.Global;
import com.example.mytime.cooker.utils.ListUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuDetailActivity extends AppCompatActivity {

    private static final String TAG = "MenuDetailActivity";
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.img_collect)
    ImageView imgCollect;
    @Bind(R.id.main_stuff)
    TextView mainStuff;
    @Bind(R.id.assist_stuff)
    TextView assistStuff;
    @Bind(R.id.method_group)
    LinearLayout methodGroup;
    @Bind(R.id.menu_tips)
    TextView tips;
    @Bind(R.id.recommend_menuList)
    ListView recommendMenuList;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.menu_addCollection)
    LinearLayout menuAddCollection;
    @Bind(R.id.menu_addCart)
    LinearLayout menuAddCart;

    private Context context;
    Menu menu = Bus.bean.getMenu();
    List<Menu> menus;
    List<MenuCollection> menuCollections;
    private List<Item> items;
    private boolean same = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);
        ButterKnife.bind(this);
        this.context = getApplicationContext();
        initData();
    }

    //正则表达的使用,提取出烹饪方法中的图片地址
    private Pattern pattern = Pattern.compile("<img src=\"(.+)\"[ /]+>");

    private void initData() {
        titleText.setText(menu.getMenuName());
        mainStuff.setText(menu.getMainStuff());
        assistStuff.setText(menu.getAssistStuff());

        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int img_width = (int) (width * 0.7);

        String[] methods = menu.getMethod().split("；");
        for (String method : methods) {
            Matcher matcher = pattern.matcher(method);
            if (matcher.find()) {
                String image_url = matcher.group(1);
                ImageView imageView = new ImageView(this);

                String menu_img_url = image_url.startsWith("http")
                        ? image_url
                        : App.URL + image_url;

                Glide.with(context)
                        .load(menu_img_url)
                        .into(imageView);
                methodGroup.addView(imageView);
            } else {
                TextView textView = new TextView(this);
                textView.setText(method);
                textView.setPadding(5, 0, 5, 0);
                textView.setTextSize(20);
                methodGroup.addView(textView);
            }
        }

        ratingBar.setRating(Float.parseFloat(menu.getScore()));
        tips.setText(menu.getTips());

        initRecommendList();
    }

    //添加推荐菜单列表
    private void initRecommendList() {
        FinalHttp http = new FinalHttp();
        String url = App.URL + "search-service/menu/type/" + menu.getType();
        http.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json_recommendList = parser.parse(t)
                        .getAsJsonObject().getAsJsonArray("content");
                menus = Global.getGson()
                        .fromJson(json_recommendList, new TypeToken<List<Menu>>() {
                        }.getType());

                MenuAdapter adapter = new MenuAdapter(context, menus);
                recommendMenuList.setAdapter(adapter);

                ListUtil listUtil = new ListUtil();
                listUtil.setListViewHeightBasedOnChildren(recommendMenuList);

                recommendMenuList.setOnItemClickListener(new MyMenuItemClickListener());
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    @OnClick({R.id.menu_addCollection, R.id.menu_addCart, R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_addCollection:
                getMenuCollectionByUser();
                break;
            case R.id.menu_addCart:
                FinalHttp http_getItem = new FinalHttp();
                final String url_getItem = App.URL + "item";
                http_getItem.get(url_getItem, new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String t) {
                        JsonParser parser = new JsonParser();
                        JsonElement json_items = parser.parse(t)
                                .getAsJsonObject().get("_embedded").getAsJsonObject()
                                .getAsJsonArray("items");
                        items = Global.getGson()
                                .fromJson(json_items, new TypeToken<List<Item>>() {
                                }.getType());

                        String[] mainStuffs = menu.getMainStuff().split("；");
                        for (String mainStuff : mainStuffs) {
                            String stuff = mainStuff.substring(2, mainStuff.length());
                            for (Item item : items) {
                                if (stuff.equals(item.getName())) {
                                    addCart(String.valueOf(Bus.bean.getUser().getId()), String.valueOf(item.getId()), "1");
                                    same = true;
                                }
                            }
                        }
                        if (!same) {
                            Toast.makeText(MenuDetailActivity.this, "商城中没有您所需的商品", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t, String strMsg) {
                        super.onFailure(t, strMsg);
                    }
                });
                break;
            case R.id.img_back:

                finish();
                break;
        }
    }

    public void addCart(String uid, String itemId, String buyNum) {
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("uid", uid);
        params.put("itemId", itemId);
        params.put("num", buyNum);
        finalHttp.put(App.URL + "cart/search/add", params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(MenuDetailActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                super.onSuccess(s);
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    //查询菜谱是否已经存在收藏中

    private void getMenuCollectionByUser() {
        FinalHttp getCollectionByUser_http = new FinalHttp();
        String getCollectionByUser_url = App.URL + "menu_collection/" + 1 + "/";
        getCollectionByUser_http.get(getCollectionByUser_url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                JsonParser parser = new JsonParser();
                JsonElement json = parser.parse(s);
                menuCollections = Global.getGson()
                        .fromJson(json, new TypeToken<List<MenuCollection>>() {
                        }.getType());
                for (MenuCollection menuCollection : menuCollections) {
                    if (menu.getId() == menuCollection.getMenu().getId()) {
                        Log.i(TAG, "onSuccess: " + menu.getId());
                        Toast.makeText(context, "该商品已经在收藏啦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                addMenuCollection();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    //添加到菜谱收藏
    private void addMenuCollection() {
        FinalHttp addMenuCollectionHttp = new FinalHttp();
        String url = App.URL + "menu_collection/add?userId=" + 1 + "&menuId=" + menu.getId();
        addMenuCollectionHttp.post(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Toast.makeText(context, "添加失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyMenuItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Menu menu = menus.get(position);
            Bus.bean.setMenu(menu);
            Intent intent = new Intent();
            intent.setClass(context, MenuDetailActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
