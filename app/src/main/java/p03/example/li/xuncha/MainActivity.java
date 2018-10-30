package p03.example.li.xuncha;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.notification.StatusBarNotification;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.githang.statusbar.StatusBarCompat;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnItemClickListener {
    private int userId = -1;
    private String userName = null;
    private String[] inf = new String[200];
    private View drawerView;
    private TextView textView;
    private int cishu;
    private SharedPreferences pref;
    String[] jilu = new String[200];
    private List<SortModel> sourceDateList = new ArrayList<SortModel>();
    private List<SortModel> sourceDateList1 = new ArrayList<SortModel>();
    private ImageView sys;//扫一扫
    private ImageView imageView_jilu;
    private int REQUEST_CODE_SCAN = 369;
    private CircleImageView circleImageView;
    private String uri = null;
    private String userZhiwu = null;
    private String jihua = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("name", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);
        uri = pref.getString(userId + "uri", null);
        initViews();
        setDates();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if (userId != -1 && !jihua.equals("-1")) {
                Intent intent = new Intent();
                intent.putExtra("jihua", jihua);
                intent.putExtra("userId",   String.valueOf(userId));
                intent.setClass(this, Zuixinxiaoxi.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "亲，你还没有登录哦~，请先登录，再来点我", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_gallery) {

            if (userId != -1) {
                Intent intent = new Intent();
                intent.setClass(this, KuaiJieYongYueActivity.class);
                intent.putExtra("userId", String.valueOf(userId));
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "亲，你还没有登录哦~，请先登录，再来点我", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_share) {
            if (userId != -1) {
                Intent intent = new Intent();
                intent.setClass(this, XiaoxiActivity.class);
                intent.putExtra("userId", String.valueOf(userId));
                intent.putExtra("userName", userName);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "亲，你还没有登录哦~，请先登录，再来点我", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_tuichu) {
            if (userId != -1) {
                SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                editor.putString("name", "点击头像登录");
                editor.putInt("userId", -1);
                editor.commit();
                userId = -1;
                textView.setText("点击头像登录");
                jilu = new String[200];
                circleImageView.setImageResource(R.drawable.logo);
            } else {
                Toast.makeText(MainActivity.this, "请先登录,才能退出哦", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_zhiNengFenXi) {
            if (userId != -1) {
                if (jilu[0] != null && !jilu[0].equals("null")) {
                    Intent intent = new Intent();
                    intent.putExtra("userId", String.valueOf(userId));
                    intent.putExtra("jilu", jilu);
                    intent.setClass(this, ChartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "没有记录，无法分析", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "请先登录,才能使用此功能", Toast.LENGTH_SHORT).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //跳转
    public void tiaoZhuan(Class a) {
        Intent intent = new Intent();
        intent.putExtra("userId", String.valueOf(userId));
        intent.putExtra("bb", 0);
        intent.setClass(getApplicationContext(), a);
        startActivityForResult(intent, 2);
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    //判断是否为今天
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);//拿传入的时间减去现在的时间

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    //判断是否为明天
    public static boolean IsTomorrow(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 1) {
                return true;
            }
        }
        return false;
    }

    //在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
    private void initViews() {
        setContentView(R.layout.activity_main);
        AppBarLayout appBarLayout = findViewById(R.id.aaaaaaa);
        Drawable bkDrawable = appBarLayout.getBackground().mutate();
        bkDrawable.setAlpha(0);
        appBarLayout.setBackgroundDrawable(bkDrawable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundDrawable(bkDrawable);
        setSupportActionBar(toolbar);
        Set<String> datess = new HashSet<>();
        pref = getSharedPreferences("data", MODE_PRIVATE);
        datess = pref.getStringSet("dates", datess);
        imageView_jilu = findViewById(R.id.jilu);
        imageView_jilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != -1) {
                    Intent intent = new Intent();
                    intent.putExtra("userId", String.valueOf(userId));
                    intent.putExtra("jilu", jilu);
                    intent.setClass(MainActivity.this, XunChuaJiLu.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "亲，你还没有登录哦~，请先登录，再来点我", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sys = findViewById(R.id.image_saoyisao);
        sys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
         /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */
                ZxingConfig config = new ZxingConfig();
                config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                config.setPlayBeep(true);//是否播放提示音
                config.setShake(true);//是否震动
                config.setShowAlbum(true);//是否显示相册
                config.setShowFlashLight(true);//是否显示闪光灯
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
        Iterator iterator1 = datess.iterator();
        //控制主页面的小红点的次数显示
        int ci = 0;
        while (iterator1.hasNext()) {
            String time = (String) iterator1.next();
            try {
                if (IsToday(time) || IsTomorrow(time)) {
                    ci++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        com.getbase.floatingactionbutton.FloatingActionButton fab = findViewById(R.id.fab_menu);
        Button button = findViewById(R.id.button_tianjia);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId != -1) {
                    tiaoZhuan(TianJiaXunChaActivity.class);
                } else {
                    Toast.makeText(MainActivity.this, "亲，你还没有登录哦~，请先登录，再来点我", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getBackground().setAlpha(190);
        navigationView.setNavigationItemSelectedListener(this);

        drawerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        drawerView.getBackground().setAlpha(190);
        circleImageView = (CircleImageView) drawerView.findViewById(R.id.circleImageView);
        if (uri != null) {
            circleImageView.setImageURI(Uri.parse(uri));
        }
        textView = drawerView.findViewById(R.id.nav_touXiang);
        pref = getSharedPreferences("name", MODE_PRIVATE);
        userName = pref.getString("name", "点击头像登录");
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId == -1) {
                    Intent mintent = new Intent();
                    mintent.setClass(MainActivity.this, User_Login.class);
                    startActivityForResult(mintent, 1);
                } else {
                    Intent mintent = new Intent();
                    mintent.putExtra("userId", userId);
                    mintent.putExtra("userName", userName);
                    mintent.putExtra("userZhiwu", userZhiwu);
                    mintent.setClass(MainActivity.this, UserInformationActivity.class);
                    startActivityForResult(mintent, 3);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (data != null) {
                userId = Integer.parseInt(data.getStringExtra("userId"));
                userName = data.getStringExtra("userName");
                userZhiwu = data.getStringExtra("userZhiwu");
                SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                editor.putString("name", userName);
                editor.putInt("userId", userId);
                editor.commit();
            }
            if (userId != -1) {
                pref = getSharedPreferences("name", MODE_PRIVATE);
                uri = pref.getString(userId + "uri", null);
                if (uri != null) {
                    circleImageView.setImageURI(Uri.parse(uri));
                }
                setDates();
            }
        } else if (requestCode == 2) {
            if (data != null) {
                if (data.getStringExtra("a").equals("a")) {
                    cishu++;
                }
            }
        } else if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                GetXinxi getXinxi = new GetXinxi(content);
                getXinxi.start();
            }
        } else if (requestCode == 3) {
            if (data != null) {
                uri = data.getStringExtra("uri");
                circleImageView.setImageURI(Uri.parse(uri));
                SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                editor.putString(userId + "uri", uri);
                editor.commit();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String teacher = null, course_name = null, class2 = null, classroom = null;
            switch (msg.what) {
                case 10:
                    Bundle bundle = msg.getData();
                    teacher = bundle.getString("teacher");
                    course_name = bundle.getString("course_name");
                    class2 = bundle.getString("class2");
                    classroom = bundle.getString("classroom");
                    break;
            }
            if (teacher != null && course_name != null && class2 != null && classroom != null) {
                Toast.makeText(MainActivity.this, "扫描成功", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent();
                mIntent.putExtra("bb", 1);
                mIntent.putExtra("userId", userId);
                mIntent.putExtra("teacher", teacher);
                mIntent.putExtra("course_name", course_name);
                mIntent.putExtra("class2", class2);
                mIntent.putExtra("classroom", classroom);
                // 设置结果，并进行传送
                mIntent.setClass(MainActivity.this, TianJiaXunChaActivity.class);
                startActivity(mIntent);
//                MainActivity.this.setResult(3, mIntent);

            }
        }
    };

    class GetXinxi extends Thread {
        String course_id;

        public GetXinxi(String course_id) {
            this.course_id = course_id;
        }

        @Override
        public void run() {
            SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
            try {
                String xinxi = null;
                String[] xx = new String[5];
                xinxi = socketFuWuQi.getXinxi(course_id);

                if (xinxi != null && !xinxi.equals("null")) {
                    xx = xinxi.split(",");
                    Message message = new Message();
                    message.what = 10;
                    Bundle bundle = new Bundle();
                    bundle.putString("teacher", xx[0]);
                    bundle.putString("course_name", xx[1]);
                    bundle.putString("class2", xx[2]);
                    bundle.putString("classroom", xx[3]);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "当前时间无课程。", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void setDates() {
        sourceDateList = new ArrayList<SortModel>();
        sourceDateList1 = new ArrayList<SortModel>();
        textView.setText(userName);
        if (userId != -1) {
            jilu();
            jihua();
        }
        //获取多少次
        new Thread(new Runnable() {
            @Override
            public void run() {
                SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                try {
                    cishu = socketFuWuQi.getCishu(String.valueOf(userId));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(int position) {

    }


    private void jilu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                try {
                    jilu = socketFuWuQi.getJiLu(Integer.toString(userId));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
    private void jihua() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                try {
                    jihua = socketFuWuQi.getjihua(Integer.toString(userId));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
