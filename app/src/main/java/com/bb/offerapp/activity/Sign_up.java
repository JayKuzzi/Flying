package com.bb.offerapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.Constant;
import com.bb.offerapp.util.Square;
import com.bb.offerapp.util.WebService;
import com.bb.offerapp.view.XCImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sign_up extends BaseActivity {
    private EditText account, password, phone, email;
    private TextView textView;
    private Button button;
    private Bitmap photo;
    private XCImageView imageView;
    private String headImage;//将头像转换成String
    private ProgressDialog checkNameDialog;
    private ProgressDialog RegDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.SERVER_NOT_RETURN:
                    checkNameDialog.dismiss();
                    Toast.makeText(Sign_up.this, "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                    break;
                case Constant.DELAY_DISMISS_DIALOG:
                    finish();
                    break;
                case Constant.NAME_HAS:
                    checkNameDialog.dismiss();
                    Toast.makeText(Sign_up.this, "用户名重复 请重新输入", Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                    break;
                case Constant.NAME_NOT_HAS:
                    checkNameDialog.dismiss();
                    Reg();
                    break;
                case Constant.REG_SUCCESS:
                    RegDialog.dismiss();
                    Toast.makeText(Sign_up.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    message.what = Constant.DELAY_DISMISS_DIALOG;
                    handler.sendMessageDelayed(message, Constant.DELAY_DISMISS_DIALOG);
                    break;
                case Constant.REG_FAIL:
                    RegDialog.dismiss();
                    Toast.makeText(Sign_up.this, "数据库添加失败 请检查服务器", Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                    break;
                case Constant.SERVER_NOT_RETURN_FOR_REG:
                    RegDialog.dismiss();
                    Toast.makeText(Sign_up.this, "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        textViewDoSomethig();
        buttonDoSomethig();
        imageViewDoSomething();
    }


    private void init() {
        photo = BitmapFactory.decodeResource(getResources(),R.mipmap.defaule_head);
        imageView= (XCImageView) findViewById(R.id.sign_head);
        account = (EditText) findViewById(R.id.sign_account);
        password = (EditText) findViewById(R.id.sign_password);
        phone = (EditText) findViewById(R.id.sign_phone);
        email = (EditText) findViewById(R.id.sign_email);
        textView = (TextView) findViewById(R.id.have);
        button = (Button) findViewById(R.id.sign_up);
    }

    private void textViewDoSomethig() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        textView.setTextColor(Color.WHITE);
    }

    private void imageViewDoSomething() {
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.defaule_head);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Sign_up.this, Manifest.
                        permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Sign_up.this, new String[]{Manifest.
                            permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, 1);
                }
            }
        });
    }

    private void buttonDoSomethig() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检测网络，无法检测wifi
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(Sign_up.this,"网络未连接哦", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                if (account.getText().toString().equals("")||
                        password.getText().toString().equals("")||
                        phone.getText().toString().equals("")||
                        email.getText().toString().equals("")) {
                    Toast.makeText(Sign_up.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                }else if (account.getText().toString().length()<2) {
                    Toast.makeText(Sign_up.this, "用户名至少两位", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().length()<6){
                    Toast.makeText(Sign_up.this, "密码至少6位", Toast.LENGTH_SHORT).show();
                }else if(phone.getText().toString().length()!=11){
                    Toast.makeText(Sign_up.this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                }else if(email.getText().toString().length()<9){
                    Toast.makeText(Sign_up.this, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
                }else{
                    button.setEnabled(false);
                    checkNameDialog = new ProgressDialog(Sign_up.this);
                    checkNameDialog.setTitle("正在注册");
                    checkNameDialog.setMessage("正在检测用户名是否重复");
                    checkNameDialog.setCancelable(false);
                    checkNameDialog.show();
                    new Thread(new checkNameThread()).start();
                }
            }
        });
    }

    private void Reg() {
        RegDialog = new ProgressDialog(Sign_up.this);
        RegDialog.setTitle("正在注册");
        RegDialog.setMessage("正在添加数据到网络数据库。");
        RegDialog.setCancelable(false);
        RegDialog.show();
        Bitmap2String(photo);
        new Thread(new regThread()).start();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, 1);
                } else {
                    Toast.makeText(Sign_up.this, "未授权", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        else {
            try {
                Uri uri = data.getData();
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(),
                        uri);
                imageView.setImageBitmap(Square.centerSquareScaleBitmap(photo,200));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    public void Bitmap2String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        headImage = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
    }


    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }



    private class checkNameThread implements Runnable {
        @Override
        public void run() {
            String checkNamePath = "http://" + Constant.MY_SERVER_IP + "/offerapp/RegCheckAccount"+ "?account=" + account.getText().toString();
            Message message =new Message();
            String checkNameResult=WebService.executeHttpGet(checkNamePath);
            if(checkNameResult==null||checkNameResult.equals("")){
                message.what = Constant.SERVER_NOT_RETURN;
            }else {
                message.what=Integer.parseInt(checkNameResult.trim());
            }
            handler.sendMessage(message);
        }
    }

    private class regThread implements Runnable {
        @Override
        public void run() {
            Message message = handler.obtainMessage();
            JSONObject object=new JSONObject();
            try {
                object.put("account", account.getText().toString());
                object.put("email", email.getText().toString());
                object.put("headPicture", headImage);
                object.put("password", password.getText().toString());
                object.put("phone", phone.getText().toString());
                String result= WebService.executeHttpPost(object,"RegLet");
                if(result.equals("")||result==null){
                    message.what=Constant.SERVER_NOT_RETURN_FOR_REG;
                }else {
                    message.what=Integer.parseInt(result);
                }
                handler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
