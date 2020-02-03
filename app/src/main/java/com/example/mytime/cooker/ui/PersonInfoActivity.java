package com.example.mytime.cooker.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytime.cooker.R;
import com.example.mytime.cooker.bean.User;
import com.example.mytime.cooker.utils.App;
import com.example.mytime.cooker.utils.Bus;
import com.example.mytime.cooker.utils.ImageUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonInfoActivity extends Activity {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.modify_head_img)
    ImageView modifyHeadImg;
    @Bind(R.id.modify_nickName)
    EditText modifyNickName;
    @Bind(R.id.modify_tel)
    EditText modifyTel;
    @Bind(R.id.btn_save)
    Button btnSave;

    private Context context;
    User user = Bus.bean.getUser();
    // 照片所在的Uri地址
    private Uri imageUri;
    private String headImageUrl;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    Bus.bean.setUser(user);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        this.context = getApplicationContext();
        fillPersonInfo();
    }

    private void fillPersonInfo() {
        FinalHttp http = new FinalHttp();
        String url = App.URL + "user/search/login?username=" + user.getUsername()
                + "&password=" + user.getPassword() + "&projection=inlineUser";
        http.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                User user = new Gson().fromJson(t,
                        new TypeToken<User>() {
                        }.getType());

                if (user.getHeadImgUrl() != null) {
                    String headImg_url = user.getHeadImgUrl().startsWith("http")
                            ? user.getHeadImgUrl()
                            : App.URL + user.getHeadImgUrl();

                    Glide.with(context)
                            .load(headImg_url)
                            .into(modifyHeadImg);
                }

                if (user.getNickname() != null) {
                    modifyNickName.setText(user.getNickname());
                }
                if (user.getTel() != null) {
                    modifyTel.setText(user.getTel());
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Log.i(TAG, "onFailure: -----" + t);
                Toast.makeText(getApplication(), "用户名或密码错误", Toast.LENGTH_LONG).show();
            }
        });

    }

    @OnClick({R.id.img_back, R.id.btn_save, R.id.modify_head_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.modify_head_img:
                if (ContextCompat.checkSelfPermission(PersonInfoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
                    // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
                    // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
                    ActivityCompat.requestPermissions(PersonInfoActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ReportActivity.CAMERA_REQUEST_CODE);

                } else { //权限已经被授予，在这里直接写要执行的相应方法即可
                    choosePhoto();
                }
                break;
            case R.id.btn_save:
                updateUserInfo();
                break;
            default:
                break;
        }
    }

    private void updateUserInfo() {
        if (headImageUrl != null) {
            user.setHeadImgUrl(headImageUrl);
        }

        if (!modifyNickName.getText().toString().isEmpty()) {
            String nickName = modifyNickName.getText().toString();
            user.setNickname(nickName);
        }

        if (!modifyTel.getText().toString().isEmpty()) {
            String tel = modifyTel.getText().toString();
            user.setTel(tel);
        }

        String json = new Gson().toJson(user);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = App.URL + "/user";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        //new call
        Call call = client.newCall(request);
        //请求调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() >= 200 && response.code() < 300) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
        startActivityForResult(intentToPickPic, ReportActivity.GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ReportActivity.RESULT_OK) {
            switch (requestCode) {
                case ReportActivity.CAMERA_REQUEST_CODE: {
                    // 获得图片
                    try {
                        //该uri就是照片文件夹对应的uri
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        // 给相应的ImageView设置图片 未裁剪
                        modifyHeadImg.setImageBitmap(bit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case ReportActivity.GALLERY_REQUEST_CODE: {
                    // 获取图片
                    try {
                        //该uri是上一个Activity返回的
                        imageUri = data.getData();
                        System.out.println("--- > " + imageUri);
                        if (imageUri != null) {
                            Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                            Log.i("bit", String.valueOf(bit));
                            modifyHeadImg.setImageBitmap(bit);
                            final File imageFile = ImageUtil.getFileByUri(context, imageUri);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    toUploadFile(imageFile,
                                            App.URL + "upload",
                                            new HashMap<String, String>());
                                }
                            }).start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String TAG = "ReportActivity";

    private void toUploadFile(File file, String RequestURL,
                              Map<String, String> param) {
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(2000);
            conn.setConnectTimeout(2000);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", "utf-8"); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            /*
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb;
            String params;

            /*
             * 以下是用于上传参数
             */
            if (param != null && param.size() > 0) {
                for (String s : param.keySet()) {
                    sb = new StringBuffer();
                    String value = param.get(s);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"")
                            .append(s).append("\"")
                            .append(LINE_END)
                            .append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    Log.i(TAG, s + "=" + params + "##");
                    dos.write(params.getBytes());
                }
            }

            sb = new StringBuffer();
            /*
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition:form-data; name=\"")
                    .append("file").append("\"; filename=\"")
                    .append(file.getName()).append("\"").append(LINE_END);
            sb.append("Content-Type:image/png" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append(LINE_END);
            params = sb.toString();

            Log.i(TAG, file.getName() + "=" + params + "##");
            dos.write(params.getBytes());
            /*上传文件*/
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
            }
            is.close();

            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();

            /*
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            if (res == 200) {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuilder sb1 = new StringBuilder();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                headImageUrl = sb1.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
