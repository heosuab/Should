package com.example.gram.should;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodoActivity extends Activity {

    public int selectYear, selectMonth, selectDay;
    public String todayDate;
    Calendar cal;
    final DBHelper1 dbHelper1 = new DBHelper1(this, "MemberData.db",null,1);
    LinearLayout todo_layout_vi1, todo_layout_vi2;
    TextView todo_time_txt, todo_course_txt;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Intent intent = getIntent();
        id = intent.getExtras().getString("아이디");

        final Button btn_todo_plus = (Button)findViewById(R.id.btn_todo_plus);
        final CalendarView calendar = (CalendarView)findViewById(R.id.calendar);
        final TextView date_txt = (TextView)findViewById(R.id.date_txt);
        todo_time_txt = (TextView)findViewById(R.id.todo_time_txt);
        todo_course_txt = (TextView)findViewById(R.id.todo_course_txt);
        todo_layout_vi1 = (LinearLayout)findViewById(R.id.todo_layout_vi1);
        todo_layout_vi2 = (LinearLayout)findViewById(R.id.todo_layout_vi2);
        ImageView popup_menu = (ImageView)findViewById(R.id.popup_menu);

        cal = Calendar.getInstance();
        Date curDate = new Date(calendar.getDate());
        cal.setTime(curDate);

        todayDate = Integer.toString(cal.get(Calendar.YEAR)) + "년 " + Integer.toString(cal.get(Calendar.MONTH)+1) + "월 " +
                Integer.toString(cal.get(Calendar.DATE)) + "일";

        date_txt.setText(todayDate);
        show_todo();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectYear = year;
                selectMonth = month + 1;
                selectDay = dayOfMonth;
                todayDate = Integer.toString(selectYear) + "년 " + Integer.toString(selectMonth) + "월 " + Integer.toString(selectDay) + "일";

                date_txt.setText(todayDate);
                show_todo();
            }
        });

        btn_todo_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(todayDate == null) {
                    Toast.makeText(getApplicationContext(), "날짜를 먼저 선택해주세요.", Toast.LENGTH_LONG).show();
                }

                else {
                    if(dbHelper1.getResult_course_todo(id, todayDate).length() <= 0) {

                        AlertDialog.Builder dlg = new AlertDialog.Builder(TodoActivity.this);
                        dlg.setTitle(todayDate + "의 일정 추가");
                        dlg.setIcon(R.drawable.time);

                        int courseNum = dbHelper1.CourseCount_course(id);
                        List<String> course_items = new ArrayList<String>();

                        LinearLayout layout = new LinearLayout(TodoActivity.this);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        TextView txt1 = new TextView(TodoActivity.this);
                        txt1.setText("운동시간을 선택하세요.");
                        txt1.setPadding(70, 40, 70, 0);
                        layout.addView(txt1);

                        String[] time_items = {"10분", "20분", "30분", "40분", "50분", "60분", "70분", "80분", "90분", "100분", "110분", "120분"};
                        final Spinner dropdown_time = new Spinner(TodoActivity.this);
                        ArrayAdapter<String> time_adapter = new ArrayAdapter<String>(TodoActivity.this, android.R.layout.simple_dropdown_item_1line, time_items);
                        dropdown_time.setAdapter(time_adapter);
                        dropdown_time.setPadding(70, 0, 70, 0);
                        layout.addView(dropdown_time);

                        TextView txt2 = new TextView(TodoActivity.this);
                        txt2.setText("코스를 선택하세요");
                        txt2.setPadding(70, 0, 70, 0);
                        layout.addView(txt2);

                        for (int i = 0; i < courseNum; i++) {
                            course_items.add("제 " + (i + 1) + "코스");
                        }
                        final Spinner dropdown_course = new Spinner(TodoActivity.this);
                        ArrayAdapter<String> course_adapter = new ArrayAdapter<String>(TodoActivity.this, android.R.layout.simple_dropdown_item_1line, course_items);
                        dropdown_course.setAdapter(course_adapter);
                        dropdown_course.setPadding(70, 0, 70, 0);
                        layout.addView(dropdown_course);

                        dlg.setView(layout);
                        dlg.setNegativeButton("취소", null);
                        dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String Time = dropdown_time.getSelectedItem().toString();
                                String Course = dropdown_course.getSelectedItem().toString();

                                dbHelper1.insert_todo(id, todayDate, Time, Course, 0);
                                show_todo();
                            }
                        });
                        dlg.show();
                    }

                    else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(TodoActivity.this);
                        dlg.setMessage("이미 일정이 존재합니다. 삭제하시겠습니까?");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper1.delete_todo(id);
                                show_todo();
                            }
                        });
                        dlg.setNegativeButton("취소",null);
                        dlg.show();
                    }
                }
            }
        });

        popup_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(getApplicationContext(), v);
                getMenuInflater().inflate(R.menu.menu, p.getMenu());

                p.show();
            }
        });

    }

    public void show_todo() {
        if(!dbHelper1.isExist_todo(id, todayDate)) {
            todo_layout_vi1.setVisibility(View.GONE);
            todo_layout_vi2.setVisibility(View.VISIBLE);
        }
        else{
            todo_time_txt.setText(dbHelper1.getResult_time_todo(id, todayDate));
            todo_course_txt.setText(dbHelper1.getResult_course_todo(id,todayDate));
            todo_layout_vi1.setVisibility(View.VISIBLE);
            todo_layout_vi2.setVisibility(View.GONE);
        }
    }

}
