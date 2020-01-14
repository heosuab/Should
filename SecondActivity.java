package com.example.gram.should;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageView profileView; //프로필 기본 이미지
        LinearLayout second_layout1; //프로필 레이아웃
        LinearLayout layout_achievement, layout_course, layout_membership;
        Button btn_workout;
        long now = System.currentTimeMillis();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final DBHelper1 dbHelper1 = new DBHelper1(getApplicationContext(), "MemberData.db",null,1);

        Intent intent = getIntent();
        final String id = intent.getExtras().getString("아이디");

        profileView = (ImageView)findViewById(R.id.profileView); //프로필사진(둥글게 할라고)
        second_layout1 = (LinearLayout)findViewById(R.id.second_layout1); //프로필 있는 레이아웃
        layout_achievement = (LinearLayout)findViewById(R.id.layout_achievement); //My Achievement 레이아웃
        layout_course = (LinearLayout)findViewById(R.id.layout_course); //Course Settings 레이아웃
        layout_membership = (LinearLayout)findViewById(R.id.layout_membership); //Membership Info 레이아웃
        btn_workout = (Button)findViewById(R.id.btn_workout); //Workout 버튼
        ImageView popup_menu = (ImageView)findViewById(R.id.popup_menu);
        TextView profile_name = (TextView)findViewById(R.id.profile_name);
        TextView second_time_txt = (TextView)findViewById(R.id.second_time_txt);
        TextView second_course_txt = (TextView)findViewById(R.id.second_course_txt);

        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 M월 d일");
        String todayDate = sdf.format(date);

        profileView.setBackground(new ShapeDrawable(new OvalShape())); //프로필 사진 둥글게
        profileView.setClipToOutline(true);

        profile_name.setText(dbHelper1.getResult_member_name(id) + " 님");
        if(dbHelper1.getResult_time_todo(id,todayDate).length() > 0) {
            second_time_txt.setText(dbHelper1.getResult_time_todo(id, todayDate) + " : ");
            second_course_txt.setText(dbHelper1.getResult_course_todo(id, todayDate));
        }
        else {
            second_time_txt.setText("일정이 ");
            second_course_txt.setText("없습니다.");
        }

        second_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodoActivity.class);
                intent.putExtra("아이디",id);
                startActivity(intent);
            }
        });

        layout_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
                intent.putExtra("아이디",id);
                startActivity(intent);
            }
        });

        layout_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemberActivity.class);
                intent.putExtra("아이디",id);
                startActivity(intent);
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

        layout_achievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AchieveActivity.class);
                intent.putExtra("아이디",id);
                startActivity(intent);
            }
        });

    }

}
