package nemo.kmj.com.a17appjam;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class MyService extends Service { //백그라운드 돌려주는 객체 ㅠㅜㅠㅜ
    boolean isAlarming=false;
    BluetoothSPP bt=new BluetoothSPP(this); //spp라이브러리 사용

    IBinder mBinder = new MyBinder();

    class MyBinder extends Binder {
        MyService getService() { // 서비스 객체를 리턴
            return MyService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) { //액티비티랑 정보 공유할때 안할때는 필요 ㄴㄴ
        Log.e("test", "서비스의 onBind");
        return mBinder;
    }



    @Override
    public void onCreate() { // startService() 실행될때 or 실행중인 앱을 종료할때 실행
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        if(load()==1){
            blueToothStart();
        }
        else{
            Log.e("test", "쉐어드0");
        }




        Log.e("test", "서비스의 onCreate");

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){ // startService() 실행될때 or 실행중인 앱을 종료할때 실행 단 oncreate실행우 실행
        Log.e("test", "서비스의 onStartCommand");

        if(load()==1){
            blueToothStart();
        }
        else{
            Log.e("test", "쉐어드0");

        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        bt.stopService();
        Log.e("test", "서비스의 onDestroy");
    }

    void blueToothStart(){ //연결
        if (!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                bt.autoConnect("HC-06");
            }
        }


        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                //ㄷㅔ이터 받아질때 매번 실행

                Log.e("test", message+" "+bt.getConnectedDeviceName());
                if(!isAlarming){
                    isAlarming=true;
                    bt.stopService();
                    Log.e("test","백그돌리는거끄기");
                    Intent intent=new Intent(getApplicationContext(),AlarmActivity.class);
                    startActivity(intent);

                }





            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener()

        {
            public void onDeviceConnected(String name, String address) {

                //연결됐을때
                Log.e("test", name+address);
                Log.e("test", "connect");
                Toast.makeText(getApplicationContext(), "알림이 활성 되었습니다!", Toast.LENGTH_SHORT).show();

            }

            public void onDeviceDisconnected() {    //연결끊김
                Log.e("test", "consad");

            }

            public void onDeviceConnectionFailed() {
                //연결 실패했을때
                Log.e("test", "sad");
            }
        });
    }
    int load() {
        SharedPreferences pref;
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        int check = pref.getInt("checked", 0);
        if (check == 1) {
            return 1;
        } else {
            return 0;
        }
    }

}

