package com.example.mytime.cooker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.MyMallAdapter;
import com.example.mytime.cooker.adapter.RecommendAdapter;
import com.example.mytime.cooker.bean.DayRecommend;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.ui.FenleiActivity;
import com.example.mytime.cooker.ui.GoodsdetailActivity;
import com.example.mytime.cooker.ui.CartActivity;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Global;
import com.example.mytime.cooker.utils.ListUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MallFragment extends Fragment {
    private static final String TAG = "MallFragment";
    @Bind(R.id.bar_edit_search)
    EditText bar_EditSearch;
    @Bind(R.id.mall_good_listview)
    ListView mallGoodListview;
    @Bind(R.id.recommend_gridview)
    GridView recommend_gridview;
    @Bind(R.id.mall_fragment_fresh_layout)
    LinearLayout fresh_layout;
    @Bind(R.id.mall_fragment_fruit_layout)
    LinearLayout fruit_layout;
    @Bind(R.id.mall_fragment_vegetable_layout)
    LinearLayout vegetable_layout;
    @Bind(R.id.mall_fragment_fenlei_layout)
    LinearLayout fenlei_layout;
    @Bind(R.id.mall_fragment_nut_layout)
    LinearLayout nut_layout;
    @Bind(R.id.more_fenlei_layout)
    LinearLayout more_fenlei_Layout;
    @Bind(R.id.img_cart)
    ImageView img_cart;

    private View mallView = null;
    List<Item> items;
    private Map<Integer, Item> itemMap = new HashMap<>();
    private List<DayRecommend> recommends;
    private int type;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mallView = inflater.inflate(R.layout.mall_fragment, null);
        ButterKnife.bind(this, mallView);

        more_fenlei_Layout.setVisibility(View.GONE);

        //添加商品列表
        initItemList();
        //添加精品列表
        initRecommend();

        return mallView;
    }

    @OnClick({R.id.mall_fragment_fresh_layout, R.id.mall_fragment_fruit_layout, R.id.mall_fragment_vegetable_layout, R.id.mall_fragment_fenlei_layout, R.id.mall_fragment_nut_layout, R.id.img_cart})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mall_fragment_fresh_layout:
                type = 1;
                Bus.bean.setType(type);
                intent = new Intent();
                intent.setClass(getActivity(), FenleiActivity.class);
                startActivity(intent);
                break;
            case R.id.mall_fragment_fruit_layout:
                type = 2;
                Bus.bean.setType(type);
                intent = new Intent();
                intent.setClass(getActivity(), FenleiActivity.class);
                startActivity(intent);
                break;
            case R.id.mall_fragment_vegetable_layout:
                type = 3;
                Bus.bean.setType(type);
                intent = new Intent();
                intent.setClass(getActivity(), FenleiActivity.class);
                startActivity(intent);
                break;
            case R.id.mall_fragment_nut_layout:
                type = 4;
                Bus.bean.setType(type);
                intent = new Intent();
                intent.setClass(getActivity(), FenleiActivity.class);
                startActivity(intent);
                break;
            case R.id.mall_fragment_fenlei_layout:
                if (more_fenlei_Layout.getVisibility() == View.GONE)
                    more_fenlei_Layout.setVisibility(View.VISIBLE);
                else
                    more_fenlei_Layout.setVisibility(View.GONE);
                break;
            case R.id.img_cart:
                intent = new Intent();
                intent.setClass(getActivity(), CartActivity.class);
                startActivity(intent);
                break;
        }
    }

    //商品列表添加的实现
    private void initItemList() {
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

                Log.i(TAG, "onSuccess: MallFragment_items" + items);

                for (Item item : items) {
                    itemMap.put(item.getId(), item);
                }

                /**
                 * 遍历列表
                 * 如果商品列表中的商品的id与推荐列表中商品的id相同
                 * 则将这些商品添加到一个列表中
                 * 在商品列表中删除此列表的数据
                 */
                if (Bus.bean.getIds() != null) {
                    for (Integer id : Bus.bean.getIds()) {
                        List<Item> temp = new ArrayList<Item>();
                        for (Item item : items) {
                            if (item.getId() == id) {
                                temp.add(item);
                            }
                        }
                        items.removeAll(temp);
                    }
                }
                //使用适配器添加ListView列表
                MyMallAdapter item_adapter = new MyMallAdapter(getActivity(), items);
                mallGoodListview.setAdapter(item_adapter);
                //设置ListView中Item的点击事件
                mallGoodListview.setOnItemClickListener(new MyItemClickListener());
                //设置ListView的高度
                ListUtil listUtil = new ListUtil();
                listUtil.setListViewHeightBasedOnChildren(mallGoodListview);
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    //精品推荐列表实现
    private void initRecommend() {
        FinalHttp http_getRecommend = new FinalHttp();
        final String url_getRecommend = App.URL + "dayRecommend";
        http_getRecommend.get(url_getRecommend, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json_recommend = parser.parse(t)
                        .getAsJsonObject().get("_embedded").getAsJsonObject()
                        .getAsJsonArray("dayRecommends");
                recommends = Global.getGson()
                        .fromJson(json_recommend, new TypeToken<List<DayRecommend>>() {
                        }.getType());

                Log.i(TAG, "onSuccess: MallFragment_recommend" + recommends);

                //将推荐列表中的商品id保存
                Integer[] ids = new Integer[recommends.size()];
                for (int i = 0; i < recommends.size(); i++) {
                    ids[i] = recommends.get(i).getItem().getId();
                }

                Bus.bean.setIds(ids);

                //使用适配器添加GridView列表
                RecommendAdapter recommend_adapter = new RecommendAdapter(getActivity(), recommends);
                recommend_gridview.setAdapter(recommend_adapter);
                //设置GridView中Item的点击事件
                recommend_gridview.setOnItemClickListener(new MyRecommendClickListener());
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
            intent.setClass(getActivity(), GoodsdetailActivity.class);
            startActivity(intent);
        }
    }

    //推荐列表中Item的点击事件 -->  进入到该商品的详情页面
    private class MyRecommendClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item item = itemMap.get(recommends.get(position).getItem().getId());
            Bus.bean.setItem(item);
            Intent intent = new Intent();
            intent.setClass(getActivity(), GoodsdetailActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
