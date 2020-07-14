package ch.zhaw.facerecognition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;

public class ChangePw  extends BottomSheetDialogFragment {
    Button btnsave;
    ImageView close;
    TextInputEditText edtOldPw,edtNewPw;
    AdminModel adminModel;
    AdminDAO adminDAO;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.changepassword,container,false);
        btnsave = myView.findViewById(R.id.btnsave);
        close = myView.findViewById(R.id.close);
        edtOldPw = myView.findViewById(R.id.edtoldpassword);
        edtNewPw = myView.findViewById(R.id.edtnewpassword);
        adminDAO = new AdminDAO(getContext());
        adminModel = adminDAO.getAdmin();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();;
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpw = edtOldPw.getText().toString().trim();
                String newpw = edtNewPw.getText().toString().trim();
                if(!oldpw.equals(newpw))
                {
                    if(oldpw.equals(adminModel.getPassword()))
                    {
                        adminDAO.updatePassword(newpw);
                        dismiss();
                        Toasty.success(getContext(),"Password Updated",Toasty.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toasty.error(getContext(),"Old Password Wrong",Toasty.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toasty.error(getContext(),"Same Old Password and New Password",Toasty.LENGTH_LONG).show();
                }
            }
        });
        return  myView;
    }
}
