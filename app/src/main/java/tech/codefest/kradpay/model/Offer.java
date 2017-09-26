package tech.codefest.kradpay.model;

/**
 * Created by Neeraj on 8/09/17.
 */
public class Offer {
    private String id;
    private String image;
    private String title;
    private String detail;
    private String redeem;
    private String percent;



    public Offer(String id, String image, String title, String detail, String redeem, String percent) {
        this.id = id;

        this.image = image;
        this.title=title;
        this.detail=detail;
        this.redeem= redeem;
        this.percent=percent;

    }

    public Offer() {

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
 
    public String getRedeem() {
        return redeem;
    }
    public void setRedeem(String redeem) {
        this.redeem = redeem;
    }
    public String getPercent() {
        return percent;
    }
    public void setPercent(String percent) {
        this.percent = percent;
    }
   
}
