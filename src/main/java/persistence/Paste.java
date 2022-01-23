package persistence;

import java.util.Date;

public class Paste {
    private String source;
    private String id;
    private String title;
    private Date date;
    private int emailCount;


    public Paste(String source, String id, String title, Date date, int emails) {
        this.source = source;
        this.id = id;
        this.title = title;
        this.date = date;
        this.emailCount = emails;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setEmailCount(int emailCount) {
        this.emailCount = emailCount;
    }

    public int getEmailCount() {
        return emailCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
