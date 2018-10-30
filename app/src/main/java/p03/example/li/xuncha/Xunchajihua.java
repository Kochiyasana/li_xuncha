package p03.example.li.xuncha;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Xunchajihua extends AppCompatActivity {
    private Collection<CalendarDay> dates = new HashSet<>();
    private Set<String> datess = new HashSet<>();
    private String string1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunchajihua);
        MaterialCalendarView widget;
        final TextView tv_jihua = findViewById(R.id.tv_jihua);
        SharedPreferences  pref=getSharedPreferences( "data", MODE_PRIVATE);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        tv_jihua.setText(pref.getString("shijian","巡查计划："));
        datess = pref.getStringSet("dates", datess);
        Iterator iterator1 = datess.iterator();
        while(iterator1.hasNext()) {
            Date date = null;
            try {
                date = sdf1.parse((String) iterator1.next());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dates.add(CalendarDay.from(calendar));
        }
        widget = findViewById(R.id.calendarView);
        widget.addDecorators(new EventDecorator(Color.parseColor("#FF4081"),dates));
        widget.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                SharedPreferences.Editor editor=getSharedPreferences( "data", MODE_PRIVATE).edit();
                date = widget.getSelectedDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                 if(date != null) {
                     if (dates.contains(date)){
                         dates.remove(date);
                         datess.remove(sdf.format(date.getCalendar().getTime()));
                         widget.removeDecorators();
                         widget.addDecorators(new EventDecorator(Color.parseColor("#FF4081"),dates));
                     }else {
                         dates.add(date);
                         datess.add(sdf.format(date.getCalendar().getTime()));
                         widget.addDecorators(new EventDecorator(Color.parseColor("#FF4081"),dates));
                     }
                     string1 = "巡查计划：";
                     Iterator iterator = dates.iterator();
                     while(iterator.hasNext()){
                         CalendarDay str = new CalendarDay();
                         str = (CalendarDay)iterator.next();
                         string1 = string1 + String.valueOf(str.getYear()) + "年" + String.valueOf(str.getMonth()+1) + "月" + String.valueOf(str.getDay() + "日" + " / ");
                     }
                     tv_jihua.setText(string1);
                 }
                editor.putString("shijian",string1);
                editor.putStringSet("dates",datess);
                editor.commit();
            }
        });

    }
}
