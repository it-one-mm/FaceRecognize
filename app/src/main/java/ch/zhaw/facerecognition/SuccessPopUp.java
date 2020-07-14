package ch.zhaw.facerecognition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SuccessPopUp  extends DialogFragment {

    Button btnok;
   TextView text;
   String content;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.usercreateddialog,container,false);
        btnok = myView.findViewById(R.id.btnok);
        text = myView.findViewById(R.id.text);
        if(content!=null)
        {
            text.setText(content);
        }
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();;
            }
        });
        return  myView;
    }
}
