package com.example.timecapsule.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.timecapsule.R;



public class InfoDialog extends Dialog {

    private TextView titleTv ;

    private TextView tvInfo ;

    private Button btCancel,btConfirm ;

    private Context context;

    private View columnLineView ;
    public InfoDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context=context;
    }


    private String message;
    private String title;
    private String info;
    private String positive,negtive ;
    private int imageResId = -1 ;


    private boolean isSingle = true;
    private boolean isRed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);
        setCanceledOnTouchOutside(true);
        initView();
        refreshView();
        initEvent();
    }


    private void initEvent() {
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onClickBottomListener!= null) {
                    onClickBottomListener.onNegtiveClick();
                }
            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onClickBottomListener!= null) {
                    onClickBottomListener.onPositiveClick();
                }
            }
        });

        if (isSingle) {
            btCancel.setVisibility(View.GONE);
            btConfirm.setBackground(null);
            btConfirm.setTextColor(context.getResources().getColor(R.color.purple_500));
        }

    }


    private void refreshView() {

        if (!TextUtils.isEmpty(info)) {
            tvInfo.setText(info);
        }
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        if (!TextUtils.isEmpty(negtive)) {
            btConfirm.setText(negtive);
        }

    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    private void initView() {

        btCancel = (Button) findViewById(R.id.bt_cancel);
        btConfirm = (Button) findViewById(R.id.bt_confirm);

        titleTv = (TextView) findViewById(R.id.title);
        tvInfo = (TextView) findViewById(R.id.tv_info);
    }

    public OnClickBottomListener onClickBottomListener;
    public InfoDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }
    public interface OnClickBottomListener{
        public void onPositiveClick();
        public void onNegtiveClick();

    }

    public String getMessage() {
        return message;
    }

    public InfoDialog setMessage(String message) {
        this.message = message;
        return this ;
    }

    public String getTitle() {
        return title;
    }

    public InfoDialog setTitle(String title) {
        this.title = title;
        return this ;
    }
    public InfoDialog setTitleRed(boolean isRed) {
        this.isRed = isRed;
        return this ;
    }
    public String getPositive() {
        return positive;
    }

    public InfoDialog setPositive(String positive) {
        this.positive = positive;
        return this ;
    }

    public String getInfo() {
        return info;
    }

    public InfoDialog setInfo(String info) {
        this.info = info;
        return this ;
    }


    public String getNegtive() {
        return negtive;
    }

    public InfoDialog setNegtive(String negtive) {
        this.negtive = negtive;
        return this ;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public InfoDialog setSingle(boolean single) {
        isSingle = single;
        return this ;
    }

    public InfoDialog setImageResId(int imageResId) {
        this.imageResId = imageResId;
        return this ;
    }

}
