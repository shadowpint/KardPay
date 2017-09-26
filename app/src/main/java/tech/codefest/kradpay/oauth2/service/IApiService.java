package tech.codefest.kradpay.oauth2.service;

import tech.codefest.kradpay.oauth2.request.ApiRequest;
import tech.codefest.kradpay.oauth2.response.ApiResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public interface IApiService {


    @Headers({
            "Content-type : application/json",
            "Authorization: Bearer vZFxTvBVMD9zTZdCiAX2B2iVRDyynM"
    })
    @POST("/recommend/api/rating/")
    void Api(@Body ApiRequest apiRequest,
             Callback<ApiResponse> ResponseCallback);


}
