package timmycheng.countdown;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button pause, start, restart;
    TextView time_min, time_sec;
    boolean isPaused = false;
    long timeRemaining = -1;
    String zero_min, zero_sec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        setListener();
    }

    public void initViews() {
        start = (Button) findViewById(R.id.start_btn);
        restart = (Button) findViewById(R.id.restart_btn);
        pause = (Button) findViewById(R.id.pause_btn);
        time_min = (TextView) findViewById(R.id.time_min);
        time_sec = (TextView) findViewById(R.id.time_sec);

        restart.setEnabled(false);
        pause.setEnabled(false);
    }

    private void setListener() {
        start.setOnClickListener(startCountdown);
        restart.setOnClickListener(restartCountdown);
        pause.setOnClickListener(pauseCountdown);
    }
    //when closing app, store the remaining time.
    public void onStop() {
        super.onStop();
        Pref.setRemain(MainActivity.this, String.valueOf(timeRemaining));
        Intent intent = new Intent(MainActivity.this, service.class);
        startService(intent);
    }
    //the action of start button
    private Button.OnClickListener startCountdown = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            restart.setEnabled(false);
            if((time_min.getText().toString().equals("00")) && (time_sec.getText().toString().equals("00"))) {
                Toast.makeText(MainActivity.this, "請先設定時間", Toast.LENGTH_SHORT).show();
            }
            else {
                isPaused = false;
                pause.setEnabled(true);
                long millisInFuture;
                if(timeRemaining > 0) {
                    millisInFuture = timeRemaining;
                }
                else {
                    millisInFuture = (Integer.parseInt(time_min.getText().toString()) * 60 + Integer.parseInt(time_sec.getText().toString())) * 1000;
                }
                CountDownTimer timer = new CountDownTimer(millisInFuture, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (isPaused) {
                            cancel();
                        } else {
                            if(((millisUntilFinished / 1000) / 60) < 10) {
                                zero_min = "0";
                            } else {
                                zero_min = "";
                            }
                            if(((millisUntilFinished / 1000) % 60) < 10) {
                                zero_sec = "0";
                            } else {
                                zero_sec = "";
                            }
                            time_min.setText(zero_min + (millisUntilFinished / 1000) / 60);
                            time_sec.setText(zero_sec + (millisUntilFinished / 1000) % 60);
                            timeRemaining = millisUntilFinished;
                        }
                    }

                    @Override
                    public void onFinish() {
                        showNotification();
                    }
                }.start();
            }
        }
    };
    //the action of pause button
    private Button.OnClickListener pauseCountdown = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            restart.setEnabled(true);
            isPaused = true;
        }
    };
    //the action of restart button
    private Button.OnClickListener restartCountdown = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            restart.setEnabled(false);
            pause.setEnabled(false);
            isPaused = false;
            timeRemaining = -1;
            time_min.setText("00");
            time_sec.setText("00");
        }
    };
    //the setting of time
    public void showTimePickerDialog(View v) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View settime = layoutInflater.inflate(R.layout.settime, null);
        dialog.setTitle("設定時間");
        dialog.setView(settime);

        final NumberPicker min_picker = (NumberPicker) settime.findViewById(R.id.min_picker);
        final NumberPicker sec_picker = (NumberPicker) settime.findViewById(R.id.sec_picker);
        final Button cancel = (Button) settime.findViewById(R.id.cancel);
        final Button ok = (Button) settime.findViewById(R.id.ok);
        final AlertDialog d = dialog.create();

        min_picker.setMaxValue(59);
        min_picker.setMinValue(0);
        min_picker.setValue(0);
        sec_picker.setMaxValue(59);
        sec_picker.setMinValue(0);
        sec_picker.setValue(0);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(min_picker.getValue() < 10) {
                    zero_min = "0";
                } else {
                    zero_min = "";
                }
                if(sec_picker.getValue() < 10) {
                    zero_sec = "0";
                } else {
                    zero_sec = "";
                }
                time_min.setText(zero_min + String.valueOf(min_picker.getValue()));
                time_sec.setText(zero_sec + String.valueOf(sec_picker.getValue()));
                Pref.setMin(MainActivity.this, time_min.getText().toString());
                Pref.setSec(MainActivity.this, time_sec.getText().toString());
                d.dismiss();
            }
        });
        d.show();
    }
    //notification
    protected void showNotification() {
        NotificationManager barManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder barMsg = new Notification.Builder(this)
                .setTicker("時間到囉！")
                .setContentTitle("時間到囉！")
                .setContentText("通知一下")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
        barManager.notify(0,barMsg.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //restore the time of last setting
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_restore) {
            if(!Pref.getMin(this).equals("")) {
                time_min.setText(Pref.getMin(this));
            }
            if(!Pref.getMin(this).equals("")) {
                time_sec.setText(Pref.getSec(this));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
