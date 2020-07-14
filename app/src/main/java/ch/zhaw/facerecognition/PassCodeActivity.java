package ch.zhaw.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hanks.passcodeview.PasscodeView;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class PassCodeActivity extends AppCompatActivity  implements View.OnClickListener {

    public static final  int DELAY=10000;
    ImageView close;
    PasscodeView passcodeView;

    AdminDAO adminDAO ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        adminDAO = new AdminDAO(getApplicationContext());
        close = findViewById(R.id.icclose);
        passcodeView = findViewById(R.id.passcodeView);
        close.setOnClickListener(this);
        passcodeView.setLocalPasscode(adminDAO.getAdminPassword());
        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {

                Toasty.error(getApplicationContext(), R.string.pass_code_wrong,Toasty.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(String number) {


                Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.icclose){
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        /*Toasty.error(getApplicationContext(), R.string.auth_fail,Toasty.LENGTH_LONG).show();*/
        super.onBackPressed();
    }
}
