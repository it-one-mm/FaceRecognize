package ch.zhaw.facerecognition;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeleteUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView dateTime;
    private ImageView back;
    static RecyclerView rcList;
    static UserDAO userDAO;
    static Context context;
    static Activity activity;
    static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delete_user);
        //For Date//
        dateTime = findViewById(R.id.date_time);
        DateTimeUtils utils = new DateTimeUtils(getApplicationContext(),
                this,dateTime);
        utils.run();
        back=findViewById(R.id.back);
        rcList = findViewById(R.id.rclist);

        back.setOnClickListener(this);
        context = getApplicationContext();
        userDAO = new UserDAO(context);
        activity = this;
        fm = getSupportFragmentManager();
        loadData();


    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    public static void loadData()
    {

        ArrayList<UserModel> userModels = userDAO.getAllUsers();

        UserAdapter adapter = new UserAdapter(userModels,context,fm,activity);
        rcList.setAdapter(adapter);
        rcList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

    }
}
