package ch.zhaw.facerecognition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import es.dmoral.toasty.Toasty;

public class DatePopUp extends DialogFragment {
    String selectedDate;

    Button btndone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.datepicker,container,false);
        final DatePicker picker = myView.findViewById(R.id.datepicker);
        btndone = myView.findViewById(R.id.btndone);
        final SimpleDateFormat informat = new SimpleDateFormat(getString(R.string.in_format));
        final SimpleDateFormat outformat = new SimpleDateFormat(getString(R.string.out_format));
        final SimpleDateFormat dataFormat = new SimpleDateFormat(getString(R.string.dailyRecordFormat));
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedDate.equals("from"))
                {
                    String selected = picker.getDayOfMonth()+"/"+(picker.getMonth()+1)+"/"+picker.getYear();

                    try {
                        ExportDataActivity.fromDate.setText(outformat.format(informat.parse(selected)));
                        ExportDataActivity.txtfromDate=dataFormat.format(informat.parse(selected));
                    }
                    catch (Exception ex)
                    {
                        Toasty.error(getContext(),"Format Err",Toasty.LENGTH_LONG).show();
                    }
                }
                else if(selectedDate.equals("to"))
                {
                    String selected = picker.getDayOfMonth()+"/"+(picker.getMonth()+1)+"/"+picker.getYear();

                    try {
                        ExportDataActivity.toDate.setText(outformat.format(informat.parse(selected)));
                        ExportDataActivity.txttoDate=dataFormat.format(informat.parse(selected));
                    }
                    catch (Exception ex)
                    {
                        Toasty.error(getContext(),"Format Err",Toasty.LENGTH_LONG).show();
                    }
                }
                dismiss();
            }
        });
        return myView;
    }

    public String selectedDate(){

        return selectedDate;
    }

}
