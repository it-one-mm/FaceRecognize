package ch.zhaw.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {



    TextView logout;
    TextView dateTime;
    Button btnaddUser,btndeleteUser,btnexportData,btnSetting;
    Intent addUserIntent;
    private Intent deleteUserIntent;
    private Intent exportDataIntent;
    private Intent settingIntent;
    private  Intent adminIntent;
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin);
        logout = findViewById(R.id.logouttxt);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        btnaddUser=findViewById(R.id.btnadduser);
        btndeleteUser = findViewById(R.id.btndeleteuser);
        btnexportData = findViewById(R.id.btnexportdata);
        btnSetting = findViewById(R.id.btnsetting);
        btnaddUser.setOnClickListener(this);
        btndeleteUser.setOnClickListener(this);
        btnexportData.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        addUserIntent = new Intent(getApplicationContext(),AddUserActivity.class);
        deleteUserIntent = new Intent(getApplicationContext(),DeleteUserActivity.class);
        exportDataIntent = new Intent(getApplicationContext(),ExportDataActivity.class);
        settingIntent = new Intent(getApplicationContext(),SettingActivity.class);
        adminIntent = new Intent(getApplicationContext(),AdminActivity.class);
        mainIntent = new Intent(getApplicationContext(),MainActivity.class);

        //For Date//
        dateTime = findViewById(R.id.date_time);
        DateTimeUtils utils = new DateTimeUtils(getApplicationContext(),
                this,dateTime);
        utils.run();

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnadduser)
        {

            startActivity(addUserIntent);
        }
       else if(v.getId()==R.id.btndeleteuser)
        {

            startActivity(deleteUserIntent);
        }
        else if(v.getId()==R.id.btnexportdata)
        {

            startActivity(exportDataIntent);
        }


        else if(v.getId()==R.id.btnsetting){

            startActivity(settingIntent);
        }
    }

    @Override
    public void onBackPressed() {
       startActivity(adminIntent);
    }
}
