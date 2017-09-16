package com.bb.offerapp.activity;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.MyViewPaperAdapter;
import com.bb.offerapp.bean.OrderLists;
import com.bb.offerapp.bean.User;
import com.bb.offerapp.dialog.CityNameDialog;
import com.bb.offerapp.util.Square;
import com.bb.offerapp.view.MyTextView;
import com.bb.offerapp.view.WrapContentHeightViewPager;
import com.bb.offerapp.view.XCImageView;
import com.nineoldandroids.view.ViewHelper;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.thinkpage.lib.api.TPCity;
import com.thinkpage.lib.api.TPListeners;
import com.thinkpage.lib.api.TPWeatherDaily;
import com.thinkpage.lib.api.TPWeatherManager;
import com.thinkpage.lib.api.TPWeatherNow;

import org.litepal.crud.DataSupport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import me.grantland.widget.AutofitTextView;


public class OfferAppMainActivity extends AppCompatActivity {
    //微信分享接口
    private static final String WX_APP_ID = "wx299170b6572a6255";
    //两次退出程序
    private static boolean isExit = false;
    final Github github = new Github(this);
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private IWXAPI iwxapi;
    //悬浮按钮
    private FloatingActionButton fab;
    //欢迎
    private AppBarLayout app_bar;
    //tab菜单
    private TabLayout tabLayout;
    private String[] tabTittle = {"下单", "资费"};
    //工具栏
    private Toolbar toolbar;
    //Viewpager
    private WrapContentHeightViewPager viewPager;
    private MyViewPaperAdapter myViewPaperAdapter;
    //侧滑菜单
    private DrawerLayout drawer;
    private NavigationView navigationView;
    //首页壁纸
    private ImageView index_image;
    //天气
    private ImageView weather_icon;
    private AutofitTextView weather_city;
    private AutofitTextView weather_case;
    private TextView weather_degree;
    private TextView weather_wind;
    private String temperature;//实时温度
    //改变天气位置dailog
    private CityNameDialog cityNameDialog;
    //侧滑用户信息
    private TextView login_name, login_phone;
    private MyTextView login_email;
    private XCImageView login_image;

    private String share_way = "pyq";

    private void regToWx() {
        iwxapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        iwxapi.registerApp(WX_APP_ID);
    }

    public WrapContentHeightViewPager getViewPager() {
        return viewPager;
    }

    public TextView getLogin_name() {
        return login_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView_ImageView();//初始化首页壁纸
        initView_NavigationView();//初始化DrawerLayout侧滑菜单导航
        initView_NavigationView_head();//初始化DrawerLayout侧滑头部菜单导航
        initView_AppBarLayout();//初始化AppBarLayout
        initView_Toolbar();//初始化Toolbar
        initView_TabLayout();//初始化TabLayout
        initView_ViewPager();//初始化ViewPaper
        initView_FloatingActionButton();//初始化FloatingActionButton(悬浮按钮)
        initView_WeatherCard();//初始化天气状况
        initView_WeatherData("济南");//从"心知天气"获得3天天气api接口数据
        initView_NowWeatherData("济南");//获得当天实时天气
        initView_DrawerLayout();//DrawerLayout
        regToWx();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init_AutoLogin();//自动登录
    }

