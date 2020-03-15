package com.example.batteryprotector;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.INVISIBLE;

public class Temperature extends AppCompatActivity {
    Button play,pause;
    TextView Heat,perc;
    public int temp,perc_value;
    SeekBar S;
    boolean start=false;
    String message;
    private BroadcastReceiver b1 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            temp = (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
            message = temp+Character.toString ((char) 176) + "C";
            Heat.setText(message);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        this.registerReceiver(this.b1, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Heat=findViewById(R.id.textView5);
        perc=findViewById(R.id.textView7);
        play=findViewById(R.id.button1);
        play.setVisibility(INVISIBLE);
        pause=findViewById(R.id.button2);
        S=findViewById(R.id.seekBar);
        S.setMax(60);
        S.setProgress(40);
        perc.setText("40"+Character.toString ((char) 176) + "C");

        S.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int P, boolean b) {
                perc.setText(String.valueOf(P)+Character.toString ((char) 176) + "C");
                perc_value = Integer.valueOf(P);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(INVISIBLE);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.registerReceiver(this.b1, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.registerReceiver(this.b1, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Service(View view) {
            switch (view.getId()) {
                case R.id.button1:
                    start=true;
                    Toast.makeText(Temperature.this, "Service Started", Toast.LENGTH_SHORT).show();
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);

                    Intent i = new Intent(Temperature.this,MainActivity.class);
                    this.startActivity(i);

                    final Thread t = new Thread() {
                        @Override
                        public void run() {
                            try {
                                while (!isInterrupted()) {
                                    Thread.sleep(500);
                                    runOnUiThread(new Runnable() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        @Override
                                        public void run() {
                                            if(start && temp>=perc_value){

                                                final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Temperature.this)
                                                        .setSmallIcon(R.drawable.batt)
                                                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.heatc6));
                                                notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
                                                final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Temperature.this);
                                                notificationBuilder.setContentText("Temperature Issue");
                                                notificationManager.notify(1, notificationBuilder.build());
                                                notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
                                            }
                                        }
                                    });
                                    Thread.sleep(10000);
                                }
                            } catch (InterruptedException e) {
                            }
                        }
                    };
                    t.start();


                    break;
                case R.id.button2:
                    start=false;
                    Toast.makeText(Temperature.this, "Service Stopped", Toast.LENGTH_SHORT).show();
                    Intent i2 = new Intent(Temperature.this,MainActivity.class);
                    this.startActivity(i2);
                    play.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.INVISIBLE);
                    break;
            }
    }
}
