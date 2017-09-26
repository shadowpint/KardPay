package tech.codefest.kradpay.oauth2.response;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class AccessTokenResponse extends BaseResponse {

    private String access_token;
    private String refresh_token;
    private int expires_in;
    private String token_type;

    public String getAccess_token() {
            return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getToken_type() {
        return token_type;
    }


    @Override
    public String toString() {

        if (super.getError() != null && super.getError_description() != null) {
            return super.getError() + super.getError_description();
        }
        return "AccessToken{" +
                "accessToken='" + access_token + '\'' +
                ", tokenType='" + token_type + '\'' +
                ", expiresIn=" + expires_in +
                ", refreshToken='" + refresh_token + '\'' +
                '}';
    }
}
