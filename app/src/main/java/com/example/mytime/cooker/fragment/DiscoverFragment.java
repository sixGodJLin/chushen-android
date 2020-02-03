package com.example.mytime.cooker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.DiscoverAdapter;
import com.example.mytime.cooker.bean.Discover;
import com.example.mytime.cooker.ui.ReportActivity;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Global;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscoverFragment extends Fragment {
    @Bind(R.id.discover_list)
    ListView discoverList;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.report)
    TextView report;
    @Bind(R.id.img_back)
    ImageView imgBack;

    private View discoverView = null;

    List<Discover> discovers;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        discoverView = inflater.inflate(R.layout.discover_fragment, null);
        ButterKnife.bind(this, discoverView);
        imgBack.setVisibility(View.GONE);
        titleText.setText("发现");

        initDiscoverList();

        return discoverView;
    }

    private void initDiscoverList() {
        FinalHttp http = new FinalHttp();
        final String url = App.URL + "discover";
        http.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json_discover = parser.parse(t);

                discovers = Global.getGson()
                        .fromJson(json_discover, new TypeToken<List<Discover>>() {
                        }.getType());

                DiscoverAdapter adapter = new DiscoverAdapter(getActivity(), discovers);
                discoverList.setAdapter(adapter);
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

    @OnClick(R.id.report)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        initDiscoverList();
    }
}
