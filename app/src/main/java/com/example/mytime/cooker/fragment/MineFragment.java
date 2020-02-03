package com.example.mytime.cooker.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.User;
import com.example.mytime.cooker.ui.AddressActivity;
import com.example.mytime.cooker.ui.ItemCollectionActivity;
import com.example.mytime.cooker.ui.OrderActivity;
import com.example.mytime.cooker.ui.PersonInfoActivity;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends Fragment {
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.mine_address)
    TextView mine_address;
    @Bind(R.id.mine_item)
    TextView mineItem;
    @Bind(R.id.mine_orderItem)
    TextView mineOrderItem;
    @Bind(R.id.mine_menu)
    TextView mineMenu;
    @Bind(R.id.report)
    TextView report;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.login_username)
    TextView loginUsername;
    @Bind(R.id.img_head)
    ImageView imgHead;

    private Context context;
    private View mineView = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mineView = inflater.inflate(R.layout.mine_fragment, null);
        ButterKnife.bind(this, mineView);
        imgBack.setVisibility(View.GONE);
        report.setText("编辑");
        initData();
        this.context = getActivity();
        return mineView;
    }

    private void initData() {
        User user = Bus.bean.getUser();
        loginUsername.setText(user.getNickname());

        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.head);
        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.head));
        circleDrawable.getPaint().setAntiAlias(true);
        circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
        imgHead.setImageDrawable(circleDrawable);

        Glide.with(getActivity())
                .load(App.URL + user.getHeadImgUrl())
                .into(imgHead);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.report, R.id.mine_address, R.id.mine_item, R.id.mine_menu, R.id.mine_orderItem})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.report:
                Intent intent1 = new Intent(context, PersonInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.mine_address:
                intent = new Intent();
                intent.setClass(context, AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_item:
                intent = new Intent();
                intent.setClass(context, ItemCollectionActivity.class);
                intent.putExtra("collectionType", "item");
                startActivity(intent);
                break;
            case R.id.mine_menu:
                intent = new Intent();
                intent.setClass(context, ItemCollectionActivity.class);
                intent.putExtra("collectionType", "menu");
                startActivity(intent);
                break;
            case R.id.mine_orderItem:
                intent = new Intent();
                intent.setClass(context, OrderActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
