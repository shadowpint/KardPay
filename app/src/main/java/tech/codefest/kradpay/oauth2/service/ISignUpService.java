package tech.codefest.kradpay.oauth2.service;

import tech.codefest.kradpay.oauth2.request.SignUpRequest;
import tech.codefest.kradpay.oauth2.response.SignUpResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public interface ISignUpService {
    @POST("/user/signup")
    void signUp(@Body SignUpRequest signUpRequest,
                Callback<SignUpResponse> signUpResponseCallback);
}
