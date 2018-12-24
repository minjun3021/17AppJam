package nemo.kmj.com.a17appjam;

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.net.wifi.p2p.WifiP2pManager.ERROR;

public class MainActivity extends AppCompatActivity {
    public Button _vibrator_btn;
    public TextToSpeech tts;

    TextView On, Off;
    Switch mSwitch;
    ImageView mbell_off, mbell_on;
    SoundPool mSound;
    int mDing;
    AudioManager mAm;

    void OnVisible() {
        On.setVisibility(TextView.VISIBLE);
        Off.setVisibility(TextView.INVISIBLE);
    }

    void OffVisible() {
        On.setVisibility(TextView.INVISIBLE);
        Off.setVisibility(TextView.VISIBLE);
    }

    void load() {
        SharedPreferences pref;
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        int check = pref.getInt("checked", 0);
        if (check == 1) {
            mSwitch.setChecked(true);
            OnVisible();
            mbell_off.setVisibility(ImageView.INVISIBLE);
            mbell_on.setVisibility(ImageView.VISIBLE);
            bluetoothStart();
        } else {
            mSwitch.setChecked(false);
            OffVisible();
            bluetoothStop();
            mbell_off.setVisibility(ImageView.VISIBLE);
            mbell_on.setVisibility(ImageView.INVISIBLE);
        }
    }

    void bluetoothStart() {


            Log.e("test","intent할께요^^");
            Intent intent = new Intent(
                    getApplicationContext(),//현재제어권자
                    MyService.class); // 이동할 컴포넌트

            startService(intent); // 서비스 시작


    }
    void bluetoothStop(){
        Intent intent = new Intent(
                getApplicationContext(),//현재제어권자
                MyService.class); // 이동할 컴포넌트
        stopService(intent); // 서비스 종료
    }
    void save(int i) {
        SharedPreferences pref;
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("checked", i);
        editor.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        _vibrator_btn = findViewById(R.id.vibrator_btn);
        mSwitch = findViewById(R.id.sw_btn);
        On = findViewById(R.id.textView2);
        Off = findViewById(R.id.textView3);
        On.setVisibility(TextView.INVISIBLE);

        mbell_off = findViewById(R.id.bell_off);
        mbell_on = findViewById(R.id.bell_on);
        load(); //쉐어드 불러오는곳
        mSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mDing = mSound.load(this, R.raw.ddiring, 1);


        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    Log.e("test", "액티비티-서비스 시작버튼클릭");
                    save(1);
                    OnVisible();
                    mbell_off.setVisibility(ImageView.INVISIBLE);
                    mbell_on.setVisibility(ImageView.VISIBLE);
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(300);
                    mSound.play(mDing, 1, 1, 0, 0, 1);

                    bluetoothStart();


                } else {
                    Log.e("test", "액티비티-서비스 종료버튼클릭");
                    save(0);
                    OffVisible();
                    On.setVisibility(TextView.INVISIBLE);
                    Off.setVisibility(TextView.VISIBLE);
                    mbell_off.setVisibility(ImageView.VISIBLE);
                    mbell_on.setVisibility(ImageView.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "알림이 비활성 되었습니다", Toast.LENGTH_SHORT).show();
                    bluetoothStop();
                }
            }
        });



    }
}
