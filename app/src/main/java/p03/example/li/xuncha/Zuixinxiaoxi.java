package p03.example.li.xuncha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Zuixinxiaoxi extends AppCompatActivity {
    private Map<String, String> datess = new HashMap<>();
    private List<Map<String, String> > mDatas = new ArrayList<Map<String, String> >();
    private HomeAdapter mAdapter;
    private String jihua, userId;
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
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }
    public String getDay(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);
            if (diffDay == 0) {
                return "今天";
            }else if (diffDay == 1) {
                return "明天";
            }else if (diffDay == -1) {
                return "两天前";
            }else if (diffDay == -2) {
                return "三天前";
            }else if (diffDay == -3) {
                return "四天前";
            }else if (diffDay == -4) {
                return "五天前";
            }else if (diffDay < -4) {
                return day;
            }
        }
        return null;
    }
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_zuixinxiaoxi);
        jihua = intent.getStringExtra("jihua");
        userId = intent.getStringExtra("userId");
        System.out.println("最新计划：" + jihua);
        if (jihua != null) {
            String[] s = jihua.split("/");
            System.out.println("s计划：" + s[0]+ " " + s[1] );
            for (int i = 0; i < s.length; i++) {
                String[] ss = s[i].split(",");
                String time = ss[0];
                String place = ss[1];
                String bot = ss[2];
                System.out.println("ss计划：" + ss[0]+ " " + ss[1] + " " + ss[2]);
                try {
                    if (bot.equals("false")){
                        bot = "未完成";
                    }
                    if (bot.equals("true")){
                        bot = "已完成";
                    }
                    datess = new HashMap<>();
                    String day = getDay(time);
                    datess.put("time", time);
                    datess.put("str",  day +"在" + place + "巡查");
                    datess.put("bot", bot);
                        mDatas.add(datess);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        RecyclerView recyclerView = findViewById(R.id.relativeLayout_zuixin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new HomeAdapter(mDatas, Zuixinxiaoxi.this,userId ));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                mAdapter.removeData(position);
//
//            }
//        });

    }

}
