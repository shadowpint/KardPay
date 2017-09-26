package tech.codefest.kradpay.oauth2.client;

import retrofit.RequestInterceptor;

/**
 * Created by dominicneeraj on 08/08/17.
 */

public class ApiRequestInterceptorService {

    private final String token;
    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {


            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer "+token);
        }


    };


    public ApiRequestInterceptorService(String token) {
        this.token=token;
    }
}

