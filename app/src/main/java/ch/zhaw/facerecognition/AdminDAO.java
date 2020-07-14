package ch.zhaw.facerecognition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AdminDAO {

    Context context;
    SQLiteDatabase db;
    public String TbName="AdminInfo",ColEmail="Email",ColTimeOut="TimeOutDuration",ColPassword="Password",ColId="Id";

    public AdminDAO(Context context) {
        this.context = context;
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public String getAdminPassword()
    {
        String password="";
        Cursor cursor = db.rawQuery("select "+ColPassword+" from "+TbName+" where "+ColId+"=1",null);
        while (cursor.moveToNext())
        {
            password= cursor.getString(cursor.getColumnIndex(ColPassword));

        }
        return  password;

    }


    public long updatePassword(String updatePassword)
    {
        ContentValues values = new ContentValues();
        values.put(ColPassword,updatePassword);
       long count = db.update(TbName,values,null,null);
       return  count;
    }
    public long updateEmail(String updateEmail)
    {
        ContentValues values = new ContentValues();
        values.put(ColEmail,updateEmail);
       long count = db.update(TbName,values,null,null);
       return  count;
    }
    public long updateTimeOut(int timeOut)
    {
        ContentValues values = new ContentValues();
        values.put(ColTimeOut,timeOut);
       long count = db.update(TbName,values,null,null);
       return  count;
    }


    public AdminModel getAdmin()
    {
        AdminModel adminModel = new AdminModel();
        Cursor cursor = db.rawQuery("select * from "+TbName,null);
        while (cursor.moveToNext())
        {

            adminModel.Id =cursor.getInt(cursor.getColumnIndex(ColId));
            adminModel.email = cursor.getString(cursor.getColumnIndex(ColEmail));
            adminModel.password = cursor.getString(cursor.getColumnIndex(ColPassword));
            adminModel.timeout = cursor.getInt(cursor.getColumnIndex(ColTimeOut));
        }
        return  adminModel;

    }


}