    private void init_AutoLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("pass", MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("remember", false);
        if (isRemember) {
            String account = sharedPreferences.getString("account", "");
            String phone = sharedPreferences.getString("phone", "");
            String email = sharedPreferences.getString("email", "");
            String image = sharedPreferences.getString("image", "");
            login_name.setText(account);
            login_phone.setText(phone);
            login_email.setText(email);
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            login_image.setImageBitmap(Square.centerSquareScaleBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length),200));
        } else {
            login_name.setText("未登录");
            login_phone.setText("phone");
            login_email.setText("E-mail");
            login_image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.defaule_head_home));
        }

    }

    private void initView_NavigationView_head() {
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        LinearLayout head_iv = (LinearLayout) headerLayout.findViewById(R.id.login_layout);
        login_name = (TextView) headerLayout.findViewById(R.id.login_name);
        login_phone = (TextView) headerLayout.findViewById(R.id.login_phone);
        login_email = (MyTextView) headerLayout.findViewById(R.id.login_email);
        login_image = (XCImageView) headerLayout.findViewById(R.id.login_head);

        head_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login_name.getText().toString().equals("未登录")) {
                    Intent intent = new Intent(OfferAppMainActivity.this, Sign_In.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(OfferAppMainActivity.this, UserMenu.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void initView_DrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setScrimColor(Color.TRANSPARENT);
        //按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerClosed(View v) {

            }

            @Override
            public void onDrawerOpened(View v) {

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // 主体窗口
                View mainFrame = drawer.getChildAt(0);

                // 这个就是隐藏起来的边侧滑菜单栏
                View leftMenu = drawerView;

                addQQStyleSlide(mainFrame, leftMenu, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int arg0) {

            }
        });

    }


    //两次退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出飞送",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }


    private void initView_ImageView() {
        index_image = (ImageView) findViewById(R.id.imageview);
    }


    private void initView_NavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        setNavigationViewSize(navigationView, 0.6f, 0.85f);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_list) {
                    if (login_name.getText().toString().equals("未登录")) {
                        Toast.makeText(OfferAppMainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OfferAppMainActivity.this, Sign_In.class);
                        startActivityForResult(intent, 200);
                    } else {
                        Intent intent = new Intent(OfferAppMainActivity.this, OrderList.class);
                        intent.putExtra("login_name",login_name.getText().toString());
                        startActivity(intent);

                    }

                } else if (id == R.id.nav_crazy) {
                    if (login_name.getText().toString().equals("未登录")) {
                        Toast.makeText(OfferAppMainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OfferAppMainActivity.this, Sign_In.class);
                        startActivityForResult(intent, 200);
                    } else {
                        Intent intent = new Intent(OfferAppMainActivity.this, OrderHall.class);
                        intent.putExtra("login_name",login_name.getText().toString());
                        startActivity(intent);

                    }
                } else if (id == R.id.nav_share) {
                    if (login_name.getText().toString().equals("未登录")) {
                        Toast.makeText(OfferAppMainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OfferAppMainActivity.this, Sign_In.class);
                        startActivityForResult(intent, 200);
                    } else {
                        LayoutInflater layoutInflater = LayoutInflater.from(OfferAppMainActivity.this);
                        View select = layoutInflater.inflate(R.layout.share, null);
                        final RadioButton wc = (RadioButton) select.findViewById(R.id.wc);
                        final RadioButton pyq = (RadioButton) select.findViewById(R.id.pyq);
                        final RadioGroup result = (RadioGroup) select.findViewById(R.id.share_way);
                        result.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                                switch (checkedId) {
                                    case R.id.wc:
                                        wc.setBackground(getResources().getDrawable(R.mipmap.wc_select));
                                        pyq.setBackground(getResources().getDrawable(R.mipmap.pyq));
                                        share_way="wc";
                                        break;
                                    case R.id.pyq:
                                        wc.setBackground(getResources().getDrawable(R.mipmap.wc));
                                        pyq.setBackground(getResources().getDrawable(R.mipmap.pyq_select));
                                        share_way="pyq";
                                        break;
                                }
                            }
                        });

                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(OfferAppMainActivity.this, R.style.AlertDialogCustom);
                        dialog1.setTitle("选择方式");
                        dialog1.setView(select);
                        dialog1.setCancelable(false);
                        dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!iwxapi.isWXAppInstalled()) {
                                    Toast.makeText(OfferAppMainActivity.this, "您还未安装微信客户端",
                                            Toast.LENGTH_SHORT).show();
                                }

                                String text = " 飞送，同城跑腿业务，为用户提供专人直送，1小时内达，欢迎使用。www.woobo.me";
                                WXTextObject textObject = new WXTextObject();
                                textObject.text = text;

                                WXMediaMessage msg = new WXMediaMessage();
                                msg.mediaObject = textObject;
                                msg.description = text;

                                SendMessageToWX.Req req = new SendMessageToWX.Req();
                                req.transaction = buildTransaction("text");
                                req.message = msg;
                                if(share_way.equals("wc")){
                                    req.scene = SendMessageToWX.Req.WXSceneSession;
                                }else if(share_way.equals("pyq")){
                                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                }else {
                                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                }
                                iwxapi.sendReq(req);
                            }
                        });
                        dialog1.show();




                    }

                } else if (id == R.id.nav_express) {
                    Intent intent = new Intent(OfferAppMainActivity.this, Express.class);
                    startActivity(intent);
                } else if (id == R.id.nav_question) {
                    Intent intent = new Intent(OfferAppMainActivity.this, Question.class);
                    startActivity(intent);

                } else if (id == R.id.nav_group) {
                    Intent intent = new Intent(OfferAppMainActivity.this, Settings.class);
                    startActivity(intent);
                }


                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, 1);
                } else {
                    Toast.makeText(OfferAppMainActivity.this, "未授权", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(fab, "即将致电客服电话", Snackbar.LENGTH_LONG)
                            .setAction("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mHandler.removeCallbacks(github);
                                    Toast.makeText(OfferAppMainActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    mHandler.postDelayed(github, 3000);
                } else {
                    Toast.makeText(OfferAppMainActivity.this, "未授权电话操作", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;
        else {
            switch (requestCode) {
                case 1:
                    try {
                        Uri uri = data.getData();
                        Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(),
                                uri);
                        index_image.setImageBitmap(photo);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }

    private void initView_AppBarLayout() {
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("");//设置收缩之前标题
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    collapsingToolbarLayout.setTitle(weather_case.getText().toString() + " " + temperature + "°");//设置收缩之后标题
                } else {
                    collapsingToolbarLayout.setTitle("");//设置收缩中标题
                }
            }
        });
    }


    private void initView_Toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void initView_TabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，MODE_FIXED是固定的,不能超出屏幕，MODE_SCROLLABLE可超出屏幕范围滚动的
        tabLayout.addTab(tabLayout.newTab().setText("下单"));
        tabLayout.addTab(tabLayout.newTab().setText("资费"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //关联viewPager
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initView_ViewPager() {
        viewPager = (WrapContentHeightViewPager) findViewById(R.id.viewpaper);
        viewPager.setAdapter(myViewPaperAdapter = new MyViewPaperAdapter(getSupportFragmentManager(), tabTittle));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void initView_FloatingActionButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.argb(222, 248, 78, 95)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(OfferAppMainActivity.this, Manifest.
                        permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OfferAppMainActivity.this, new String[]{Manifest.
                            permission.CALL_PHONE}, 2);
                } else {
                    Snackbar.make(view, "即将致电客服电话", Snackbar.LENGTH_LONG)
                            .setAction("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mHandler.removeCallbacks(github);
                                    Toast.makeText(OfferAppMainActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    mHandler.postDelayed(github, 3000);
                }

            }
        });
    }

    private void initView_WeatherCard() {

        weather_icon = (ImageView) findViewById(R.id.weather_icon);
        weather_city = (AutofitTextView) findViewById(R.id.weather_city);
        weather_case = (AutofitTextView) findViewById(R.id.weather_case);
        weather_degree = (TextView) findViewById(R.id.weather_degree);
        weather_wind = (TextView) findViewById(R.id.weather_wind);

        weather_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityNameDialog = new CityNameDialog(OfferAppMainActivity.this);
                cityNameDialog.show();
                cityNameDialog.getOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        weather_city.setText(cityNameDialog.getEditText().getText().toString());
                        initView_WeatherData(cityNameDialog.getEditText().getText().toString());
                        initView_NowWeatherData(cityNameDialog.getEditText().getText().toString());
                        cityNameDialog.cancel();
                    }
                });


            }
        });

    }

    private void initView_WeatherData(String cityName) {
        final TPWeatherManager weatherManager = TPWeatherManager.sharedWeatherManager();
        //使用心知天气官网获取的key和用户id初始化WeatherManager
        weatherManager.initWithKeyAndUserId("fyajwhj499drrlob", "U041DF6FDF");
        weatherManager.getWeatherDailyArray(new TPCity(cityName)
                , TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese
                , TPWeatherManager.TPTemperatureUnit.kCelsius
                , new Date(), 3
                , new TPListeners.TPWeatherDailyListener() {
                    @Override
                    public void onTPWeatherDailyAvailable(TPWeatherDaily[] tpWeatherDailies, String s) {
                        if (tpWeatherDailies != null) {
                            String textDay = tpWeatherDailies[0].textDay;
                            switch (textDay) {
                                case "晴":
                                    weather_icon.setImageResource(R.drawable.sun);
                                    break;
                                case "多云":
                                    weather_icon.setImageResource(R.drawable.cloudy);
                                    break;
                                case "晴间多云":
                                    weather_icon.setImageResource(R.drawable.suncloudy);
                                    break;
                                case "大部多云":
                                    weather_icon.setImageResource(R.drawable.mostcloudy);
                                    break;
                                case "阴":
                                    weather_icon.setImageResource(R.drawable.overcast);
                                    break;
                                case "阵雨":
                                    weather_icon.setImageResource(R.drawable.shower);
                                    break;
                                case "雷阵雨":
                                    weather_icon.setImageResource(R.drawable.storm);
                                    break;
                                case "雷阵雨伴有冰雹":
                                    weather_icon.setImageResource(R.drawable.showersnow);
                                    break;
                                case "小雨":
                                    weather_icon.setImageResource(R.drawable.shower);
                                    break;
                                case "中雨":
                                    weather_icon.setImageResource(R.drawable.midshower);
                                    break;
                                case "大雨":
                                    weather_icon.setImageResource(R.drawable.heavyshower);
                                    break;
                                case "暴雨":
                                    weather_icon.setImageResource(R.drawable.heavyshower);
                                    break;
                                case "大暴雨":
                                    weather_icon.setImageResource(R.drawable.heavyshower);
                                    break;
                                case "特大暴雨":
                                    weather_icon.setImageResource(R.drawable.heavyshower);
                                    break;
                                case "冻雨":
                                    weather_icon.setImageResource(R.drawable.heavyshower);
                                    break;
                                case "雨夹雪":
                                    weather_icon.setImageResource(R.drawable.showersnow);
                                    break;
                                case "阵雪":
                                    weather_icon.setImageResource(R.drawable.snow);
                                    break;
                                case "小雪":
                                    weather_icon.setImageResource(R.drawable.snow);
                                    break;
                                case "中雪":
                                    weather_icon.setImageResource(R.drawable.midshower);
                                    break;
                                case "大雪":
                                    weather_icon.setImageResource(R.drawable.heavysnow);
                                    break;
                                case "暴雪":
                                    weather_icon.setImageResource(R.drawable.heavysnow);
                                    break;
                                case "浮尘":
                                    weather_icon.setImageResource(R.drawable.dust);
                                    break;
                                case "扬沙":
                                    weather_icon.setImageResource(R.drawable.dust);
                                    break;
                                case "沙尘暴":
                                    weather_icon.setImageResource(R.drawable.dust);
                                    break;
                                case "强沙尘暴":
                                    weather_icon.setImageResource(R.drawable.dust);
                                    break;
                                case "雾":
                                    weather_icon.setImageResource(R.drawable.foggy);
                                    break;
                                case "霾":
                                    weather_icon.setImageResource(R.drawable.haze);
                                    break;
                                case "风":
                                    weather_icon.setImageResource(R.drawable.windy);
                                    break;
                                case "大风":
                                    weather_icon.setImageResource(R.drawable.windy);
                                    break;
                                case "飓风":
                                    weather_icon.setImageResource(R.drawable.hurricane);
                                    break;
                                case "热带风暴":
                                    weather_icon.setImageResource(R.drawable.hurricane);
                                    break;
                                case "龙卷风":
                                    weather_icon.setImageResource(R.drawable.hurricane);
                                    break;
                                case "冷":
                                    weather_icon.setImageResource(R.drawable.cold);
                                    break;
                                case "热":
                                    weather_icon.setImageResource(R.drawable.hot);
                                    break;
                                case "未知":
                                    weather_icon.setImageResource(R.drawable.na);
                                    break;
                            }
                            weather_case.setText(textDay);
                            weather_degree.setText(tpWeatherDailies[0].lowTemperature + "°~" + tpWeatherDailies[0].highTemperature + "°");
                            weather_wind.setText(tpWeatherDailies[0].windDirection + "风 " + (int) tpWeatherDailies[0].windScale + "级");
                        } else {
                            System.out.println(s); //错误信息
                        }
                    }

                });
    }

    private void initView_NowWeatherData(String cityNamee) {
        TPWeatherManager weatherManager = TPWeatherManager.sharedWeatherManager();
        weatherManager.initWithKeyAndUserId("fyajwhj499drrlob", "U041DF6FDF");
        weatherManager.getWeatherNow(new TPCity(cityNamee)
                , TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese
                , TPWeatherManager.TPTemperatureUnit.kCelsius
                , new TPListeners.TPWeatherNowListener() {
                    @Override
                    public void onTPWeatherNowAvailable(TPWeatherNow weatherNow, String errorInfo) {
                        if (weatherNow != null) {
                            temperature = weatherNow.temperature + "";
                        } else {
                        }
                    }
                });

    }


    //此处将控制NavigationView侧滑出的高度、宽度已经重心位置（居中？靠上？靠下？）
    private void setNavigationViewSize(NavigationView nv, float w_percent, float h_percent) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        //宽度默认是MATCH_PARENT，
        //NavigationView的宽度
        int width = (int) (displayMetrics.widthPixels * w_percent);

        //NavigationView的高度
        int height = (int) (displayMetrics.heightPixels * h_percent);

        //高度默认是MATCH_PARENT，如果不打算打满屏幕高度：DrawerLayout.LayoutParams.MATCH_PARENT，
        // 那么比如可以设置成屏幕高度的80%(即0.8f)
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(width, height);

        //主要要设置center，否则侧滑出来的菜单栏将从下往上绘制相应高度和宽度而不是居中
        params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;

        nv.setLayoutParams(params);
    }


    private void addQQStyleSlide(View mainFrame, View leftMenu, float slideOffset) {
        //GAP的值决定左边侧滑出来的宽度和右边的主界面之间在侧滑过程以及侧滑结束后的间距。
        //如果不设置此值或者设置为0，则将恢复成Android系统默认的样式，即侧滑出来的界面和主界面之间紧密贴在一起。
        int GAP = 100;

        float leftScale = 0.5f + 0.5f * slideOffset;
        float rightScale = 1 - 0.2f * slideOffset;

        ViewHelper.setScaleX(leftMenu, leftScale);
        ViewHelper.setScaleY(leftMenu, leftScale);
        ViewHelper.setAlpha(leftMenu, 0.5f + 0.5f * slideOffset);
        ViewHelper.setTranslationX(mainFrame, (leftMenu.getMeasuredWidth() + GAP) * slideOffset);
        ViewHelper.setPivotX(mainFrame, 0);
        ViewHelper.setPivotY(mainFrame, mainFrame.getMeasuredHeight() / 2);
        mainFrame.invalidate();
        ViewHelper.setScaleX(mainFrame, rightScale);
        ViewHelper.setScaleY(mainFrame, rightScale);

        // 该处主要是为了使背景的颜色渐变过渡。
        // 如果失效，则可能是因为Android DrawerLayout的NavigationView绘制背景的图层互相之间遮盖导致。
        //此处不关乎重点实现，作为代码在未来的复用，仍然保留，当然也可以删掉！
        getWindow().getDecorView().getBackground().setColorFilter(evaluate(slideOffset, Color.BLACK, Color.TRANSPARENT),
                PorterDuff.Mode.SRC_OVER);
    }

    private Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }


    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}

class Github implements Runnable {
    Context context;

    Github(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Intent it = new Intent(Intent.ACTION_CALL);
        it.setData(Uri.parse("tel:15610106465"));
        context.startActivity(it);
    }
}

