package com.bb.offerapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.bean.User;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.Square;
import com.bb.offerapp.view.XCImageView;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Sign_up extends BaseActivity {
    private EditText account, password, phone, email;
    private TextView textView;
    private Button button;
    private Bitmap photo;
    private XCImageView imageView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finish();
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
                    List<User> userlist = DataSupport.findAll(User.class);
                    for (User user:userlist) {
                        if(user.getAccount().equals(account.getText().toString())){
                            Toast.makeText(Sign_up.this, "用户名重复，请重新输入", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    User newUser = new User();
                    newUser.setAccount(account.getText().toString());
                    newUser.setPassword(password.getText().toString());
                    newUser.setPhone(phone.getText().toString());
                    newUser.setEmail(email.getText().toString());
                    newUser.setImage(Bitmap2Bytes(photo));
                    newUser.save();

                    User lastUserInDatabase = DataSupport.findLast(User.class);
                    User user =new User(account.getText().toString(),password.getText().toString());
                    if(user.equals(lastUserInDatabase)){
                        button.setEnabled(false);
                        Toast.makeText(Sign_up.this, "注册成功", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessageDelayed(0, 1500);
                    }else{
                        Toast.makeText(Sign_up.this, "注册失败,请联系客服", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
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

    public byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }




}
