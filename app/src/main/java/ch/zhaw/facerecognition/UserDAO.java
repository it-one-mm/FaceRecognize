package ch.zhaw.facerecognition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UserDAO {


    public static String TBName="UserTable",ColId="Id",ColName="Name",ColFaceId="faceId";
    DBHelper dbhelper;
    SQLiteDatabase db;


    public UserDAO(Context context) {
        dbhelper = new DBHelper(context);
        db = dbhelper.getWritableDatabase();
    }

    public long saveUser(UserModel model)
    {
        ContentValues values = new ContentValues();
        values.put(ColName,model.UserName);
        values.put(ColFaceId,model.faceId);
        return  db.insert(TBName,null,values);

    }

    public ArrayList<UserModel> getAllUsers()
    {
        Cursor cursor = db.rawQuery("select * from "+TBName,null);
        ArrayList<UserModel> userModels = new ArrayList<>();
        while (cursor.moveToNext())
        {
            userModels.add(
                  new UserModel(  cursor.getInt(cursor.getColumnIndex(ColId)),
                          cursor.getString(cursor.getColumnIndex(ColName)),
                          cursor.getString(cursor.getColumnIndex(ColFaceId)))
            );
        }
        return  userModels;
    }
    public UserModel getAllUserByName(String Name)
    {
        Cursor cursor = db.rawQuery("select * from "+TBName+" where "+ColName+"=?",new String[]{Name});
        UserModel userModel=new UserModel();
        while (cursor.moveToNext())
        {
                  userModel=new UserModel(  cursor.getInt(cursor.getColumnIndex(ColId)),
                          cursor.getString(cursor.getColumnIndex(ColName)),
                          cursor.getString(cursor.getColumnIndex(ColFaceId))
                  );

        }
        return  userModel;
    }

    public  long deleteUser (int Id)
    {
        String wherecluase = ColId+"=?";
        String whereArgs[] = new String[]{String.valueOf(Id)};
       long id =db.delete(TBName,wherecluase,whereArgs);
        return  id;
    }



}
