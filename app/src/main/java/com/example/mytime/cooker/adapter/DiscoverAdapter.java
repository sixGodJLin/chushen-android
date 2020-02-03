package com.example.mytime.cooker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.Discover;
import com.example.mytime.cooker.bean.User;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.TimeUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mr.Lin on 2018/3/10
 */

public class DiscoverAdapter extends BaseAdapter {
    private static final String TAG = "DiscoverAdapter";
    private List<Discover> discovers;
    private Context context;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public DiscoverAdapter(Context context, List<Discover> discovers) {
        this.context = context;
        this.discovers = discovers;
    }


    @Override
    public int getCount() {
        return discovers.size();
    }

    @Override
    public Object getItem(int position) {
        return discovers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return discovers.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.discover_item, null);
            holder = new ViewHolder();
            holder.imgHead = convertView.findViewById(R.id.img_head);
            holder.username = convertView.findViewById(R.id.username);
            holder.time = convertView.findViewById(R.id.discover_createTime);
            holder.content = convertView.findViewById(R.id.content);
            holder.tableLayout = convertView.findViewById(R.id.tableLayout);
            holder.imageView1 = convertView.findViewById(R.id.imageView1);
            holder.imageView2 = convertView.findViewById(R.id.imageView2);
            holder.imageView3 = convertView.findViewById(R.id.imageView3);
            holder.voteNum = convertView.findViewById(R.id.vote_num);
            holder.commentNum = convertView.findViewById(R.id.comment_num);
            holder.good = convertView.findViewById(R.id.upVote);
            holder.comment = convertView.findViewById(R.id.comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Discover discover = discovers.get(position);

        Log.i(TAG, "getView: " + discover);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.drawable.head));
        circleDrawable.getPaint().setAntiAlias(true);
        circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
        holder.imgHead.setImageDrawable(circleDrawable);

        holder.username.setText(discover.getUser().getNickname());
        holder.time.setText(TimeUtil.DateToString(discover.getCreateTime()));
        holder.content.setText(discover.getContent());
        holder.voteNum.setText(String.valueOf(discover.getUpvote()));
        holder.commentNum.setText(String.valueOf(discover.getCommentNum()));

        Glide.with(context)
                .load(discover.getUser().getHeadImgUrl())
                .into(holder.imgHead);

        if (discover.getImageUrls() != null) {
            String discover_url = discover.getImageUrls().startsWith("http")
                    ? discover.getImageUrls()
                    : App.URL + discover.getImageUrls();

            Glide.with(context)
                    .load(discover_url)
                    .into(holder.imageView1);
        }

        holder.imageView2.setVisibility(View.GONE);
        holder.imageView3.setVisibility(View.GONE);

        final ViewHolder finalHolder = holder;
        holder.good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vpNum = discover.getUpvote() + 1;
                finalHolder.voteNum.setText(String.valueOf(vpNum));

                Discover upDis = new Discover();
                upDis.setId(discover.getId());
                upDis.setUpvote(vpNum);

                String json = new Gson().toJson(upDis);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                String url = App.URL + "discover";
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .patch(body)
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
                    }
                });
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView imgHead;
        private TextView username;
        private TextView time;
        private TextView content;
        private TableLayout tableLayout;
        private ImageView imageView1;
        private ImageView imageView2;
        private ImageView imageView3;
        private TextView voteNum;
        private TextView commentNum;
        private LinearLayout good;
        private LinearLayout comment;
    }
}
