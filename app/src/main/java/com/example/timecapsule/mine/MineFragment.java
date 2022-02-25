package com.example.timecapsule.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.makeramen.roundedimageview.RoundedImageView;
import com.example.timecapsule.R;
import com.example.timecapsule.bean.UserBean;
import com.example.timecapsule.config.SettingConstant;
import com.example.timecapsule.login.LoginActivity;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MineFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.h_head)
    RoundedImageView hHead;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.tv_exit)
    TextView tvExit;

    private String mParam1;
    private String mParam2;

    private UserBean mUser;
    private static final int REQUEST_SETTING_MONEY = 1015;
    private SharedPreferences mSettings;


    public MineFragment() {
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSettings = getActivity().getSharedPreferences(SettingConstant.SETTING_NAME, getActivity().MODE_PRIVATE);

        List<UserBean> list= LitePal.where("account = ?", mParam1).find(UserBean.class);
        if (list.size() > 0) {
            mUser = list.get(0);
        }
        getInfo();

        return view;
    }

    private void getInfo() {
        List<UserBean> list= LitePal.where("account = ?", mParam1).find(UserBean.class);
        if (list.size() > 0) {
            mUser = list.get(0);
            if (mUser.getImage() == null || mUser.getImage().equals("")) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_head);
                hHead.setImageBitmap(bitmap);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(mUser.getImage());
                hHead.setImageBitmap(bitmap);
            }
            tvAccount.setText(mUser.getName());
            tvInfo.setText("Account: " + mUser.getAccount());
        }
    }


    Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_head, R.id.tv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head:
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("user", mUser);
                startActivityForResult(intent, REQUEST_SETTING_MONEY);
                break;
            case R.id.tv_exit:
                mSettings.edit().putString(SettingConstant.ACCOUNT, "").commit();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING_MONEY && resultCode == getActivity().RESULT_OK) {
            getInfo();
        }
    }
}