package nemo.kmj.com.a17appjam;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.POST;


public interface RetrofitServices {
    @POST("/sendData") //종훈이가 짠 서버 ㅎ
    @FormUrlEncoded
    Call<Data> sendData(@Field("hour") int hour);

}
