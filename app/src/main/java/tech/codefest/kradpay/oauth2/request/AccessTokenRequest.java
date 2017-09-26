package tech.codefest.kradpay.oauth2.request;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class AccessTokenRequest {

    private String username;
    private String password;
    private String client_id;
    private String client_secret;
    private String grant_type;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }


}
