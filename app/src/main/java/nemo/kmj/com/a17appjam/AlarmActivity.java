package nemo.kmj.com.a17appjam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmActivity extends AppCompatActivity {
    Button btn;
    Vibrator vibrator;

    SimpleDateFormat mFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        btn=findViewById(R.id.message_off);
        vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        th.start();
        long now = System.currentTimeMillis();
        mFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date mDate = new Date(now);
        String time=mFormat.format(mDate).split(" ")[1];
        time=time.split(":")[0];
        time=String.valueOf(Integer.valueOf(time));
        Log.e("test",time);



        NetRetrofit.getInstance().sendData(Integer.valueOf(time)).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.e("test","success");
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e("test","fail");
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                th.interrupt();
                save();
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });

    }

    Thread th= new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    vibrator.vibrate(400);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    vibrator.vibrate(400);
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    void save(){
        SharedPreferences pref;
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("checked", 0);
        editor.commit();
    }
}
