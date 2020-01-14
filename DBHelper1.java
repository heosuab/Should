package com.example.gram.should;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

public class DBHelper1 extends SQLiteOpenHelper{

    public DBHelper1(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE MEMBERDATA (id TEXT, password TEXT, name TEXT);");
        db.execSQL("CREATE TABLE COURSEDATA (id TEXT, destination TEXT, ordernum INTEGER, image TEXT);");
        db.execSQL("CREATE TABLE TODODATA (id TEXT, date TEXT, time TEXT, course TEXT, achieve INTEGER);");
        db.execSQL("CREATE TABLE PROFILEDATA (id TEXT, profile TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert_member(String id, String password, String name) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO MEMBERDATA VALUES('" + id + "', '" + password + "', '" + name + "');");
        db.close();
    }

    public void insert_profile(String id, String profile) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO PROFILEDATA VALUES('" + id + "', '" + profile + "');");
        db.close();
    }

    public void insert_course(String id, String destination, String image) {
        SQLiteDatabase db = getWritableDatabase();

        int num = this.CourseCount_course(id) + 1;

        db.execSQL("INSERT INTO COURSEDATA VALUES('" + id + "', '" + destination + "', "+ num + ", '" + image + "');");
        db.close();
    }

    public void insert_todo(String id, String date, String time, String course, int achieve) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO TODODATA VALUES('" + id + "', '" + date + "', '" + time + "', '" + course + "', " + achieve + ");");
        db.close();
    }

    public void update_member_password(String id, String password) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE MEMBERDATA SET password='" + password + "' WHERE id='" + id + "';");
        db.close();
    }

    public void update_member_name(String id, String name) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE MEMBERDATA SET name='" + name + "' WHERE id='" + id + "';");
        db.close();
    }

    public void delete_course(String id) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM COURSEDATA WHERE id='" + id + "';");
        db.close();
    }

    public void delete_todo(String id) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM TODODATA WHERE id='" + id + "';");
        db.close();
    }

    public int CourseCount_course(String id) {
        SQLiteDatabase db = getReadableDatabase();
        int count = 0;

        Cursor cursor = db.rawQuery("SELECT * FROM COURSEDATA",null);
        while(cursor.moveToNext()) {
            if(cursor.getString(0).equals(id))
                count++;
        }
        return count;
    }

    public void delete_member(String id) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM MEMBERDATA WHERE id='" + id + "';");
        db.close();
    }

    public String getResult_member() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM MEMBERDATA", null);
        while (cursor.moveToNext()) {
            result += "id : "
                    + cursor.getString(0)
                    + " password : "
                    + cursor.getString(1)
                    + "name : "
                    + cursor.getString(2)
                    + "\n";
        }

        return result;
    }

    public String getResult_member_passowrd(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM MEMBERDATA", null);
        while (cursor.moveToNext()) {
            if(cursor.getString(0).equals(id)) {
                result = cursor.getString(1);
                break;
            }
        }
        return result;
    }

    public String getResult_course_destination(String id,int i) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM COURSEDATA ", null);
        while (cursor.moveToNext()) {
                if(cursor.getString(0).equals(id) && cursor.getInt(2) == i){
                    result = cursor.getString(1);
                    break;
                }
                else continue;

        }
        return result;
    }

    public Boolean isExist_todo(String id, String date) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM TODODATA", null);
        while(cursor.moveToNext()){
            if(cursor.getString(0).equals(id) &&
                    cursor.getString(1).equals(date))
                return true;
        }
        return false;
    }

    public String getResult_time_todo(String id,String date) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM TODODATA ", null);
        while (cursor.moveToNext()) {
            if(cursor.getString(0).equals(id) && cursor.getString(1).equals(date)){
                result = cursor.getString(2);
                break;
            }
            else continue;

        }
        return result;
    }

    public String getResult_course_todo(String id,String date) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM TODODATA ", null);
        while (cursor.moveToNext()) {
            if(cursor.getString(0).equals(id) && cursor.getString(1).equals(date)){
                result = cursor.getString(3);
                break;
            }
            else continue;

        }
        return result;
    }

    public String getResult_course_image(String id,int i) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM COURSEDATA ", null);
        while (cursor.moveToNext()) {
            if(cursor.getString(0).equals(id) && cursor.getInt(2) == i){
                result = cursor.getString(3);
                break;
            }
            else continue;

        }
        return result;
    }

    public String getResult_profile_image(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM PROFILEDATA ", null);
        while (cursor.moveToNext()) {
            if(cursor.getString(0).equals(id)){
                    result = cursor.getString(1);
                break;
            }
            else continue;

        }
        return result;
    }

    public void update_member_profile(String id, String profile) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE PROFILEDATA SET profile='" + profile + "' WHERE id='" + id + "';");
        db.close();
    }

    public String getResult_member_name(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM MEMBERDATA", null);
        while (cursor.moveToNext()) {
            if(cursor.getString(0).equals(id)) {
                result = cursor.getString(2);
                break;
            }
        }
        return result;
    }

    public Boolean member_idCheck(String id) {

        SQLiteDatabase db = getReadableDatabase();
        Boolean result = false;

        Cursor cursor = db.rawQuery("SELECT * FROM MEMBERDATA", null);
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(id)) {
                result = true;
                break;
            }
        }
        return result;
    }

}
