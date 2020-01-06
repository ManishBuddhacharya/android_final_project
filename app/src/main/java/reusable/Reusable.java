package reusable;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Reusable {
    /*127.0.0.1:3000*/
//    public static final String BASE_URL = "http://127.0.0.1:3000/";
    public static final String BASE_URL = "http://10.0.2.2:3000/";
//    public static final String BASE_URL = "http://192.168.0.104/";


    public static Retrofit getInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Reusable.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
