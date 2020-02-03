package com.example.mytime.cooker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.CommentAdapter;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.bean.ItemCollection;
import com.example.mytime.cooker.bean.ItemComment;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.Global;
import com.example.mytime.cooker.utils.ListUtil;
import com.example.mytime.cooker.utils.TimeUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsdetailActivity extends AppCompatActivity {

    private static final String TAG = "GoodsdetailActivity";
    @Bind(R.id.img_back)
    ImageView img_Back;
    @Bind(R.id.title_text)
    TextView title_Text;
    @Bind(R.id.img_collect)
    ImageView img_Collect;
    @Bind(R.id.good_detail_img)
    ImageView good_Detail_Img;
    @Bind(R.id.good_detail_title)
    TextView good_Detail_Title;
    @Bind(R.id.good_detail_price)
    TextView good_Detail_Price;
    @Bind(R.id.good_detail_sellnum)
    TextView good_Detail_Sellnum;
    @Bind(R.id.good_detail_shop)
    TextView good_Detail_Shop;
    @Bind(R.id.good_detail_time)
    TextView good_Detail_Time;
    @Bind(R.id.good_detail_comment_num)
    TextView good_detail_comment_num;
    @Bind(R.id.item_comment_list)
    ListView itemc_comment_list;
    @Bind(R.id.add_collection_layout)
    LinearLayout addCollectionLayout;
    @Bind(R.id.add_cart_layout)
    LinearLayout addCartLayout;
    @Bind(R.id.buy_layout)
    LinearLayout buyLayout;
    @Bind(R.id.item_buy_num)
    EditText itemBuyNum;

    private Item item;
    private Context context;
    private List<ItemComment> comments;
    private Map<Integer, ItemComment> commentMap = new HashMap<>();
    private List<ItemCollection> itemCollections;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        this.context = getApplicationContext();

        item = Bus.bean.getItem();
        initData();
    }

    private void initData() {
        title_Text.setText("商品详情");

        if (item.getImgUrlJson() != null) {
            String item_url = item.getImgUrlJson().startsWith("http")
                    ? item.getImgUrlJson()
                    : App.URL + item.getImgUrlJson();
            Glide.with(context)
                    .load(item_url)
                    .into(good_Detail_Img);
        }
        good_Detail_Title.setText(item.getName());
        good_Detail_Price.setText("￥" + item.getPrice());
        good_Detail_Shop.setText(item.getShop().getName());
        good_Detail_Sellnum.setText("销量：" + String.valueOf(item.getSellNum()));
        good_Detail_Time.setText(TimeUtil.formatTimestamp(item.getTime()));
        good_detail_comment_num.setText("全部评论   (" + item.getCommentNum() + "条)");

        FinalHttp getComment_http = new FinalHttp();
        String getComment_url = App.URL + "item_comment/search/getComment?itemid=" + item.getId();

        getComment_http.get(getComment_url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                Log.i(TAG, "onSuccess: " + t);
                JsonParser parser = new JsonParser();
                JsonElement json_comments = parser.parse(t)
                        .getAsJsonObject().get("_embedded").getAsJsonObject()
                        .getAsJsonArray("itemComments");

                comments = Global.getGson()
                        .fromJson(json_comments, new TypeToken<List<ItemComment>>() {
                        }.getType());

                for (ItemComment comment : comments) {
                    commentMap.put(comment.getId(), comment);
                }

                CommentAdapter adapter = new CommentAdapter(context, comments);
                itemc_comment_list.setAdapter(adapter);

                ListUtil listUtil = new ListUtil();
                listUtil.setListViewHeightBasedOnChildren(itemc_comment_list);

                itemc_comment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ItemComment comment = commentMap.get(comments.get(position).getId());
                        Bus.bean.setComment(comment);
                        Intent intent = new Intent();
                        intent.setClass(GoodsdetailActivity.this, CommentActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                Log.i(TAG, "onSuccess: " + "123456");
                super.onFailure(t, strMsg);
            }
        });
    }

    @OnClick({R.id.img_back, R.id.add_collection_layout, R.id.add_cart_layout, R.id.buy_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.add_collection_layout:
                getCollectionByUser();
                break;
            case R.id.add_cart_layout:
                if (TextUtils.isEmpty(itemBuyNum.getText())){
                    Toast.makeText(context , "请填写数量" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(itemBuyNum.getText().toString()) > item.getStoreNum()){
                    Toast.makeText(context , "库存有限，请修改数量" , Toast.LENGTH_SHORT).show();
                    return;
                }
                addCart("" + Bus.bean.getUser().getId(), "" + item.getId(), itemBuyNum.getText().toString());
                break;
            case R.id.buy_layout:
                Bus.bean.setItem(item);
                if (TextUtils.isEmpty(itemBuyNum.getText())){
                    Toast.makeText(context , "请填写数量" , Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Bus.bean.setBuyNum(itemBuyNum.getText().toString());
                }
                Intent intent = new Intent();
                intent.putExtra("single1_or_cart2",1);
                intent.setClass(context , OrderDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void addCart(String uid, String itemId, String buyNum) {
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("uid", uid);
        params.put("itemId", itemId);
        params.put("num",buyNum);
        finalHttp.put(App.URL + "cart/search/add", params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(GoodsdetailActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                super.onSuccess(s);
            }
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    //查询商品是否已经存在收藏中
    private void getCollectionByUser() {
        FinalHttp getCollectionByUser_http = new FinalHttp();
        String getCollectionByUser_url = App.URL + "item_collection/" + 1 + "/";
        getCollectionByUser_http.get(getCollectionByUser_url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                JsonParser parser = new JsonParser();
                JsonElement json = parser.parse(s);
                itemCollections = Global.getGson()
                        .fromJson(json, new TypeToken<List<ItemCollection>>() {
                        }.getType());
                for (ItemCollection itemCollection : itemCollections) {
                    if (item.getId() == itemCollection.getItem().getId()) {
                        Toast.makeText(context, "该商品已经在收藏啦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                addCollection();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    //添加到收藏
    private void addCollection() {
        FinalHttp addCollection_http = new FinalHttp();
        String url = App.URL + "item_collection/add?userId=" + 1 + "&itemId=" + item.getId();
        addCollection_http.post(url, new AjaxCallBack<String>() {
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
}
