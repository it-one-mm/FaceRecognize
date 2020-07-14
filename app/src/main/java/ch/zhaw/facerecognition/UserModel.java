package ch.zhaw.facerecognition;

public class UserModel {
    int UserId;
    String UserName;
    String faceId;

    public UserModel(int userId, String userName, String faceId) {
        UserId = userId;
        UserName = userName;
        this.faceId = faceId;
    }

    public UserModel(int userId, String userName) {
        UserId = userId;
        UserName = userName;
    }

    public UserModel() {

    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }
}
