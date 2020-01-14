package com.example.gram.should;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CourseActivity extends Activity{

    public int i;
    Bitmap bmp1;
    Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        final String id = intent.getExtras().getString("아이디");
        final DBHelper1 dbHelper1 = new DBHelper1(getApplicationContext(), "MemberData.db",null,1);
        final int course_num = dbHelper1.CourseCount_course(id);
        ImageView popup_menu = (ImageView)findViewById(R.id.popup_menu);

        Button btn_course_plus = (Button)findViewById(R.id.btn_course_plus);
        LinearLayout course_scroll_layout = (LinearLayout) findViewById(R.id.course_scroll_layout);

        for(i=1;i<=course_num;i++) {
            LinearLayout dynamic = new LinearLayout(this);
            dynamic.setOrientation(LinearLayout.HORIZONTAL);
            TextView newtxt = new TextView(this);
            newtxt.setText("제 "+ i + "코스");
            newtxt.setTextSize(15);
            newtxt.setGravity(Gravity.CENTER);
            dynamic.setBackgroundResource(R.drawable.round_corner3);
            dynamic.setPadding(390,70,20,70);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            param.topMargin=30;
            param.bottomMargin=30;
            param.leftMargin=30;
            param.rightMargin=30;
            param.gravity= Gravity.CENTER;
            dynamic.setLayoutParams(param);

            final int temp = i;

            dynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String destination = dbHelper1.getResult_course_destination(id,temp);
                    String image = dbHelper1.getResult_course_image(id,temp);
                    Uri imageUri = Uri.parse(image);

                    AlertDialog.Builder dlg = new AlertDialog.Builder(CourseActivity.this);

                    LinearLayout layout = new LinearLayout(CourseActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    TextView txt = new TextView(CourseActivity.this);
                    txt.setText(destination);
                    txt.setTextSize(25);
                    txt.setGravity(Gravity.CENTER);
                    txt.setPadding(0,100,0,0);

                    ImageView img = new ImageView(CourseActivity.this);
                    img.setImageURI(imageUri);
                    img.setPadding(80,0,80,0);

                    layout.addView(txt);
                    layout.addView(img);

                    dlg.setView(layout);

                    dlg.setPositiveButton("확인",null);
                    dlg.show();
                }
            });

            dynamic.addView(newtxt);
            course_scroll_layout.addView(dynamic);
        }

        btn_course_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(CourseActivity.this);
                LinearLayout layout = new LinearLayout(CourseActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText et = new EditText(CourseActivity.this);
                dlg.setTitle("코스명 설정");

                final Button btn_plusImage = new Button(CourseActivity.this);
                btn_plusImage.setText("사진 추가");

                btn_plusImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                        int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if(permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(CourseActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, 1);

                    }
                });

                layout.addView(et);
                layout.addView(btn_plusImage);
                dlg.setView(layout);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper1.insert_course(id,et.getText().toString(),photoUri.toString());
                        finish();
                        Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
                        intent.putExtra("아이디",id);
                        startActivity(intent);
                    }
                });
                dlg.show();

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            photoUri = data.getData();
        }
    }



}
