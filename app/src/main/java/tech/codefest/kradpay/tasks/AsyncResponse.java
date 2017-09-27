package tech.codefest.kradpay.tasks;

/**
 * Created by DELL on 9/27/2017.
 */

public interface AsyncResponse {
    void transactionFinish(String output);
    void lastTransactionResult(String output);
    void cartsaveResult(String output);
}
