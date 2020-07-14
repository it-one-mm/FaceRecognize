package ch.zhaw.facerecognition;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {


    Context context;
    Activity activity;
    TextView dateTime;
    private String TAG="Date Time Utils";
    SimpleDateFormat format;

    public DateTimeUtils(Context context, Activity activity, TextView dateTime) {
        this.context = context;
        this.activity = activity;
        this.dateTime = dateTime;
        format = new SimpleDateFormat(context.getString(R.string.date_pattern));
    }

    public void run(){
        new MyThread().start();
    }


    class MyThread extends Thread{
        @Override
        public void run() {

            while (true) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dateTime.setText(format.format(new Date()));

                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    Log.e(TAG, "Thread Error");
                }
            }
        }
    }
}
