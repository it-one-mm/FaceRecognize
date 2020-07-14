package ch.zhaw.facerecognition;

public class AdminModel
{
    int Id,timeout;
    String email,password;

    public AdminModel(int id, int timeout, String email, String password) {
        Id = id;
        this.timeout = timeout;
        this.email = email;
        this.password = password;
    }

    public AdminModel() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
