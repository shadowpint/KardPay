package tech.codefest.kradpay.oauth2.response;

/**
 * Created by Neeraj on 8/09/17.
 */
public class News {
    private String id;
    private String title;
    private String url;
    private String publisher;
    private String category;
    private String hostname;
    private String timestamp;


    public News(String id, String title, String url,  String publisher, String category, String hostname,String timestamp) {
        this.id = id;

        this.title = title;
        this.url=url;
        this.publisher=publisher;
        this.category= category;
        this.hostname=hostname;
        this.timestamp=timestamp;
    }

    public News() {

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
 
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
