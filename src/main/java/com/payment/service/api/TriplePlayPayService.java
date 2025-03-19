package com.payment.service.api;

import com.payment.exception.ServiceException;
import com.payment.model.api.ApiResponse;
import com.payment.model.api.ProcessedTransaction;
import jakarta.ws.rs.core.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Map;

import static com.payment.exception.ErrorMessages.BAD_REQUEST_TPP;

public class TriplePlayPayService {

    private final TriplePlayPayApi triplePlayPayApi;
    private final String apiKey;

    public TriplePlayPayService(String apiUrl, String apiKey) {
        this.apiKey = apiKey;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.triplePlayPayApi = retrofit.create(TriplePlayPayApi.class);
    }

    private <T> T controlResponse(retrofit2.Response<T> response) {
        try {
            if(!response.isSuccessful()){
                String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                System.out.println("Error calling {}: {} " + errorMessage);
                throw ServiceException.withStatusAndMessage(Response.Status.INTERNAL_SERVER_ERROR, errorMessage);
            }

            return response.body();
        } catch (Exception e) {
            throw ServiceException.withStatusAndMessage(Response.Status.INTERNAL_SERVER_ERROR, BAD_REQUEST_TPP);
        }
    }

    public <T> T postForProcessedTransaction(String path, Map<String, Object> requestBody) {
        try{
            Call<ApiResponse<ProcessedTransaction>> call = triplePlayPayApi.postProcessedTransaction(path, apiKey, requestBody);
            retrofit2.Response<ApiResponse<ProcessedTransaction>> response = call.execute();

            return (T) controlResponse(response);
        } catch (Exception e) {
            throw ServiceException.withStatusAndMessage(Response.Status.INTERNAL_SERVER_ERROR, BAD_REQUEST_TPP);
        }
    }

    public <T> T postForString(String path, Map<String, Object> requestBody) {
        try{
            Call<ApiResponse<String>> call = triplePlayPayApi.postString(path, apiKey, requestBody);
            retrofit2.Response<ApiResponse<String>> response = call.execute();

            return (T) controlResponse(response);
        } catch (Exception e) {
            throw ServiceException.withStatusAndMessage(Response.Status.INTERNAL_SERVER_ERROR, BAD_REQUEST_TPP);
        }
    }

}
