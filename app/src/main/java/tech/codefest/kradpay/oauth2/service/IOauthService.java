package tech.codefest.kradpay.oauth2.service;

import tech.codefest.kradpay.oauth2.request.AccessTokenRequest;
import tech.codefest.kradpay.oauth2.response.AccessTokenResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by dominicneeraj on 08/08/17.
 */

public interface IOauthService {

    @POST("/auth/token/")
    void getAccessToken(@Body AccessTokenRequest accessTokenRequest,
                        Callback<AccessTokenResponse> responseCallback);


}
