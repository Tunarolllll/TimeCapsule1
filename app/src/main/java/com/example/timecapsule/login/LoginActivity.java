package com.example.timecapsule.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timecapsule.MainActivity;
import com.example.timecapsule.R;
import com.example.timecapsule.bean.UserBean;
import com.example.timecapsule.config.SettingConstant;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    TextView btLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mSettings = getSharedPreferences(SettingConstant.SETTING_NAME, MODE_PRIVATE);

    }

    @OnClick({R.id.bt_login, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void login(){
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        if (account.equals("")) {
            Toast.makeText(LoginActivity.this,"Account",Toast.LENGTH_SHORT).show();
            return;
        } else if (password.equals("")) {
            Toast.makeText(LoginActivity.this,"Password",Toast.LENGTH_SHORT).show();
            return;
        }
        List<UserBean> list= LitePal.where("account = ? and password = ? ", account,password).find(UserBean.class);
        if (list.size() > 0) {
            mSettings.edit().putString(SettingConstant.ACCOUNT, account).commit();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Toast.makeText(LoginActivity.this,"Your user ID or password is incorrect",Toast.LENGTH_SHORT).show();
    }
}