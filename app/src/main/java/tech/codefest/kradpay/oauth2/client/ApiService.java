package tech.codefest.kradpay.oauth2.client;

import tech.codefest.kradpay.oauth2.constant.OauthConstant;
import tech.codefest.kradpay.oauth2.service.IApiService;

import retrofit.RestAdapter;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class ApiService {

    public String token;
    private IApiService _apiService;

    public ApiService(String token) {
        this.token=token;
    }


    public IApiService getMessage() {
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(OauthConstant.AUTHENTICATION_SERVER_URL).
                        setRequestInterceptor(new ApiRequestInterceptorService(token).requestInterceptor)
                .build();
        _apiService = restAdapter.create(IApiService.class);


        return _apiService;
    }
}


