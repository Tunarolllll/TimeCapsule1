package com.example.timecapsule.mine;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.dou361.dialogui.listener.DialogUIListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.example.timecapsule.BaseActivity;
import com.example.timecapsule.R;
import com.example.timecapsule.bean.UserBean;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class EditInfoActivity extends BaseActivity {

    @BindView(R.id.h_head)
    RoundedImageView hHead;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.bt_save)
    TextView btSave;
    @BindView(R.id.bt_password)
    TextView btPassword;

    private UserBean mUser;
    private List<LocalMedia> selectList = new ArrayList<>();
    private Dialog mBuildBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mUser = (UserBean) intent.getSerializableExtra("user");
        if (mUser.getImage() == null || mUser.getImage().equals("")) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_head);
            hHead.setImageBitmap(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(mUser.getImage());
            hHead.setImageBitmap(bitmap);
        }
        tvName.setText(mUser.getName() == null ? "Not set" : mUser.getName());
        tvSex.setText(mUser.getSex() == 0 ? "Not Set" : mUser.getSex() == 1 ? "Male" : "Female");


    }

    @OnClick({R.id.rl_head, R.id.rl_name, R.id.rl_sex,  R.id.bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head:
                RxPermissions rxPermission = new RxPermissions(EditInfoActivity.this);
                rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) {
                                if (permission.granted) {
                                    PictureSelector.create(EditInfoActivity.this)
                                            .openGallery(PictureMimeType.ofAll())
                                            .maxSelectNum(1)
                                            .minSelectNum(1)
                                            .selectionMode(PictureConfig.MULTIPLE)
                                            .forResult(PictureConfig.CHOOSE_REQUEST);
                                } else {
                                    Toast.makeText(EditInfoActivity.this, "Please Authorize", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
            case R.id.rl_name:
                final EditText etName = new EditText(this);
                if (mUser != null)
                    etName.setText(mUser.getName());
                new AlertDialog.Builder(this).setTitle("Your Name")
                        .setView(etName)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mUser.setName(etName.getText().toString());
                                tvName.setText(etName.getText().toString());
                            }
                        }).show();
                break;
            case R.id.rl_sex:
                String[] words2 = new String[]{"Male", "Female"};
                int defult = 0;
                if (mUser.getSex() == 2) {
                    defult = 1;
                }
                DialogUIUtils.showSingleChoose(this, "Choose Gender", defult, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        mUser.setSex(position + 1);
                        tvSex.setText(words2[position]);
                    }
                }).show();
                break;
            case R.id.bt_save:
                ContentValues values = new ContentValues();
                values.put("name",  mUser.getName() );
                values.put("image", mUser.getImage());
                values.put("sex", mUser.getSex());
                LitePal.update(UserBean.class, values, mUser.getId());
                Toast.makeText(EditInfoActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    images = PictureSelector.obtainMultipleResult(data);
                    Log.e("123", "!!" + images.size());
                    selectList.clear();
                    selectList.addAll(images);
                    mUser.setImage(images.get(0).getPath());
                    Glide.with(this)
                            .load(images.get(0).getPath())
                            .into(hHead);
                    break;
            }
        }
    }

    @OnClick(R.id.bt_password)
    public void onClick() {
        DialogUIUtils.showAlert(this, "Reset Password", "",
                "New Password", "Confirm New Password", "Confirm", "Cancel",
                false, true, false, new DialogUIListener() {
                    @Override
                    public void onPositive() {
                    }

                    @Override
                    public void onNegative() {

                    }

                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                        Log.e("wqe","input1:" + input1 + "--input2:" + input2);
                        if (input1.equals(input2) && !input1.equals("")) {
                            ContentValues values = new ContentValues();
                            values.put("password",  input1.toString() );
                            LitePal.update(UserBean.class, values, mUser.getId());
                            Toast.makeText(EditInfoActivity.this, "Reset Successful", Toast.LENGTH_LONG).show();
                        } else if (!input1.equals(input2)) {
                            Toast.makeText(EditInfoActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditInfoActivity.this, "New Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }


}