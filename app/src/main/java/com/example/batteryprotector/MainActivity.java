package com.example.batteryprotector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView Heat,Battery,Health;
    ImageView HC,CC;
    int level,status,temp;
    String message;
    private BroadcastReceiver b = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            temp = (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
            message = temp +" "+Character.toString ((char) 176) + "C";
            Heat.setText("Temperature = "+ message);

            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Battery.setText("Battery Level = "+level + " %");

            status = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            if(status == BatteryManager.BATTERY_HEALTH_COLD){Health.setText("Battery Health = Cold");}
            if (status == BatteryManager.BATTERY_HEALTH_DEAD){Health.setText("Battery Health = Dead");}
            if (status == BatteryManager.BATTERY_HEALTH_GOOD){Health.setText("Battery Health = Good");}
            if (status == BatteryManager.BATTERY_HEALTH_OVERHEAT){Health.setText("Battery Health = Over Heat");}
            if (status == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){Health.setText("Battery Health = Over Voltage");}
            if(status == BatteryManager.BATTERY_HEALTH_UNKNOWN){Health.setText("Battery Health = Unknown");}
            if(status == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){Health.setText("Battery Health = Unspecified failure");}
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        this.registerReceiver(this.b, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Heat=findViewById(R.id.textView2);
        Battery=findViewById(R.id.textView3);
        Health=findViewById(R.id.textView4);
        HC=findViewById(R.id.imageView4);
        CC=findViewById(R.id.imageView5);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(b);
        //Toast.makeText(MainActivity.this, "Unregister b", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.registerReceiver(this.b, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void Click(View view) {
        switch (view.getId()) {
            case R.id.textView2:
                Toast.makeText(MainActivity.this, "Battery Temperature Is "+ message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.textView3:
                Toast.makeText(MainActivity.this, "Battery Level Is "+ level+" %", Toast.LENGTH_SHORT).show();
                break;
            case R.id.textView4:
                if(status == BatteryManager.BATTERY_HEALTH_COLD){Toast.makeText(MainActivity.this, "Battery Health Is Cold",Toast.LENGTH_SHORT).show();}
                if (status == BatteryManager.BATTERY_HEALTH_DEAD){Toast.makeText(MainActivity.this, "Battery Health Is Dead",Toast.LENGTH_SHORT).show();}
                if (status == BatteryManager.BATTERY_HEALTH_GOOD){Toast.makeText(MainActivity.this, "Battery Health Is Good",Toast.LENGTH_SHORT).show();}
                if (status == BatteryManager.BATTERY_HEALTH_OVERHEAT){Toast.makeText(MainActivity.this, "Battery Health Is Over Heat",Toast.LENGTH_SHORT).show();}
                if (status == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){Toast.makeText(MainActivity.this, "Battery Health Is Over Voltage",Toast.LENGTH_SHORT).show();}
                if(status == BatteryManager.BATTERY_HEALTH_UNKNOWN){Toast.makeText(MainActivity.this, "Battery Health Is Unknown",Toast.LENGTH_SHORT).show();}
                if(status == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){Toast.makeText(MainActivity.this, "Battery Health Inspecified",Toast.LENGTH_SHORT).show();}
                break;
            case R.id.imageView4:
                Intent i= new Intent(MainActivity.this,Temperature.class);
                startActivity(i);
                break;
            case R.id.imageView5:
                Intent ii= new Intent(MainActivity.this,First.class);
                startActivity(ii);
                break;
        }
    }
}
