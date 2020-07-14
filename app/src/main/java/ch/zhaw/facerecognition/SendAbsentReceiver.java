package ch.zhaw.facerecognition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
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

public class SendAbsentReceiver  extends BroadcastReceiver {

    AdminDAO adminDAO;
    DailyRecordDAO recordDAO;
    SimpleDateFormat format;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            initMail();
        }
    };
    @Override
    public void onReceive(Context context, Intent intent) {

        adminDAO = new AdminDAO(context);
        recordDAO = new DailyRecordDAO(context);
      format = new SimpleDateFormat(context.getString(R.string.dailyRecordFormat));
        Workbook wb=new HSSFWorkbook();
        CellStyle cellStyle=wb.createCellStyle();
        CellStyle cellStyle2=wb.createCellStyle();
        Font defaultFont= wb.createFont();
        defaultFont.setFontHeightInPoints((short)10);
        defaultFont.setFontName("Arial");
        defaultFont.setColor(IndexedColors.BLACK.getIndex());
        /*defaultFont.setBold(true);*/
        defaultFont.setItalic(false);
        cellStyle2.setFont(defaultFont);

        Sheet sheet = wb.createSheet("Absent List"+format.format(new Date()));
        Row headerRow=sheet.createRow(0);

        Cell cell1=headerRow.createCell(0);
        cell1.setCellValue("Employee Id");
        cell1.setCellStyle(cellStyle2);

        Cell cell2 = headerRow.createCell(1);
        cell2.setCellValue("Employee Name");
        cell2.setCellStyle(cellStyle2);

        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.dailyRecordFormat));
        ArrayList<UserModel> absentModels = recordDAO.getAbsentUsers(format.format(new Date()));

        if(absentModels.size()>0) {
            for (int i = 0; i < absentModels.size(); i++) {
                UserModel tmp = absentModels.get(i);
                Row row = sheet.createRow(i + 1);
                Cell c1 = row.createCell(0);
                c1.setCellValue(tmp.UserId);

                c1.setCellStyle(cellStyle);
                Cell c2 = row.createCell(1);
                c2.setCellValue(tmp.UserName);
                c2.setCellStyle(cellStyle);
            }

        }
        new CreateFile().execute(wb);
        handler.postDelayed(runnable,10000);


    }

    public void initMail()
    {
        final String sEmail = "itonemm@gmail.com";
        final String sPassword="9111990@gmail.com";
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
                    adminDAO.getAdmin().email
            ));
            message.setSubject("AbsentList"+format.format(new Date()));
            String fileName = "/storage/emulated/0/download/"+"/AbsentReport"+format.format(new Date())+".xls";

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Absent List");
            DataSource source = new FileDataSource(fileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("AbsentList"+format.format(new Date())+".xls");
            message.setFileName(fileName);
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
    private class SendMail extends AsyncTask<Message,String,String>
    {



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



    }


    public class CreateFile extends AsyncTask<Workbook,String,String>
    {





        @Override
        protected String doInBackground(Workbook... workbooks) {
            Workbook wb = workbooks[0];
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
            ),"AbsentReport"+format.format(new Date())+".xls");
            FileOutputStream outputStream =null;

            try {
                outputStream=new FileOutputStream(file);
                wb.write(outputStream);
                return  "OK";
            } catch (java.io.IOException e) {
                e.printStackTrace();
                return "Opps";
            }


        }


    }
}