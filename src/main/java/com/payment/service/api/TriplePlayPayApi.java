package com.payment.service.api;

import com.payment.model.api.ApiResponse;
import com.payment.model.api.ProcessedTransaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

public interface TriplePlayPayApi {

    @POST("{path}")
    Call<ApiResponse<ProcessedTransaction>> performPost(@Path("path") String path,
                                                        @Header("Authorization") String apiKey,
                                                        @Body Map<String, Object> requestBody);

}
