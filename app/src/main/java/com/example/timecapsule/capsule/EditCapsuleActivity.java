package com.example.timecapsule.capsule;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.example.timecapsule.BaseActivity;
import com.example.timecapsule.R;
import com.example.timecapsule.adapter.FullyGridLayoutManager;
import com.example.timecapsule.adapter.GridImageAdapter;
import com.example.timecapsule.bean.CapsuleBean;
import com.example.timecapsule.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class EditCapsuleActivity extends BaseActivity {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rl_date)
    RelativeLayout rlDate;
    @BindView(R.id.bt_save)
    TextView btSave;

    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private long mOpenTime=-1;
    private String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_capsule);
        ButterKnife.bind(this);
        account = getIntent().getStringExtra("account");
        initWidget();
    }


    private void initWidget() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            PictureSelector.create(EditCapsuleActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            PictureSelector.create(EditCapsuleActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            PictureSelector.create(EditCapsuleActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @Override
        public void onAddPicClick() {
            RxPermissions rxPermission = new RxPermissions(EditCapsuleActivity.this);
            rxPermission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                PictureSelector.create(EditCapsuleActivity.this)
                                        .openGallery(PictureMimeType.ofImage())
                                        .maxSelectNum(9)
                                        .minSelectNum(1)
                                        .selectionMode(PictureConfig.MULTIPLE)
                                        .forResult(PictureConfig.CHOOSE_REQUEST);
                            } else {
                                Toast.makeText(EditCapsuleActivity.this, "Authorization needed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    images = PictureSelector.obtainMultipleResult(data);
                    Log.e("123", "!!" + images.size());
                    selectList.addAll(images);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @OnClick(R.id.rl_date)
    public void onClick() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvDate.setText(DateUtils.getStrDate(date));
                mOpenTime = date.getTime();
            }
        })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isCenterLabel(false)
                .build();

        pvTime.setDate(Calendar.getInstance());
        pvTime.show();

    }

    @OnClick(R.id.bt_save)
    public void onClickSave() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if (title.equals("")) {
            Toast.makeText(EditCapsuleActivity.this,"Input Title",Toast.LENGTH_SHORT).show();
            return;
        }else if (content.equals("")) {
            Toast.makeText(EditCapsuleActivity.this,"Inpute Message",Toast.LENGTH_SHORT).show();
            return;
        }else if (mOpenTime==-1) {
            Toast.makeText(EditCapsuleActivity.this,"Time to Open",Toast.LENGTH_SHORT).show();
            return;
        }
        String image = new Gson().toJson(selectList);
        CapsuleBean bean = new CapsuleBean();
        bean.setTitle(title);
        bean.setContent(content);
        bean.setPath(image);
        bean.setAccount(account);
        bean.setOpenTime(mOpenTime);
        bean.save();
        setResult(RESULT_OK);
        finish();
    }
}