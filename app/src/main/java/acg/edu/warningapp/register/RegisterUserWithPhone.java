package acg.edu.warningapp.register;

//HELPER CLASS for registration
public class RegisterUserWithPhone {


    String fname, lname, email, phoneNo, password, age, country, notifications, safety_radius;
    Integer reports;
    boolean first_warning, final_warning, first_message, ban_message;


    public RegisterUserWithPhone(){}

    public RegisterUserWithPhone(String fname, String lname, String email, String phoneNo, String password, String age, String country, Integer reports, boolean first_warning, boolean final_warning, boolean first_message, boolean ban_message, String notifications, String safety_radius) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        this.age = age;
        this.country = country;
        this.reports = reports;
        this.first_warning = first_warning;
        this.final_warning = final_warning;
        this.first_message = ban_message;
        this.notifications = notifications;
        this.safety_radius = safety_radius;



    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getReports() {
        return reports;
    }

    public void setReports(Integer reports) {
        this.reports = reports;
    }

    public boolean isFirst_warning() {
        return first_warning;
    }

    public void setFirst_warning(boolean first_warning) {
        this.first_warning = first_warning;
    }

    public boolean isFinal_warning() {
        return final_warning;
    }

    public void setFinal_warning(boolean final_warning) {
        this.final_warning = final_warning;
    }

    public boolean isFirst_message() {
        return first_message;
    }

    public void setFirst_message(boolean first_message) {
        this.first_message = first_message;
    }

    public boolean isBan_message() {
        return ban_message;
    }

    public void setBan_message(boolean ban_message) {
        this.ban_message = ban_message;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }

    public String getSafety_radius() {
        return safety_radius;
    }

    public void setSafety_radius(String safety_radius) {
        this.safety_radius = safety_radius;
    }
}
