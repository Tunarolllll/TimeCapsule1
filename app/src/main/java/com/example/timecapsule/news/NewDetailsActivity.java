package com.example.timecapsule.news;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.example.timecapsule.BaseActivity;
import com.example.timecapsule.R;
import com.example.timecapsule.bean.NewsList;
import com.example.timecapsule.config.SettingConstant;
import com.example.timecapsule.http.DataBean;
import com.example.timecapsule.http.HttpReq;
import com.example.timecapsule.http.HttpResponse;
import com.example.timecapsule.http.RetrofitManager;
import com.example.timecapsule.utils.HtmlFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewDetailsActivity extends BaseActivity {


    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    private Dialog mBuildBean;

    private NewsList mBean;
    private SharedPreferences mSettings;
    private String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);
        ButterKnife.bind(this);
        mSettings = getSharedPreferences(SettingConstant.SETTING_NAME, MODE_PRIVATE);
        account = mSettings.getString(SettingConstant.ACCOUNT, SettingConstant.Account_Default);


        WebSettings webSettings = webView.getSettings();
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        Intent intent = getIntent();
        mBean = (NewsList) intent.getSerializableExtra("item");
        getNewsDetails(mBean.getUniquekey());
    }


    private void getNewsDetails(String key) {
        mBuildBean = DialogUIUtils.showLoading(this, "Loading...",
                true, false, false, true).show();

        RetrofitManager.getInstance().getNewsDetails(key, HttpReq.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResponse<DataBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull HttpResponse<DataBean> listHttpResponse) {
                        Log.e("!!12!", listHttpResponse.toString());
                        if (listHttpResponse.getErrorCode() == 0) {
                            if (listHttpResponse.getResults().getDetail() == null) {
                                Toast.makeText(NewDetailsActivity.this,listHttpResponse.getMessage(),Toast.LENGTH_SHORT).show();
                            } else {
                                tvTitle.setText(listHttpResponse.getResults().getDetail().getTitle());
                                tvInfo.setText(listHttpResponse.getResults().getDetail().getDate().substring(5, 16) + "   " + listHttpResponse.getResults().getDetail().getAuthor_name());
                                webView.loadDataWithBaseURL(null, HtmlFormat.getNewContent(listHttpResponse.getResults().getContent()), "text/html", "UTF-8", null);
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("qe2", e.toString());
                        Toast.makeText(NewDetailsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                        DialogUIUtils.dismiss(mBuildBean);

                    }

                    @Override
                    public void onComplete() {
                        DialogUIUtils.dismiss(mBuildBean);

                    }
                });
    }
}