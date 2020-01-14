package com.example.gram.should;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.IOException;

public class MemberActivity extends Activity{

    Bitmap bmp1;
    Uri photoUri;
    ImageView profile_imgview;
    Intent intent = getIntent();
    String id;

    final DBHelper1 dbHelper1 = new DBHelper1(this, "MemberData.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        Intent intent = getIntent();
        id = intent.getExtras().getString("아이디");

        final EditText member_edit_id = (EditText)findViewById(R.id.member_edit_id);
        final EditText member_edit_password = (EditText)findViewById(R.id.member_edit_password);
        final EditText member_edit_confirm = (EditText)findViewById(R.id.member_edit_confirm);
        final EditText member_edit_name = (EditText)findViewById(R.id.member_edit_name);
        final Button member_btn_cancel = (Button)findViewById(R.id.member_btn_cancel);
        final Button member_btn_finish = (Button)findViewById(R.id.member_btn_finish);
        final Button member_btn_delete = (Button)findViewById(R.id.member_btn_delete);
        final Button member_btn_profile = (Button)findViewById(R.id.member_btn_profile);
        profile_imgview = (ImageView)findViewById(R.id.profile_imgview);
        final ImageView popup_menu = (ImageView)findViewById(R.id.popup_menu);

        member_edit_id.setHint(id);
        member_edit_id.setFocusable(false);
        member_edit_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MemberActivity.this, "id는 수정이 불가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        member_edit_password.setHint(dbHelper1.getResult_member_passowrd(id));
        member_edit_confirm.setHint(dbHelper1.getResult_member_passowrd(id));
        member_edit_name.setHint(dbHelper1.getResult_member_name(id));

        if(dbHelper1.getResult_profile_image(id).length() > 10) {
            String image = dbHelper1.getResult_profile_image(id);
            Uri imageUri = Uri.parse(image);
            profile_imgview.setImageURI(imageUri);
        }

        member_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        member_btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(member_edit_password.getText().toString().equals(member_edit_confirm.getText().toString())) {
                    if(member_edit_password.getText().length() > 0)
                        dbHelper1.update_member_password(id, member_edit_password.getText().toString());
                    if(member_edit_name.getText().length() > 0)
                        dbHelper1.update_member_name(id, member_edit_name.getText().toString());
                    Toast.makeText(MemberActivity.this, "정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(MemberActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        member_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MemberActivity.this);
                dlg.setMessage("회원 탈퇴가 완료된 후엔 정보를 복구할 수 없습니다. 계속하시겠습니까?");
                dlg.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper1.delete_member(id);
                        dbHelper1.delete_course(id);
                        dbHelper1.delete_todo(id);
                        Toast.makeText(MemberActivity.this, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
            }
        });

        member_btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(MemberActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 1);
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
            if(dbHelper1.getResult_profile_image(id).toString().length() <= 0)
                dbHelper1.insert_profile(id,photoUri.toString());
            else
                dbHelper1.update_member_profile(id,photoUri.toString());
            Uri temp = Uri.parse(dbHelper1.getResult_profile_image(id));
            try {
                bmp1 =MediaStore.Images.Media.getBitmap( getContentResolver(), temp );
            } catch (IOException e) {
                e.printStackTrace();
            }

        profile_imgview.setImageBitmap(bmp1);

    }
    }
}
