package newteam2.welcometomoscow;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Джал on 11/6/2016.
 */


public class GetData {
    private static final String URL = "http://31.135.85.7:900/";
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}


