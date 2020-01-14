package com.example.gram.should;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        final DBHelper1 dbHelper1 = new DBHelper1(getApplicationContext(), "MemberData.db",null,1);

        final EditText edit_id = (EditText)findViewById(R.id.edit_id);
        final EditText edit_password = (EditText)findViewById(R.id.edit_password);
        final EditText edit_confirm = (EditText)findViewById(R.id.edit_confirm);
        final EditText edit_name = (EditText)findViewById(R.id.edit_name);

        final Button btn_id_check = (Button)findViewById(R.id.btn_id_check);
        final Button btn_join = (Button)findViewById(R.id.btn_join);

        final CheckBox checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        final CheckBox checkbox2 = (CheckBox)findViewById(R.id.checkbox2);

        dbHelper1.insert_member("administrator","0000","administrator");

        btn_id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = edit_id.getText().toString();

                if(dbHelper1.member_idCheck(id)) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(JoinActivity.this);
                    dlg.setMessage("사용할 수 없는 아이디입니다.");
                    dlg.setPositiveButton("확인",null);
                    dlg.show();
                }
                else {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(JoinActivity.this);
                    dlg.setMessage("사용 가능한 아이디입니다.");
                    dlg.setPositiveButton("확인",null);
                    dlg.show();
                }
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = edit_id.getText().toString();
                String password = edit_password.getText().toString();
                String confirm = edit_confirm.getText().toString();
                String name = edit_name.getText().toString();

                if(id.getBytes().length>0 && password.getBytes().length>0 &&
                        confirm.getBytes().length>0 && name.getBytes().length>0) {

                    if (!dbHelper1.member_idCheck(id)) {

                        if (password.equals(confirm)) {

                            if (checkbox1.isChecked() && checkbox2.isChecked()) {
                                dbHelper1.insert_member(id, password, name);

                                AlertDialog.Builder dlg = new AlertDialog.Builder(JoinActivity.this);
                                dlg.setMessage("회원가입이 완료되었습니다. 로그인 후 이용해주세요.");
                                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                    }
                                });
                                dlg.show();

                            } else {
                                Toast.makeText(getApplicationContext(),"이용약관에 동의해주세요.",Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"아이디 중복확인이 필요합니다.",Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(getApplicationContext(),"필수 입력사항이 비었습니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
