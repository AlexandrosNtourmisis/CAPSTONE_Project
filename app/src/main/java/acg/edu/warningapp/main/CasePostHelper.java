package acg.edu.warningapp.main;

import java.sql.Timestamp;

//Getters and Setters to Post Case
public class CasePostHelper {
    String post_id, type, time, date, longitude, latitude, country, city, severity, comments, author, author_id, iconURL, searchvalue, details;
    Integer verifications;
    long timestamp;

    public CasePostHelper() {
    }

    public CasePostHelper(String post_id, String type, String time, String date, String longitude, String latitude, String country, String city, String severity, String comments, String author, String author_id, String iconURL, Integer verifications, String searchvalue, long timestamp, String details) {
        this.post_id = post_id;
        this.type = type;
        this.time = time;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.country = country;
        this.city = city;
        this.severity = severity;
        this.comments = comments;
        this.author = author;
        this.author_id = author_id;
        this.iconURL = iconURL;
        this.verifications = verifications;
        this.searchvalue = searchvalue;
        this.timestamp = timestamp;
        this.details = details;


    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getVerifications() {
        return verifications;
    }

    public void setVerifications(Integer verifications) {
        this.verifications = verifications;
    }

    public String getSearchvalue() {
        return searchvalue;
    }

    public void setSearchvalue(String searchvalue) {
        this.searchvalue = searchvalue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
