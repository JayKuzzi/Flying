package com.bb.offerapp.activity;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.bean.User;
import com.bb.offerapp.dialog.ChangePassDialog;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.Square;
import com.bb.offerapp.view.XCImageView;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserMenu extends BaseActivity implements View.OnClickListener {
    private TextView userName;
    private Button myList, changeHead, changePass, exitPass;
    private XCImageView userMenu_head;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        init();
    }

    private void init() {
        sharedPreferences = getSharedPreferences("pass", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userName = (TextView) findViewById(R.id.usermenu_name);
        userMenu_head = (XCImageView) findViewById(R.id.usermenu_head);
        Intent intent = getIntent();
        String loginedusername = intent.getStringExtra("loginedusername");
        List<User> userlistbyname = DataSupport.select("account", "password", "image")
                .where("account = ?", loginedusername).find(User.class);
        userName.setText(userlistbyname.get(0).getAccount());
        userMenu_head.setImageBitmap(Square.centerSquareScaleBitmap(Bytes2Bimap(userlistbyname.get(0).getImage()),200));

        myList = (Button) findViewById(R.id.usermenu_mylist);
        changeHead = (Button) findViewById(R.id.usermenu_changehead);
        changePass = (Button) findViewById(R.id.usermenu_changepass);
        exitPass = (Button) findViewById(R.id.usermenu_exitlogin);
        myList.setOnClickListener(this);
        changeHead.setOnClickListener(this);
        changePass.setOnClickListener(this);
        exitPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.usermenu_mylist:
                Intent intent3 = new Intent(UserMenu.this, OrderList.class);
                startActivity(intent3);
                break;
            case R.id.usermenu_changehead:
                if (ContextCompat.checkSelfPermission(UserMenu.this, Manifest.
                        permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserMenu.this, new String[]{Manifest.
                            permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, 1);
                }
                break;
            case R.id.usermenu_changepass:
                final ChangePassDialog changePassDialog = new ChangePassDialog(UserMenu.this);
                changePassDialog.show();
                changePassDialog.getOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(changePassDialog.getEditText().getText().toString().length()<6){
                            Toast.makeText(UserMenu.this, "密码至少为6位", Toast.LENGTH_SHORT).show();
                        }else{
                            User user = new User();
                            user.setPassword(changePassDialog.getEditText().getText().toString());
                            user.updateAll("account = ?", userName.getText().toString());
                            List<User> changedUser = DataSupport.select("password")
                                    .where("account = ?", userName.getText().toString()).find(User.class);
                            if (changePassDialog.getEditText().getText().toString()
                                    .equals(changedUser.get(0).getPassword())) {
                                Toast.makeText(UserMenu.this, "密码更新成功,请重新登录", Toast.LENGTH_LONG).show();
                                editor.clear();
                                editor.putBoolean("remember", false);
                                editor.commit();
                                finish();
                            } else {
                                Toast.makeText(UserMenu.this, "密码更新失败,请联系客服", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            changePassDialog.cancel();
                        }

                    }
                });
                break;
            case R.id.usermenu_exitlogin:
                Toast.makeText(UserMenu.this, "您已退出，请重新登录", Toast.LENGTH_LONG).show();
                editor.clear();
                editor.putBoolean("remember", false);
                editor.commit();
                finish();
                break;
        }

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, 1);
                } else {
                    Toast.makeText(UserMenu.this, "未授权", Toast.LENGTH_SHORT).show();
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
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(),
                        uri);
                userMenu_head.setImageBitmap(Square.centerSquareScaleBitmap(photo,200));
                User user = new User();
                user.setImage(Bitmap2Bytes(photo));
                user.updateAll("account = ?", userName.getText().toString());

                List<User> changedUser = DataSupport.select("image")
                        .where("account = ?", userName.getText().toString()).find(User.class);
                if (Arrays.equals(Bitmap2Bytes(photo), changedUser.get(0).getImage())) {
                    Toast.makeText(this, "更新头像成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "更新头像失败,请联系客服", Toast.LENGTH_SHORT).show();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }



}
