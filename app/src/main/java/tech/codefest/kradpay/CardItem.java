package tech.codefest.kradpay;

/**
 * Created by Neeraj on 8/09/17.
 */
public class CardItem {
    private String id;
    private String title;
    private String url;
    private String publisher;
    private String category;
    private String hostname;
    private String timestamp;


    public CardItem(String id, String title, String url, String publisher, String category, String hostname, String timestamp) {
        this.id = id;

        this.title = title;
        this.url=url;
        this.publisher=publisher;
        this.category= category;
        this.hostname=hostname;
        this.timestamp=timestamp;
    }

    public CardItem() {

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
