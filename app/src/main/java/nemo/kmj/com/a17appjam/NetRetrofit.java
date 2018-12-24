package nemo.kmj.com.a17appjam;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit { //레트로핏 ㅎ
    final static String url="http://198.13.39.251";
    final static int port=3000;

    private static Retrofit retrofit;
    public static RetrofitServices getInstance(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(url+":"+port)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RetrofitServices.class);
    }
}