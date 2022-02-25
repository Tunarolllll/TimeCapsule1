package com.example.timecapsule.login;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timecapsule.BaseActivity;
import com.example.timecapsule.R;
import com.example.timecapsule.bean.UserBean;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    TextView btLogin;
    @BindView(R.id.et_name)
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.bt_login)
    public void onClick() {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();

        if (account.equals("")) {
            Toast.makeText(RegisterActivity.this, "Account", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.equals("")) {
            Toast.makeText(RegisterActivity.this, "Password", Toast.LENGTH_SHORT).show();
            return;
        }else if (name.equals("")) {
            Toast.makeText(RegisterActivity.this, "Nickname", Toast.LENGTH_SHORT).show();
            return;
        }

        List<UserBean> list= LitePal.where("account = ? ", account).find(UserBean.class);
        if (list.size() > 0) {
            Toast.makeText(RegisterActivity.this, "This account is already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        UserBean bean = new UserBean(name, account, password);
        bean.save();
        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
        finish();

    }
}