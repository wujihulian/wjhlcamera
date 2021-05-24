package com.wj.uikit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: DBHelper
 * Author: xiongxiang
 * Date: 2021/1/18
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class DBHelper extends SQLiteOpenHelper {
    public String TAB_NAME = "hk_device";
    private SQLiteDatabase mReadableDatabase;
    private static DBHelper dbHelper;

    public static DBHelper getInstance() {
        return dbHelper;
    }
    public static  void init(Context context){
        dbHelper = new DBHelper(context);
    }

    public DBHelper(Context context) {
        super(context, "wj_device_kzsk.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTab(db);
    }

    /**
     * 创建设备表
     * @param db
     */
    private void createTab(SQLiteDatabase db) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("create table ");
        buffer.append(TAB_NAME);
        buffer.append("(");
        buffer.append("id INTEGER PRIMARY KEY AUTOINCREMENT ");
        buffer.append(",");
        buffer.append("device_serial Text");
        buffer.append(",");
        buffer.append("device_code Text");
        buffer.append(",");
        buffer.append("device_type Text");
        buffer.append(",");
        buffer.append("device_factory Text");
        buffer.append(")");
        db.execSQL(buffer.toString());
    }

    private static final String TAG = "DBHelper";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<DeviceInfo> queryAll() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        //查询所有
        Cursor query = writableDatabase.query(TAB_NAME, null, null, null, null, null, null);
        //条件查询
        //  Cursor query = writableDatabase.query(TAB_NAME, null, "device_serial=?", new String[]{"FJJDJDJ0"}, null, null, null);

        //分页查询
        //   Cursor query=  writableDatabase.rawQuery("select * from device22 limit  ?,10", new String[]{0 + ""});
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        while (query.moveToNext()) {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.id = query.getInt(query.getColumnIndex("id"));
            deviceInfo.device_serial = query.getString(query.getColumnIndex("device_serial"));
            deviceInfo.device_code = query.getString(query.getColumnIndex("device_code"));
            deviceInfo.device_type = query.getString(query.getColumnIndex("device_type"));
            deviceInfo.device_factory = query.getString(query.getColumnIndex("device_factory"));
            deviceInfos.add(deviceInfo);
        }
        writableDatabase.close();
        query.close();

        return deviceInfos;
    }

    public boolean query(String device_serial) {
        SQLiteDatabase writableDatabase = getWritableDatabase();

        Cursor query = writableDatabase.query(TAB_NAME, null, "device_serial=?", new String[]{device_serial}, null, null, null);

        if (query.moveToNext()){
            String device_serial1 = query.getString(query.getColumnIndex("device_serial"));
            return true;
        }
        writableDatabase.close();
        query.close();
        return false;
    }


    /**
     * 更新
     */
    public void update(DeviceInfo deviceInfo, String id) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.update(TAB_NAME, deviceInfo.toContentValues(), "id=?", new String[]{id});
        writableDatabase.close();
    }


    public long insert(DeviceInfo deviceInfo) {
        ContentValues contentValues = deviceInfo.toContentValues();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        long insert = getWritableDatabase().insert(TAB_NAME, null, contentValues);
        writableDatabase.close();
        return insert;
    }
    public int  delete(String device_serial){
        return delete("device_serial=?",new String[]{device_serial});
    }

    public int delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        int delete = writableDatabase.delete(TAB_NAME, whereClause, whereArgs);
        writableDatabase.close();
        return delete;
    }
}
