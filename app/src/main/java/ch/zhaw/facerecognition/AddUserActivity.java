package ch.zhaw.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import ch.zhaw.facerecognition.Activities.AddPersonPreviewActivity;
import ch.zhaw.facerecognition.Activities.TrainingActivity;
import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener{
    public static  int addface=0,saveface=0,svaedata;
    TextInputEditText edtName;
    private TextView dateTime;
    ImageView back;
    UserDAO userDAO;
    Button btnsave;
    Button btnsaveface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_user);
        //For Date//
        dateTime = findViewById(R.id.date_time);
        back=findViewById(R.id.back);
        edtName = findViewById(R.id.userNameData);
        back.setOnClickListener(this);
        btnsave = findViewById(R.id.btnsave);
        btnsaveface = findViewById(R.id.btnsaveface);
        btnsaveface.setEnabled(false);
        btnsave.setEnabled(false);
        btnsave.setOnClickListener(this);
        userDAO = new UserDAO(getApplicationContext());
        DateTimeUtils utils = new DateTimeUtils(getApplicationContext(),
                this,dateTime);
        utils.run();

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.back) {
            onBackPressed();
        }
        else if(v.getId()==R.id.btnsave){

           if(!edtName.getText().toString().trim().equals(""))
           {
               UserModel userModel = new UserModel();
               userModel.UserName=edtName.getText().toString().trim();
               userModel.faceId=userModel.UserName+"face";
               if(userDAO.saveUser(userModel)>0)
               {
                   /*Toasty.success(getApplicationContext(),"Save Success",Toasty.LENGTH_LONG).show();*/
                   SuccessPopUp popUp = new SuccessPopUp();
                   popUp.show(getSupportFragmentManager(),"Show Success");
                   edtName.setText("");
                   startActivity(new Intent(getApplicationContext(), TrainingActivity.class));



               }
           }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(addface==1) {
            btnsave.setEnabled(true);

        }

    }

    public void addFace(View v) {

        if(!edtName.getText().toString().trim().equals("")) {
            addface = 1;
            String name = edtName.getText().toString();
            Intent intent = new Intent(v.getContext(), AddPersonPreviewActivity.class);
            intent.putExtra("Name", name);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (isNameAlreadyUsed(new FileHelper().getTrainingList(), name)) {
                Toast.makeText(getApplicationContext(), "This name is already used. Please choose another one.", Toast.LENGTH_SHORT).show();
            } else {
                intent.putExtra("Folder", "Training");
                intent.putExtra("Method", AddPersonPreviewActivity.MANUALLY);
                startActivity(intent);

            }
        }

    }
    private boolean isNameAlreadyUsed(File[] list, String name){

        boolean used = false;
        if(list != null && list.length > 0){
            for(File person : list){
                // The last token is the name --> Folder name = Person name
                String[] tokens = person.getAbsolutePath().split("/");
                final String foldername = tokens[tokens.length-1];
                if(foldername.equals(name)){
                    used = true;
                    break;
                }
            }
        }
        return used;
    }

    public void saveFace(View view) {

    }
}
