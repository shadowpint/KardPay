package tech.codefest.kradpay.model;

/**
 * Created by Neeraj on 8/09/17.
 */
public class Transaction {
    private String id;
    private String tr_id;
    private String amount;
    private String item;
    private String reward;
    private String tr_date;
   


    public Transaction(String id, String tr_id, String amount, String item, String reward, String tr_date) {
        this.id = id;

        this.tr_id = tr_id;
        this.amount=amount;
        this.item=item;
        this.reward= reward;
        this.tr_date=tr_date;
     
    }

    public Transaction() {

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTr_id() {
        return tr_id;
    }
    public void setTr_id(String tr_id) {
        this.tr_id = tr_id;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }
 
    public String getReward() {
        return reward;
    }
    public void setReward(String reward) {
        this.reward = reward;
    }
    public String getTr_date() {
        return tr_date;
    }
    public void setTr_date(String tr_date) {
        this.tr_date = tr_date;
    }
   
}
