package edu.zyh.finalproject.POJO;

/**
 * 注册信息类
 * @author zyhsna
 */
public class RegisterInfo {
    private int userId;
    private String userName;
    private double progress;
    private String gender;
    private String telephone;
    private String password;
    private int userLevel;
    private int userRole;
    private String deviceUID;

    @Override
    public String toString() {
        return "RegisterInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", progress=" + progress +
                ", gender='" + gender + '\'' +
                ", telephone='" + telephone + '\'' +
                ", password='" + password + '\'' +
                ", userLevel=" + userLevel +
                ", userRole=" + userRole +
                ", deviceUID='" + deviceUID + '\'' +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getDeviceUID() {
        return deviceUID;
    }

    public void setDeviceUID(String deviceUID) {
        this.deviceUID = deviceUID;
    }
}
