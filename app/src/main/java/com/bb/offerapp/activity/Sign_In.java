package com.bb.offerapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.bean.User;
import com.bb.offerapp.util.BaseActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

public class Sign_In extends BaseActivity {

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button login;

//    private AppCompatCheckBox checkBox;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        accountEdit = (EditText) findViewById(R.id.account);
        accountEdit.setText("");
        passwordEdit = (EditText) findViewById(R.id.password);
        passwordEdit.setText("");
        textView = (TextView) findViewById(R.id.create);
        passwordEdit.setTypeface(Typeface.DEFAULT);
        passwordEdit.setTransformationMethod(new PasswordTransformationMethod());
//        checkBox = (AppCompatCheckBox) findViewById(R.id.checkbox1);

        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        textView.setTextColor(Color.WHITE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_In.this, Sign_up.class);
                startActivity(intent);
            }
        });
        //取数据
        sharedPreferences = getSharedPreferences("pass", MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("remember", false);
        if (isRemember) {
            String account = sharedPreferences.getString("account", "");
            String password = sharedPreferences.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
        }

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountEdit.getText().toString().equals("")) {
                    Toast.makeText(Sign_In.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (passwordEdit.getText().toString().equals("")) {
                    Toast.makeText(Sign_In.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    List<User> userlist = DataSupport.findAll(User.class);
                    String account = accountEdit.getText().toString();
                    String password = passwordEdit.getText().toString();
                    User user = new User(account, password);
                    // 如果账号是admin且密码是123456，就认为登录成功
                    if (userlist.contains(user)) {
                        editor = sharedPreferences.edit();
                        editor.putBoolean("remember", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                        editor.commit();
                        Toast.makeText(Sign_In.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        //Intent中也可以传递Bundle
                        Bundle bundle = new Bundle();
                        List<User> userlistbyname = DataSupport.select("account", "phone", "email", "image")
                                .where("account = ?", account).find(User.class);
                        bundle.putSerializable("userforlogin", userlistbyname.get(0));
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(Sign_In.this, "账户或密码错误",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

}
