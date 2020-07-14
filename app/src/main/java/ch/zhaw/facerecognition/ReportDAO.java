package ch.zhaw.facerecognition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ReportDAO  {
    Context context;


    SQLiteDatabase db;
    public ReportDAO(Context context) {
        this.context = context;
        DBHelper helper = new DBHelper(context);
        db=helper.getWritableDatabase();
    }



}
