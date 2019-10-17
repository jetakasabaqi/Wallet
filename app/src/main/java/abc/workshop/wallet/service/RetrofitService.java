package abc.workshop.wallet.service;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private RetrofitService() {
    }

    private static Retrofit.Builder retrofitBuilder
            = new Retrofit.Builder()
            .baseUrl("http://192.168.0.219:8080/")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();


    private static APIService sAPIService = retrofit.create(APIService.class);

    public static APIService getAPIService() {

        return sAPIService;
    }

}

