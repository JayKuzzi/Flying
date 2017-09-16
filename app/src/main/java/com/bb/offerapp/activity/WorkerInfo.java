package com.bb.offerapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.ReadContactMsg;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkerInfo extends BaseActivity implements View.OnClickListener{

    TextView name;
    EditText phone;
    Button ok;
    ImageView  contact2;
    String workerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_info);
        workerName=getIntent().getStringExtra("workerName");
        init();
        contact2.setOnClickListener(this);
        ok.setOnClickListener(this);
        setFinishOnTouchOutside(false);
    }

    private void init() {
        name = (TextView) findViewById(R.id.worker_info_name);
        name.setText(workerName);
        phone = (EditText) findViewById(R.id.worker_info_phone);
        contact2 = (ImageView) findViewById(R.id.worker_info_get_contact2);
        ok = (Button) findViewById(R.id.worker_info_ok);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(WorkerInfo.this, "未授权读取通讯录的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.worker_info_get_contact2:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_CONTACTS }, 2);
                } else {
                    readContacts();
                }
                break;
            case R.id.worker_info_ok:
                if (phone.getText().toString().equals("")) {
                    Toast.makeText(WorkerInfo.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                }else if(phone.getText().toString().length()!=11){
                    Toast.makeText(WorkerInfo.this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("worker_info_name", workerName);
                    bundle.putString("worker_info_phone", phone.getText().toString());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

        }

    }

    private void readContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    ReadContactMsg readContactMsg = new ReadContactMsg(this,data);
                    phone.setText(readContactMsg.getPhone());
                }
                break;

            default:
                break;
        }
    }
}
