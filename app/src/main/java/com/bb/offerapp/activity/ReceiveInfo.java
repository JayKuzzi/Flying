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

public class ReceiveInfo extends BaseActivity implements
        View.OnClickListener, AMapLocationListener {
    public AMapLocationClientOption mLocationOption;

    AMapLocationClient mlocationClient;

    EditText name, phone, address;
    Button ok;
    ImageView contact1, contact2, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_info);
        init();
        location.setOnClickListener(this);
        contact1.setOnClickListener(this);
        contact2.setOnClickListener(this);
        address.setOnClickListener(this);
        ok.setOnClickListener(this);
        setFinishOnTouchOutside(false);
    }

    private void init() {
        name = (EditText) findViewById(R.id.receive_info_name);
        phone = (EditText) findViewById(R.id.receive_info_phone);
        address = (EditText) findViewById(R.id.receive_info_address);
        contact1 = (ImageView) findViewById(R.id.receive_info_get_contact1);
        contact2 = (ImageView) findViewById(R.id.receive_info_get_contact2);
        location = (ImageView) findViewById(R.id.receive_info_get_loc);
        ok = (Button) findViewById(R.id.receive_info_ok);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(ReceiveInfo.this, "未授权获得位置的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(ReceiveInfo.this, "未授权读取通讯录的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                address.setText(amapLocation.getProvince()
                        + amapLocation.getCity() +
                        amapLocation.getDistrict() +
                        amapLocation.getStreet() +
                        amapLocation.getStreetNum());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.receive_info_get_loc:
                if (ContextCompat.checkSelfPermission(ReceiveInfo.this, Manifest.
                        permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ReceiveInfo.this, new String[]{Manifest.
                            permission.ACCESS_COARSE_LOCATION}, 1);
                } else {
                    getLocation();
                }
                break;
            case R.id.receive_info_get_contact1:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_CONTACTS }, 2);
                } else {
                    readContacts();
                }
                break;
            case R.id.receive_info_get_contact2:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_CONTACTS }, 2);
                } else {
                    readContacts();
                }
                break;
            case R.id.receive_info_address:
                if (mLocationOption != null) {
                    mLocationOption = null;
                }
                if (mlocationClient != null) {
                    mlocationClient.onDestroy();
                    mlocationClient = null;
                    mLocationOption = null;
                }
                break;
            case R.id.receive_info_ok:
                if (name.getText().toString().equals("")||
                        phone.getText().toString().equals("")||
                        address.getText().toString().equals("")) {
                    Toast.makeText(ReceiveInfo.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                }else if (name.getText().toString().length()<2) {
                    Toast.makeText(ReceiveInfo.this, "姓名至少两位", Toast.LENGTH_SHORT).show();
                }else if(phone.getText().toString().length()!=11){
                    Toast.makeText(ReceiveInfo.this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                }else if(address.getText().toString().length()<6){
                    Toast.makeText(ReceiveInfo.this, "请输入正确的地址", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("receive_info_name", name.getText().toString());
                    bundle.putString("receive_info_phone", phone.getText().toString());
                    bundle.putString("receive_info_address", address.getText().toString());
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
                    name.setText(readContactMsg.getName());
                    phone.setText(readContactMsg.getPhone());
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mLocationOption != null) {
            mLocationOption = null;
        }
        if (mlocationClient != null) {
            mlocationClient.onDestroy();
            mlocationClient = null;
            mLocationOption = null;
        }
    }




    private void getLocation() {

        Toast.makeText(this, "正在获取当前位置，需要网络", Toast.LENGTH_SHORT).show();
        mlocationClient = new AMapLocationClient(ReceiveInfo.this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
        //设置定位监听
        mlocationClient.setLocationListener(ReceiveInfo.this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();

    }
}
