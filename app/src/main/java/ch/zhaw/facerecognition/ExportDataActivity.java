package ch.zhaw.facerecognition;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class ExportDataActivity extends AppCompatActivity implements View.OnClickListener{

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            sendFile();
            progressLoading.dismiss();
        }
    };

    private TextView dateTime;
    private ImageView back;
    AdminDAO adminDAO;
    public static TextView fromDate,toDate;
    ImageView frompicker,topicker;
    public  static String txtfromDate,txttoDate;
    Button btnsendNow;
    UserDAO userDAO;
    DailyRecordDAO recordDAO;
    ArrayList<String>cellHeader = new ArrayList<>();
    SimpleDateFormat outformat,infromat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);
        setContentView(R.layout.activity_export_data);
        adminDAO = new AdminDAO(getApplicationContext());
        //For Date//
        dateTime = findViewById(R.id.date_time);
        outformat = new SimpleDateFormat(getString(R.string.report_out_pattern));
        infromat = new SimpleDateFormat(getString(R.string.dailyRecordFormat));
        DateTimeUtils utils = new DateTimeUtils(getApplicationContext(),
                this,dateTime);
        utils.run();
        back=findViewById(R.id.back);
        userDAO = new UserDAO(getApplicationContext());
        recordDAO = new DailyRecordDAO(getApplicationContext());


        cellHeader.add("Employee Id");
        cellHeader.add("Name");
        cellHeader.add("Date");
        cellHeader.add("CheckIn");
        cellHeader.add("BreakOut");
        cellHeader.add("BreakIn");
        cellHeader.add("CheckOut");
        cellHeader.add("Total Time");
        cellHeader.add("Break Time");
        cellHeader.add("Work Time");

        back.setOnClickListener(this);
        fromDate = findViewById(R.id.fromdate);
        toDate = findViewById(R.id.todate);
        frompicker = findViewById(R.id.fromdatepicker);
        topicker = findViewById(R.id.todatepicker);
        frompicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePopUp popUp = new DatePopUp();
                popUp.selectedDate="from";
                popUp.show(getSupportFragmentManager(),"Choose Time");
            }
        });
        topicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePopUp popUp = new DatePopUp();
                popUp.selectedDate="to";
                popUp.show(getSupportFragmentManager(),"Choose Time");
            }
        });
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.out_format));
        toDate.setText(format.format(new Date()));
        fromDate.setText(format.format(new Date()));
        txtfromDate=new SimpleDateFormat(getString(R.string.dailyRecordFormat)).format(new Date());
        txttoDate=new SimpleDateFormat(getString(R.string.dailyRecordFormat)).format(new Date());
        btnsendNow = findViewById(R.id.sendReport);
        btnsendNow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back)
        {
            onBackPressed();
        }
        else
        {

            Workbook wb=new HSSFWorkbook();
            CellStyle cellStyle=wb.createCellStyle();
            CellStyle cellStyle2=wb.createCellStyle();
            Font defaultFont= wb.createFont();
            defaultFont.setFontHeightInPoints((short)10);
            defaultFont.setFontName("Arial");
            defaultFont.setColor(IndexedColors.BLACK.getIndex());

            defaultFont.setItalic(false);
            cellStyle2.setFont(defaultFont);




            ArrayList<UserModel> userModels = userDAO.getAllUsers();
            for(UserModel userModel : userModels)
            {

                ArrayList<ReportModel> reportModels = recordDAO.getAllReportsByRange(txtfromDate,txttoDate,userModel.UserId);

               if(reportModels.size()>0)
               {
                   Sheet sheet=null;
                   sheet = wb.createSheet(userModel.UserName);
                   Row row = sheet.createRow(0);

                   for(int j=0;j<cellHeader.size();j++)
                   {
                       Cell cell=row.createCell(j);
                       cell.setCellValue(cellHeader.get(j));
                       cell.setCellStyle(cellStyle2);
                   }
                   for(int i=0;i<reportModels.size();i++)
                   {
                       ReportModel tmp = reportModels.get(i);
                       Row datarow =sheet.createRow(i+1);

                       Cell cell1=datarow.createCell(0);
                       cell1.setCellValue(tmp.userId);
                       cell1.setCellStyle(cellStyle);

                       Cell cell2 = datarow.createCell(1);
                       cell2.setCellValue(tmp.Name);
                       cell2.setCellStyle(cellStyle);
                       Cell c3 = datarow.createCell(2);
                       try{
                       Date date = infromat.parse(tmp.Date);
                       tmp.Date = outformat.format(date);
                       c3.setCellValue(tmp.Date);
                       }
                       catch (Exception ex)
                       {
                           Toasty.error(getApplicationContext(),"Date Error",Toasty.LENGTH_LONG).show();
                       }
                       c3.setCellStyle(cellStyle);
                       Cell c4 = datarow.createCell(3);
                       c4.setCellValue(tmp.checkIn);
                       c4.setCellStyle(cellStyle);
                       Cell c5 = datarow.createCell(4);
                       c5.setCellValue(tmp.BreakOut);
                       c5.setCellStyle(cellStyle);
                       Cell c6 = datarow.createCell(5);
                       c6.setCellValue(tmp.BreakIn);
                       c6.setCellStyle(cellStyle);
                       Cell c7 = datarow.createCell(6);
                       c7.setCellValue(tmp.checkOut);

                       c7.setCellStyle(cellStyle);

                       String checkInParts[]= tmp.checkIn.split(":");
                       String checkOutParts[]= tmp.checkOut.split(":");

                       long checkInMin= (Integer.parseInt(checkInParts[0])*60)+(Integer.parseInt(checkInParts[1]));
                       long checkOutMin= (Integer.parseInt(checkOutParts[0])*60)+(Integer.parseInt(checkOutParts[1]));
                       long totalhour = checkOutMin-checkInMin;
                       Cell c8 = datarow.createCell(7);
                       String txttotalhour = totalhour/60+"hr"+totalhour%60+"min";
                       c8.setCellValue(txttotalhour);
                       c8.setCellStyle(cellStyle);
                       String BreakOutParts[]= tmp.BreakOut.split(":");
                       String BreakInParts[]= tmp.BreakIn.split(":");

                       long BreakOutMin= (Integer.parseInt(BreakOutParts[0])*60)+(Integer.parseInt(BreakOutParts[1]));
                       long BreakInMin= (Integer.parseInt(BreakInParts[0])*60)+(Integer.parseInt(BreakInParts[1]));
                       long totalbreak = BreakInMin-BreakOutMin;
                       Cell c9 = datarow.createCell(8);
                       String txttotalbreak= totalbreak/60+"hr"+totalbreak%60+"min";
                       c9.setCellValue(txttotalbreak);
                       c9.setCellStyle(cellStyle);

                       long totalWorking = totalhour-totalbreak;

                       String txttotalworking = totalWorking/60+"hr"+totalWorking%60+"min";
                       Cell c10 = datarow.createCell(9);
                       c10.setCellValue(txttotalworking);
                       c10.setCellStyle(cellStyle);



                   }
               }
            }

            /*File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
            ),"Report.xls");
            FileOutputStream outputStream =null;

            try {
                outputStream=new FileOutputStream(file);
                wb.write(outputStream);

            } catch (java.io.IOException e) {
                e.printStackTrace();

            }*/
            new CreateFile().execute(wb);
            handler.postDelayed(r,10000);


        }




    }
    Multipart _multipart;
    public void sendFile()
    {
        _multipart = new MimeMultipart();

        final String sEmail = "itonemm@gmail.com";
        final String sPassword="mtn$5797";

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail,sPassword);
            }
        });
        Message message = new MimeMessage(session);
        try{
            message.setFrom(new InternetAddress(sEmail));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(
                    adminDAO.getAdmin().email));
            message.setSubject("Attendance Report ( "+txtfromDate+"-"+txttoDate+")");
            message.setText("Reporting");

            String filename = "/storage/emulated/0/download/"+"Report("+ fromDate.getText()+"-"+toDate.getText()+").xls";

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("This is message body");
            messageBodyPart.setDescription("Hey Welcome");
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("Attendance Report ( "+txtfromDate+"-"+txttoDate+").xls");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

            message.setContent(multipart);
            new SendMail().execute(message);

        }
        catch (Exception ex)
        {

            Log.e("Error",ex.getMessage());
        }
    }
    ProgressDialog progressLoading;
    public class CreateFile extends AsyncTask<Workbook,String,String>
    {
        String fromdate,todate;


        @Override
        protected void onPreExecute() {
            fromdate = fromDate.getText().toString();
            todate = toDate.getText().toString();
            progressLoading = ProgressDialog.show(ExportDataActivity.this,"Please Wait","Creating File",true);



        }

        @Override
        protected String doInBackground(Workbook... workbooks) {
            Workbook wb = workbooks[0];
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
            ),"Report("+fromdate+"-"+todate+").xls");
            FileOutputStream outputStream =null;

            try {
                outputStream=new FileOutputStream(file);
                wb.write(outputStream);
                return  "OK";
            } catch (IOException e) {
                e.printStackTrace();
                return "Opps";
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("OK"))
            {
              /*  Toasty.success(ExportDataActivity.this,"Success",Toasty.LENGTH_LONG).show();
*/
            }
            else {

                Toasty.error(ExportDataActivity.this,"Error",Toasty.LENGTH_LONG).show();
            }
        }
    }

    private class SendMail extends AsyncTask<Message,String,String>
    {

        ProgressDialog progressDialog ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ExportDataActivity.this,"Please Wait","Sending Mail",true);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            }
            catch (Exception ex)
            {
                return  "Error";
            }


        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();;
            if(s.equals("Success"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExportDataActivity.this);
                builder.setTitle("Success");
                builder.setMessage("Email Sent Success");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.show();
            }
            else {
                Toast.makeText(ExportDataActivity.this,"Sending Error",Toast.LENGTH_LONG).show();

            }
        }
    }

}


