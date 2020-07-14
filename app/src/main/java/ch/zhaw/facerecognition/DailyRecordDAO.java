package ch.zhaw.facerecognition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DailyRecordDAO {
    Context context;
    String ColId="Id",
            ColUserId="UserId",
            ColCheckIn="CheckIn",ColBreakIn="BreakIn",ColBreakOut="BreakOut",ColDate="Date",
            ColCheckOut="CheckOut",TBName="DailyRecord";
    SimpleDateFormat dailyformat;
    SimpleDateFormat dateFormat;
    DBHelper dbHelper;
    SQLiteDatabase db;
    public DailyRecordDAO(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        dateFormat = new SimpleDateFormat(context.getString(R.string.checkInDatePattern));
        dailyformat = new SimpleDateFormat(context.getString(R.string.dailyRecordFormat));;
    }

    public long insertCheckIn(int UserId)
    {

        ContentValues values = new ContentValues();
        values.put(ColCheckIn,dateFormat.format(new Date()));
        values.put(ColUserId,UserId);
        values.put(ColDate,dailyformat.format(new Date()));
        return db.insert(TBName,null,values);
    }

    public long insertCheckOut(int UserId)
    {
        ContentValues values = new ContentValues();
        values.put(ColCheckOut,dateFormat.format(new Date()));
        String selection = ColUserId+"=? and "+ColDate+"=?";
        String selectionArgs[] = new String[]{
                String.valueOf(UserId),
                dailyformat.format(new Date())
        };
        return db.update(TBName,values,selection,selectionArgs);
    }
    public long insertBreakIn(int UserId)
    {
        ContentValues values = new ContentValues();
        values.put(ColBreakIn,dateFormat.format(new Date()));
        String selection = ColUserId+"=? and "+ColDate+"=?";
        String selectionArgs[] = new String[]{
                String.valueOf(UserId),
                dailyformat.format(new Date())
        };
        return db.update(TBName,values,selection,selectionArgs);
    }
    public long insertBreakOut(int UserId)
    {
        ContentValues values = new ContentValues();
        values.put(ColBreakOut,dateFormat.format(new Date()));
        String selection = ColUserId+"=? and "+ColDate+"=?";
        String selectionArgs[] = new String[]{
                String.valueOf(UserId),
                dailyformat.format(new Date())
        };
        long id= db.update(TBName,values,selection,selectionArgs);
        return  id;
    }

    public boolean isCheckIn(int UserId)
    {
        int count=0;
        String sql="select count (*) from DailyRecord where UserId="+UserId+" and Date='"+dailyformat.format(new Date())+"'";
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext())
        {
            count = c.getInt(0);

        }
        if(count>0)
        {
            return true;
        }
        else
        {
            return  false;
        }
    }
    public boolean isBreakIn(int UserId)
    {
        int count=0;
        String sql="select count (*) from DailyRecord where UserId="+UserId+" and Date='"+dailyformat.format(new Date())+"'"+" and "+ColBreakIn+"!='null'";
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext())
        {
            count = c.getInt(0);

        }
        if(count>=0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public boolean isCheckOut(int UserId) {

        int count=0;
        String sql="select count (*) from DailyRecord where UserId="+UserId+" and Date='"+dailyformat.format(new Date())+"' and "+ColCheckOut+"!='null'";
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext())
        {
            count = c.getInt(0);

        }
        if(count>0)
        {
            return true;
        }
        else
        {
            return  false;
        }
    }

    public boolean isBreakOut(int UserId) {

        int count=0;
        String sql="select count (*) from DailyRecord where "+ColUserId+"="+UserId+" and Date='"+dailyformat.format(new Date())+"' and "+ColBreakOut+"!='null'";
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext())
        {
            count = c.getInt(0);

        }
        if(count>0)
        {
            return true;
        }
        else
        {
            return  false;
        }
    }

    public ArrayList<ReportModel> getAllReportsByRange(String fromdate, String todate,int UserId)
    {
        ArrayList<ReportModel> reportModels  = new ArrayList<>();
        String sql="select  DailyRecord.UserId,UserTable.Name,CheckIn,BreakOut,BreakIn,CheckOut,Date FROM DailyRecord,UserTable WHERE DailyRecord.UserId=UserTable.Id and DailyRecord.Date>='"+fromdate+
                "' and DailyRecord.Date<= '"+todate+"' and UserTable.Id="+UserId;
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext())
        {
            ReportModel tmp = new ReportModel();
            tmp.userId=cursor.getInt(0);
            tmp.Name = cursor.getString(1);
            tmp.checkIn = cursor.getString(2);
            tmp.BreakOut = cursor.getString(3);
            tmp.BreakIn = cursor.getString(4);
            tmp.checkOut= cursor.getString(5);
            tmp.Date =cursor.getString(6);
            reportModels.add(tmp);

        }
        return reportModels;
    }
    public ArrayList<UserModel> getAbsentUsers(String date)
    {
        String sql="select DISTINCT Id,Name from UserTable EXCEPT " +
                "select UserTable.Id,UserTable.Name from UserTable,DailyRecord where Date='"+date+"' and UserTable.Id = DailyRecord.UserId";
        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<UserModel> userModels = new ArrayList<>();
        while (cursor.moveToNext())
        {
            userModels.add(new
                    UserModel(
                            cursor.getInt(0),
                    cursor.getString(1)
                    )
            );
        }
        return userModels;
    }

}
