package newteam2.welcometomoscow;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Джал on 11/6/2016.
 */

public interface GetApi {
    @GET("quest/1")
    Call<QuestInfo> getData();
}
