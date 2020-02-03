package com.example.mytime.cooker.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mytime.cooker.R;
import com.example.mytime.cooker.adapter.CommentAdapter;
import com.example.mytime.cooker.bean.ItemComment;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Global;
import com.example.mytime.cooker.utils.ListUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends Activity {

    private static final String TAG =  "CommentActivity";
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.title_text)
    TextView title_text;
    @Bind(R.id.comment_item_username)
    TextView username;
    @Bind(R.id.img_comment)
    ImageView img_comment;
    @Bind(R.id.comment_item_content)
    TextView content;
    @Bind(R.id.comment_comment_list)
    ListView comment_comment_list;

    private Context context;
    private ItemComment comment;
    private List<ItemComment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        this.context = getApplicationContext();
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        title_text.setText("全部评论");
        comment = Bus.bean.getComment();
        username.setText(comment.getUser().getUsername());
        content.setText(comment.getContent());

        getCommentFromUrl();
    }

    private void getCommentFromUrl() {
        FinalHttp http = new FinalHttp();
        String get_comment_coment_url = App.URL + "item_comment/search/getCommentComment?commentId=" + comment.getId();
        http.get(get_comment_coment_url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                JsonParser parser = new JsonParser();
                JsonElement json_comment_comment = parser.parse(t)
                        .getAsJsonObject().get("_embedded")
                        .getAsJsonObject().getAsJsonArray("itemComments");

                comments = Global.getGson()
                        .fromJson(json_comment_comment,new TypeToken<List<ItemComment>>() {
                        }.getType());

                CommentAdapter adapter = new CommentAdapter(context,comments);
                comment_comment_list.setAdapter(adapter);

                ListUtil listUtil = new ListUtil();
                listUtil.setListViewHeightBasedOnChildren(comment_comment_list);

            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        });
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
