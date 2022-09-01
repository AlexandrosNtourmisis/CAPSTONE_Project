package acg.edu.warningapp.home;

public class Cases {

    private String city;
    private String country;
    private String iconURL;
    private String time;
    private String type;
    private String date;


    //Setters and getters for displaying the cases in other classes
    public Cases(String city_, String country_, String iconURL_, String time_, String type_, String date_) {

        city = city_;
        country = country_;
        iconURL = iconURL_;
        time = time_;
        type = type_;
        date = date_;
    }

    public Cases() {

    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

