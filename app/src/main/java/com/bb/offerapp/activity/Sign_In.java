package com.bb.offerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.bean.User;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.Constant;
import com.bb.offerapp.util.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

public class Sign_In extends BaseActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog loginDialog;
    private TextView textView;
    private String loginResult;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SERVER_NOT_RETURN:
                    loginDialog.dismiss();
                    Toast.makeText(Sign_In.this, "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.LOGIN_FAIL:
                    login.setEnabled(true);
                    loginDialog.dismiss();
                    Toast.makeText(Sign_In.this, "账户或密码错误 请重新输入", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.LOGIN_SUCCESS:
                    loginDialog.dismiss();
                    sharedPreferences = getSharedPreferences("pass", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("remember", true);
                    JSONArray myJsonArray = null;
                    try {
                        myJsonArray = new JSONArray(loginResult);
                        JSONObject object = myJsonArray.getJSONObject(0);
                        System.out.println(object.getString("account"));
                        editor.putString("image", object.getString("image"));
                        editor.putString("account", object.getString("account"));
                        editor.putString("email", object.getString("email"));
                        editor.putString("phone", object.getString("phone"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.commit();
                    Toast.makeText(Sign_In.this, "登录成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

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

        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        textView.setTextColor(Color.WHITE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_In.this, Sign_up.class);
                startActivity(intent);
            }
        });


        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检测网络，无法检测wifi
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(Sign_In.this, "网络未连接哦", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if (accountEdit.getText().toString().equals("")) {
                    Toast.makeText(Sign_In.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (passwordEdit.getText().toString().equals("")) {
                    Toast.makeText(Sign_In.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    login.setEnabled(false);
                    loginDialog = new ProgressDialog(Sign_In.this);
                    loginDialog.setTitle("正在登录");
                    loginDialog.setMessage("正在验证用户信息，请稍后。");
                    loginDialog.setCancelable(false);
                    loginDialog.show();
                    new Thread(new loginThread()).start();
                }

            }
        });
    }

    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    private class loginThread implements Runnable {
        @Override
        public void run() {
            String loginPath = "http://" + Constant.MY_SERVER_IP + "/offerapp/LogLet" + "?account=" + accountEdit.getText().toString() + "&password=" + passwordEdit.getText().toString();
            Message message = new Message();
            loginResult = WebService.executeHttpGet(loginPath);
            if (loginResult == null||loginResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (loginResult.trim().equals("0")) {
                message.what = Constant.LOGIN_FAIL;
            } else {
                message.what = Constant.LOGIN_SUCCESS;
            }
            handler.sendMessage(message);
        }
    }

}
