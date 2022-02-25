package com.example.timecapsule.capsule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.example.timecapsule.BaseActivity;
import com.example.timecapsule.R;
import com.example.timecapsule.adapter.FullyGridLayoutManager;
import com.example.timecapsule.adapter.GridImagePreviewAdapter;
import com.example.timecapsule.adapter.MyCommentRecyclerAdapter;
import com.example.timecapsule.bean.CapsuleBean;
import com.example.timecapsule.bean.CommentBean;
import com.example.timecapsule.config.SettingConstant;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CapsuleActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.recycler_comment)
    RecyclerView recyclerComment;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.bt_comment)
    TextView btComment;

    private GridImagePreviewAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private CapsuleBean mBean;
    private SharedPreferences mSettings;
    private String account;
    private List<CommentBean> mList = new ArrayList<>();
    private MyCommentRecyclerAdapter myCommentRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule);
        ButterKnife.bind(this);
        mBean = (CapsuleBean) getIntent().getSerializableExtra("item");
        mSettings = getSharedPreferences(SettingConstant.SETTING_NAME, MODE_PRIVATE);
        account = mSettings.getString(SettingConstant.ACCOUNT, SettingConstant.Account_Default);

        tvContent.setText(mBean.getContent());
        tvTitle.setText(mBean.getTitle());

        if (mBean.getPath() != null && !mBean.getPath().equals("")) {
            List<LocalMedia> list = new Gson().fromJson(mBean.getPath(), new TypeToken<List<LocalMedia>>() {
            }.getType());
            Log.e("1231", "123");
            selectList.addAll(list);
        }

        mList.addAll(LitePal.where("capseleId = ?", mBean.getId() + "").find(CommentBean.class));
        recyclerComment.setLayoutManager(new LinearLayoutManager(this));
        myCommentRecyclerAdapter = new MyCommentRecyclerAdapter(mList,this);
        recyclerComment.setAdapter(myCommentRecyclerAdapter);

        initWidget();
    }


    private void initWidget() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new GridImagePreviewAdapter(this);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImagePreviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            PictureSelector.create(CapsuleActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            PictureSelector.create(CapsuleActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            PictureSelector.create(CapsuleActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    @OnClick(R.id.bt_comment)
    public void onClick() {
        String input = etComment.getText().toString();
        if (input.equals("")) {
            Toast.makeText(CapsuleActivity.this,"Input your Comment",Toast.LENGTH_SHORT).show();
            return;
        }

        CommentBean bean = new CommentBean(input,System.currentTimeMillis(),account);
        bean.setCapseleId(mBean.getId());
        bean.save();
        mList.add(bean);
        myCommentRecyclerAdapter.notifyDataSetChanged();
        Toast.makeText(CapsuleActivity.this,"Successful",Toast.LENGTH_SHORT).show();
        etComment.setText("");
    }
}