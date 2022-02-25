package com.example.timecapsule.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.example.timecapsule.R;
import com.example.timecapsule.adapter.MyCapsuleRecyclerAdapter;
import com.example.timecapsule.bean.CapsuleBean;
import com.example.timecapsule.config.SettingConstant;
import com.example.timecapsule.utils.DateUtils;
import com.example.timecapsule.utils.Utils;
import com.example.timecapsule.utils.WrapContentLinearLayoutManager;
import com.example.timecapsule.view.InfoDialog;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CapsuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CapsuleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<CapsuleBean> mList = new ArrayList<>();
    private MyCapsuleRecyclerAdapter adapter;
    private SharedPreferences mSettings;

    private static final int REQUEST_SETTING_MONEY = 1011;

    public CapsuleFragment() {
    }


    public static CapsuleFragment newInstance(String param1, String param2) {
        CapsuleFragment fragment = new CapsuleFragment();
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

        View view= inflater.inflate(R.layout.fragment_capsule, container, false);
        unbinder = ButterKnife.bind(this, view);

        mList = LitePal.where("account = ?",mParam1).find(CapsuleBean.class);
        mSettings = getActivity().getSharedPreferences(SettingConstant.SETTING_NAME, getActivity().MODE_PRIVATE);
        recyclerview.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), 1));
        adapter = new MyCapsuleRecyclerAdapter(mList, getActivity());
        recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyCapsuleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mList.get(position).getOpenTime() > System.currentTimeMillis()) {
                    Toast.makeText(getActivity(),"It's not the time yet",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), CapsuleActivity.class);
                intent.putExtra("item", mList.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                List<TieBean> datass = new ArrayList<>();
                datass.add(new TieBean("Delete"));
                DialogUIUtils.showSheet(getActivity(), datass,
                        "Cancel", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
                            @Override
                            public void onItemClick(CharSequence text, int i) {
                                Log.e("1111", "!!!!!!!!" + position);
                                if (i == 0) {
                                    LitePal.delete(CapsuleBean.class, mList.get(position).getId());
                                    mList.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }).show();

            }
        });

        isToday();

        return view;
    }

    private void isToday(){
        long time = DateUtils.getTodayTime(0);
        long first = mSettings.getLong(SettingConstant.TODAY, SettingConstant.Today_Default);
        if (time > first) {
            mSettings.edit().putLong(SettingConstant.TODAY, System.currentTimeMillis()).commit();
            List<CapsuleBean> list = LitePal.where("account = ? and openTime < ?", mParam1, System.currentTimeMillis() + "").find(CapsuleBean.class);
            if (list.size() > 0) {
                int random = Utils.getNum(list.size());
                final InfoDialog dialog = new InfoDialog(getActivity());
                dialog
                        .setTitle("Recommendations")
                        .setTitleRed(false)
                        .setInfo(list.get(random).getTitle())
                        .setSingle(true)
                        .setNegtive("Details")
                        .setOnClickBottomListener(new InfoDialog.OnClickBottomListener() {
                            @Override
                            public void onPositiveClick() {
                                Intent intent = new Intent(getActivity(), CapsuleActivity.class);
                                intent.putExtra("item", mList.get(random));
                                startActivity(intent);
                                dialog.dismiss();
                            }

                            @Override
                            public void onNegtiveClick() {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }

    }

    @OnClick(R.id.iv_add)
    public void onClick() {
        Intent intent = new Intent(getActivity(), EditCapsuleActivity.class);
        intent.putExtra("account", mParam1);
        startActivityForResult(intent,REQUEST_SETTING_MONEY);
    }



    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING_MONEY && resultCode == getActivity().RESULT_OK) {
            mList.clear();
            List<CapsuleBean> list = LitePal.where("account = ?",mParam1).find(CapsuleBean.class);
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }
}