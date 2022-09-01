package acg.edu.warningapp.home;

// Setters and getters for comments ListView
public class CommentsListViewHelper {

    private String comment;
    private String commenter;
    private String commenter_id;
    private String time;
    private String date;
    private String country;

    public CommentsListViewHelper( String comment_,  String commenter_,String commenter_id_,  String time_, String date_, String country_) {

        comment = comment_;
        commenter = commenter_;
        commenter_id = commenter_id_;
        time = time_;
        date = date_;
        country = country_;
    }

    public CommentsListViewHelper(){

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getCommenter_id() {
        return commenter_id;
    }

    public void setCommenter_id(String commenter_id) {
        this.commenter_id = commenter_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
