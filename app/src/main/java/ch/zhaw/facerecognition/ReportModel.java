package ch.zhaw.facerecognition;

public class ReportModel {
    int userId;
    String Name,checkIn,checkOut,BreakIn,BreakOut,Date;

    public ReportModel() {
    }

    public ReportModel(int userId, String name, String checkIn, String checkOut, String breakIn, String breakOut) {
        this.userId = userId;
        Name = name;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        BreakIn = breakIn;
        BreakOut = breakOut;
    }
}
