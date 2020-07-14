package ch.zhaw.facerecognition;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Calendar;

import androidx.annotation.NonNull;
import ch.zhaw.facerecognition.Activities.RecognitionActivity;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int RQ_CODE =9000 ;
    TextView dateTime;

    UserDAO userDAO;
    DailyRecordDAO dao;
    LinearLayout checkIn;
    LinearLayout checkOut;
    LinearLayout breakIn;
    LinearLayout breakOut;
    private String TAG="Main Activity";
    ImageView adminLogIn;

    public static String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        checkPermission();
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, SendAbsentReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
         dateTime = findViewById(R.id.date_time);
         adminLogIn = findViewById(R.id.adminLogIn);
        adminLogIn.setOnClickListener(this);
        userDAO = new UserDAO(getApplicationContext());
        //For Time //
        final DateTimeUtils utils = new DateTimeUtils(getApplicationContext(),
                this,dateTime);
        utils.run();
        checkIn = findViewById(R.id.checkIn);
        checkOut = findViewById(R.id.checkOut);
        breakOut = findViewById(R.id.breakout);
        breakIn = findViewById(R.id.breakIn);
        dao = new DailyRecordDAO(getApplicationContext());

    }

    UserModel user=new UserModel();
    @Override
    protected void onResume() {
        super.onResume();
        try {
            checkIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), RecognitionActivity.class));
                   /* user = userDAO.getAllUserByName(UserName);
                    try {
                        if (!dao.isCheckIn(user.UserId)
                        ) {

                            if (dao.insertCheckIn(user.UserId) > 0) {
                                Toasty.success(getApplicationContext(), "Check In Success", Toasty.LENGTH_LONG).show();
                            }
                        } else {
                            Toasty.error(getApplicationContext(), "Already Check In", Toasty.LENGTH_LONG).show();
                            ;
                        }
                    } catch (Exception ex) {
                        Toasty.error(getApplicationContext(), "No User and Can't CheckIN", Toasty.LENGTH_LONG).show();
                    }*/
                }
            });

            checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dao.isCheckIn(user.UserId) &&
                            !dao.isCheckOut(user.UserId)) {
                        if (dao.insertCheckOut(user.UserId) > 0) {
                            Toasty.success(getApplicationContext(), "Check Ouf Success", Toasty.LENGTH_LONG).show();
                        }

                    } else {
                        Toasty.error(getApplicationContext(), "You Can't Check Out!", Toasty.LENGTH_LONG).show();


                    }
                }
            });
            breakIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dao.isCheckIn(user.UserId) &&
                            dao.isBreakOut(user.UserId)
                            && !dao.isCheckOut(user.getUserId())
                            && !dao.isBreakIn(user.getUserId())) {
                        if (dao.insertBreakIn(user.UserId) > 0) {
                            Toasty.success(getApplicationContext(), "Break In Success", Toasty.LENGTH_LONG).show();
                        }

                    } else {
                        Toasty.error(getApplicationContext(), "Can't Break In!", Toasty.LENGTH_LONG).show();
                    }
                }
            });
            breakOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (dao.isCheckIn(user.UserId) &&
                            !dao.isCheckOut(user.UserId)
                            && !dao.isBreakIn(user.UserId)
                            && !dao.isBreakOut(user.UserId)) {
                        if (dao.insertBreakOut(user.UserId) > 0) {
                            Toasty.success(getApplicationContext(), "Break Out Success", Toasty.LENGTH_LONG).show();
                            ;
                        }

                    } else {
                        Toasty.error(getApplicationContext(), "Can't Break Out", Toasty.LENGTH_LONG).show();
                        ;
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Toasty.error(getApplicationContext(),"Hello",Toasty.LENGTH_LONG).show();
        }
    }

    public void checkPermission()
    {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RQ_CODE );

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==RQ_CODE)
        {
            Toasty.success(getApplicationContext(),"Success",Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.adminLogIn)
        {
            Intent intent = new Intent(getApplicationContext(),PassCodeActivity.class);
            startActivity(intent);
        }
    }
}
