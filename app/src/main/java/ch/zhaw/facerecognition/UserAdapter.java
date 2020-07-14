package ch.zhaw.facerecognition;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    UserDAO userDAO;
    FragmentManager fm;
    ArrayList<UserModel> userModels = new ArrayList<>();
    Context context;
    Activity activity;

    public UserAdapter(ArrayList<UserModel> userModels, Context context, FragmentManager fm,Activity activity) {
        this.userModels = userModels;
        this.context = context;
        userDAO = new UserDAO(context);
        this.fm = fm;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View myView = inflater.inflate(R.layout.user_list_item,parent,false);
        return  new UserHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {

        holder.userId.setText(position+1+"");
        holder.userName.setText(userModels.get(position).UserName);
        holder.userDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Confirmation!");
                builder.setMessage("Are you sure To Exit?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if( userDAO.deleteUser(userModels.get(position).UserId)>0) {
                           SuccessPopUp popUp = new SuccessPopUp();
                           popUp.content = "User Deleted";
                           popUp.show(fm, "User Deleted");
                           DeleteUserActivity.loadData();
                       }

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder{
        TextView userId,userName;
        ImageView userDelete;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            userId = itemView.findViewById(R.id.userId);
            userName = itemView.findViewById(R.id.userName);
            userDelete = itemView.findViewById(R.id.userDelete);
        }
    }
}
