package tech.codefest.kradpay.oauth2.response;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public abstract class BaseResponse {

    private String error = null;
    private String error_description = null;

    public String getError() {
        return error;
    }

    public String getError_description() {

        return error_description;
    }
}
