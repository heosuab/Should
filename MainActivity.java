package com.example.gram.should;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.SystemClock.sleep;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout_main, main_layout, logo_layout;
    ConstraintLayout Constraint_layout;
    Animation main_logo;
    ImageView Image_logo;
    TextView main_text;
    Button btn_login, btn_Join;
    EditText login_id, login_password;
    int click=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DBHelper1 dbHelper1 = new DBHelper1(getApplicationContext(), "MemberData.db",null,1);

        layout_main = (LinearLayout)findViewById(R.id.layout_main); //전체 Linearlayout
        main_layout = (LinearLayout)findViewById(R.id.main_layout); //로그인,회원가입 Edittext포함 레이아웃
        logo_layout = (LinearLayout)findViewById(R.id.logo_layout);
        Constraint_layout = (ConstraintLayout)findViewById(R.id.Constraint_layout);
        btn_login = (Button)findViewById(R.id.btn_login); //로그인 버튼
        btn_Join = (Button)findViewById(R.id.btn_Join); //회원가입 버튼
        Image_logo = (ImageView)findViewById(R.id.image_logo); //로고 이미지
        main_text = (TextView)findViewById(R.id.main_text); //쓸데없는 문구
        login_id = (EditText)findViewById(R.id.login_id);
        login_password = (EditText)findViewById(R.id.login_password);
        layout_main.setOnClickListener(layout_main_ClickListener); //화면 클릭 시 로그인,회원가입 버튼 생성
        btn_Join.setOnClickListener(btn_Join_ClickListener); //회원가입 버튼 클릭시 회우너ㅏㄱ니ㅏ

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = login_id.getText().toString();
                String password = login_password.getText().toString();

                if(id.getBytes().length>0 && password.getBytes().length >0) {

                    if (dbHelper1.member_idCheck(id)) {

                        if (dbHelper1.getResult_member_passowrd(id).equals(password)) {

                            String name = dbHelper1.getResult_member_name(id);

                            Toast.makeText(getApplicationContext(),
                                    name + "님 환영합니다.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                            intent.putExtra("아이디",id);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),"아이디 정보가 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"로그인 정보를 입력하세요",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    View.OnClickListener layout_main_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            main_logo = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.main_logo);
            if(click==0) {
                logo_layout.startAnimation(main_logo);
                click++;
                Constraint_layout.setBackgroundResource(R.drawable.background2_blur);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    main_layout.setVisibility(View.VISIBLE);
                    btn_login.setVisibility(View.VISIBLE);
                    btn_Join.setVisibility(View.VISIBLE);
                }
            },1000);
        }
    };

    View.OnClickListener btn_Join_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        }
    };

}
