package com.bb.offerapp.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.bean.User;
import com.bb.offerapp.dialog.ChangePassDialog;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.Constant;
import com.bb.offerapp.util.Square;
import com.bb.offerapp.util.WebService;
import com.bb.offerapp.view.XCImageView;

import org.json.JSONException;
import org.json.JSONObject;
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

    private String account,headImage;//当前账号，头像
    private String newPass,newHead;//要修改的密码，头像
    private ProgressDialog changePassProgress,changeImageProgress;
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.SERVER_NOT_RETURN:
                    changePassProgress.dismiss();
                    Toast.makeText(UserMenu.this, "更新失败 服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.SERVER_NOT_RETURN_FOR_CI:
                    changeImageProgress.dismiss();
                    Toast.makeText(UserMenu.this, "更新失败 服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.CHANGE_PASS_SUCCESS:
                    changePassProgress.dismiss();
                    Toast.makeText(UserMenu.this, "密码更新成功 请重新登录", Toast.LENGTH_LONG).show();
                    editor.clear();
                    editor.putBoolean("remember", false);
                    editor.commit();
                    finish();
                    break;
                case Constant.CHANGE_PASS_FAIL:
                    changePassProgress.dismiss();
                    Toast.makeText(UserMenu.this, "密码更新失败 请检查服务器", Toast.LENGTH_LONG).show();
                    break;
                case Constant.CHANGE_IMAGE_SUCCESS:
                    changeImageProgress.dismiss();
                    Toast.makeText(UserMenu.this, "头像已更新成功", Toast.LENGTH_LONG).show();
                    editor.putString("image", newHead);
                    editor.commit();
                    break;
                case Constant.CHANGE_IMAGE_FAIL:
                    changeImageProgress.dismiss();
                    Toast.makeText(UserMenu.this, "头像更新失败 请检查服务器", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    };

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

        boolean isRemember = sharedPreferences.getBoolean("remember", false);
        if (isRemember) {
            account = sharedPreferences.getString("account", "");
            headImage = sharedPreferences.getString("image", "");
            userName.setText(account);
            byte[] bytes = Base64.decode(headImage, Base64.DEFAULT);
            userMenu_head.setImageBitmap(Square.centerSquareScaleBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length),200));
        }

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
                intent3.putExtra("login_name",account);
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
                        // 检测网络，无法检测wifi
                        if (!checkNetwork()) {
                            Toast toast = Toast.makeText(UserMenu.this, "网络未连接哦", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                        if(changePassDialog.getEditText().getText().toString().length()<6){
                            Toast.makeText(UserMenu.this, "密码至少为6位", Toast.LENGTH_SHORT).show();
                        }else{
                            changePassProgress = new ProgressDialog(UserMenu.this);
                            changePassProgress.setTitle("正在修改");
                            changePassProgress.setMessage("正在修改密码，请稍后。");
                            changePassProgress.setCancelable(false);
                            changePassProgress.show();
                            newPass=changePassDialog.getEditText().getText().toString();
                            new Thread(new changPassThread()).start();
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
                Bitmap2String(photo);
                changeImageProgress = new ProgressDialog(UserMenu.this);
                changeImageProgress.setTitle("正在修改");
                changeImageProgress.setMessage("正在修改头像，请稍后。");
                changeImageProgress.setCancelable(false);
                changeImageProgress.show();
                new Thread(new changImageThread()).start();
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
        newHead = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
    }

    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    private class changPassThread implements Runnable {
        @Override
        public void run() {
            Message message = handler.obtainMessage();
            JSONObject object=new JSONObject();
            try {
                object.put("account", account);
                object.put("newPass", newPass);
                String result= WebService.executeHttpPost(object,"ChangePass");
                if(result.equals("")||result==null){
                    message.what=Constant.SERVER_NOT_RETURN;
                }else {
                    message.what=Integer.parseInt(result);
                }
                handler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class changImageThread implements Runnable {
        @Override
        public void run() {
            Message message = handler.obtainMessage();
            JSONObject object=new JSONObject();
            try {
                object.put("account", account);
                object.put("newImage", newHead);
                String result= WebService.executeHttpPost(object,"ChangeImage");
                if(result.equals("")||result==null){
                    message.what=Constant.SERVER_NOT_RETURN_FOR_CI;
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
