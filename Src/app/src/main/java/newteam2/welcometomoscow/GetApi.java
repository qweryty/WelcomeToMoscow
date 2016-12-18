package newteam2.welcometomoscow;

import com.google.gson.JsonArray;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by Джал on 11/6/2016.
 */

public interface GetApi {
    @GET("quest/all")
    Call<ResponseQ> getData();
}
