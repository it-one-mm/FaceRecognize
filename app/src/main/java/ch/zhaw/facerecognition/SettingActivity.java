package ch.zhaw.facerecognition;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView dateTime;
    private ImageView back;
    TextInputEditText email,timeout;
    AdminDAO adminDAO;

    Button btnsave;
    AdminModel adminModel;
    Button changepw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);//For Date//
        dateTime = findViewById(R.id.date_time);
        DateTimeUtils utils = new DateTimeUtils(getApplicationContext(),
                this,dateTime);
        utils.run();
        back=findViewById(R.id.back);
        back.setOnClickListener(this);
        changepw=findViewById(R.id.changepassword);
        adminDAO = new AdminDAO(getApplicationContext());
        email = findViewById(R.id.edtemail);
        timeout = findViewById(R.id.edttimeout);
        changepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePw pw = new ChangePw();
                pw.show(getSupportFragmentManager(),"Chane Password");
            }
        });
        adminModel=adminDAO.getAdmin();
        email.setText(adminModel.getEmail());
        timeout.setText(adminModel.getTimeout()+"");
        btnsave = findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminModel newAdmin = adminDAO.getAdmin();
                if(!adminModel.email.equals(email.getText().toString().trim()))
                {

                    newAdmin.email=email.getText().toString().trim();

                }
                if(adminModel.timeout!=Integer.parseInt(timeout.getText().toString().trim()))
                {
                    newAdmin.timeout= Integer.parseInt(timeout.getText().toString().trim());
                }
                adminDAO.updateEmail(newAdmin.email);
                adminDAO.updateTimeOut(newAdmin.timeout);

                Toasty.success(getApplicationContext(),"Save Success",Toasty.LENGTH_LONG).show();

            }
        });



    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
