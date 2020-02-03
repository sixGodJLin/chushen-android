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
import android.widget.ImageView;
import android.widget.ListView;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.MenuAdapter;
import com.example.mytime.cooker.bean.Menu;
import com.example.mytime.cooker.ui.MenuDetailActivity;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.Global;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    @Bind(R.id.img_cart)
    ImageView img_Cart;
    @Bind(R.id.list_view)
    ListView listView;
    private View homeView = null;

    List<Menu> menus;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.home_fragment, null);
        ButterKnife.bind(this, homeView);
        img_Cart.setVisibility(View.GONE);

        initMenuList();
        return homeView;
    }

    //初始化菜谱列表数据
    private void initMenuList() {
        FinalHttp http = new FinalHttp();
        String url = App.URL + "menu";
        http.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json_menus = parser.parse(t)
                        .getAsJsonObject().get("_embedded").getAsJsonObject()
                        .getAsJsonArray("menus");
                menus = Global.getGson()
                        .fromJson(json_menus, new TypeToken<List<Menu>>() {
                        }.getType());

                MenuAdapter adapter = new MenuAdapter(getActivity(), menus);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new MyMenuItemClickListener());
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class MyMenuItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Menu menu = menus.get(position);
            Bus.bean.setMenu(menu);
            Intent intent = new Intent();
            intent.setClass(getActivity(), MenuDetailActivity.class);
            startActivity(intent);
        }
    }
}
