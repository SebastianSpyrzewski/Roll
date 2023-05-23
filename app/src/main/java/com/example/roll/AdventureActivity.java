package com.example.roll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class AdventureActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor sensor;
    SharedPreferences sharedPref;
    private TextView xtv;
    private TextView ytv;
    private CanvasView canvas;
    private Map map;
    private double ballX;
    private double ballY;
    private double ballXspeed;
    private double ballYspeed;
    private int ballI;
    private int ballJ;
    private final double field_size = 45.0;
    private final float[] Rot = new float[9];
    private int level;
    private final int levels_number = 6;
    private int mode;
    int color_palette;
    Timer timer;
    long miliseconds;

    private SensorEventListener sl = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            SensorManager.getRotationMatrixFromVector(Rot, sensorEvent.values);

            double yAcc = -9.81*(Rot[2]*Rot[1]+Rot[5]*Rot[4]);
            double xAcc = 9.81*(Rot[2]*Rot[0]+Rot[5]*Rot[3]);
            double zRot = Rot[8];


            //xtv.setText(String.valueOf(Rot[0])+" "+String.valueOf(Rot[1])+" "+String.valueOf(Rot[2])+" ");
            //ytv.setText(String.valueOf(Rot[3])+" "+String.valueOf(Rot[4])+" "+String.valueOf(Rot[5])+" ");
            //ztv.setText(String.valueOf(Rot[6])+" "+String.valueOf(Rot[7])+" "+String.valueOf(Rot[8])+" ");
            //adding some friction
            ballXspeed *= 0.999;
            ballYspeed *= 0.999;

            double newBallXspeed = ballXspeed + xAcc*0.01;
            double newBallYspeed = ballYspeed + yAcc*0.01;

            double xInField = ballX - field_size*ballJ;
            double yInField = ballY - field_size*ballI;
            double minX, maxX, minY, maxY;
            minX = map.fields[ballI][ballJ].edges[Map.LEFTEDGE] ? 0.0 : field_size*ballJ+17.0;
            maxX = map.fields[ballI][ballJ].edges[Map.RIGHTEDGE] ? 360.0 : field_size*(ballJ+1)-17.0;
            minY = map.fields[ballI][ballJ].edges[Map.TOPEDGE] ? 0.0 : field_size*ballI+17.0;
            maxY = map.fields[ballI][ballJ].edges[Map.BOTTOMEDGE] ? 450.0 : field_size*(ballI+1)-17.0;

            {
                if(ballI>0 && xInField>=yInField && xInField>= field_size-yInField){
                    if(map.fields[ballI][ballJ].edges[Map.RIGHTEDGE] && (!map.fields[ballI-1][ballJ].edges[Map.RIGHTEDGE] || !map.fields[ballI][ballJ+1].edges[Map.TOPEDGE]))
                        maxX = field_size*ballJ + field_size - Math.sqrt(15*15 - (yInField)*(yInField));
                    if(map.fields[ballI][ballJ].edges[Map.LEFTEDGE] && (!map.fields[ballI-1][ballJ].edges[Map.LEFTEDGE]|| !map.fields[ballI][ballJ-1].edges[Map.TOPEDGE]))
                        minX = field_size*ballJ +  Math.sqrt(15*15 - (yInField)*(yInField));

                }
                else if(ballJ<7 && xInField<=yInField && xInField>= 45-yInField){
                    if(map.fields[ballI][ballJ].edges[Map.TOPEDGE] && (!map.fields[ballI][ballJ+1].edges[Map.TOPEDGE]|| !map.fields[ballI-1][ballJ].edges[Map.RIGHTEDGE]))
                        minY = field_size*ballI + Math.sqrt(15*15 - (field_size-xInField)*(field_size-xInField));
                    if(map.fields[ballI][ballJ].edges[Map.BOTTOMEDGE] && (!map.fields[ballI][ballJ+1].edges[Map.BOTTOMEDGE]|| !map.fields[ballI+1][ballJ].edges[Map.RIGHTEDGE]))
                        maxY = field_size*ballI + field_size - Math.sqrt(15*15 - (field_size-xInField)*(field_size-xInField));
                }
                else if(ballI<9 && xInField<=yInField && xInField<= 45-yInField){
                    if(map.fields[ballI][ballJ].edges[Map.RIGHTEDGE] && (!map.fields[ballI+1][ballJ].edges[Map.RIGHTEDGE]|| !map.fields[ballI][ballJ+1].edges[Map.BOTTOMEDGE]))
                        maxX = field_size*ballJ + field_size - Math.sqrt(15*15 - (field_size-yInField)*(field_size-yInField));
                    if(map.fields[ballI][ballJ].edges[Map.LEFTEDGE] && (!map.fields[ballI+1][ballJ].edges[Map.LEFTEDGE]|| !map.fields[ballI][ballJ-1].edges[Map.BOTTOMEDGE]))
                        minX = field_size*ballJ + Math.sqrt(15*15 - (field_size-yInField)*(field_size-yInField));
                }
                else if(ballJ>0 && xInField>=yInField && xInField<= 45-yInField){
                    if(map.fields[ballI][ballJ].edges[Map.TOPEDGE] && (!map.fields[ballI][ballJ-1].edges[Map.TOPEDGE]|| !map.fields[ballI-1][ballJ].edges[Map.LEFTEDGE]))
                        minY = field_size*ballI + Math.sqrt(15*15 - (xInField)*(xInField));
                    if(map.fields[ballI][ballJ].edges[Map.BOTTOMEDGE] && (!map.fields[ballI][ballJ-1].edges[Map.BOTTOMEDGE]|| !map.fields[ballI+1][ballJ].edges[Map.LEFTEDGE]))
                        maxY = field_size*ballI + field_size -  Math.sqrt(15*15 - (xInField)*(xInField));
                }
            }

            ballX = ballX + 150.0*(newBallXspeed+ballXspeed)*0.01;
            if(ballX < minX){ ballX=minX; newBallXspeed = 0; }
            else if(ballX > maxX){ ballX=maxX; newBallXspeed = 0; }
            ballJ = ((int)ballX)/45;
            ballY = ballY + 150.0*(newBallYspeed+ballYspeed)*0.01;
            if(ballY < minY){ ballY=minY; newBallYspeed = 0; }
            else if(ballY > maxY){ ballY=maxY; newBallYspeed = 0; }
            ballI = ((int)ballY)/45;
            ballXspeed = newBallXspeed;
            ballYspeed = newBallYspeed;
            xInField = ballX - field_size*ballJ -22.5;
            yInField = ballY - field_size*ballI -22.5;
            if(map.fields[ballI][ballJ].hole && xInField * xInField + yInField * yInField <= 20.0 * 20.0){
                String name = sharedPref.getString("name", "unnamed");
                Context context = getApplicationContext();
                ScoreSaver.SetScore(level, mode, miliseconds, name, context);
                startLevel(0);
                adventureWin();
            }
            if(map.fields[ballI][ballJ].finish && xInField * xInField + yInField * yInField <= 17.5 * 17.5){
                level += mode;
                if(level > levels_number){
                    String name = sharedPref.getString("name", "unnamed");
                    Context context = getApplicationContext();
                    ScoreSaver.SetScore(level, mode, miliseconds, name, context);
                    startLevel(0);
                    adventureWin();
                }
                startLevel(level);
            }
            canvas.drawMap(map, color_palette);
            canvas.drawBall((int)ballX, (int)ballY, color_palette);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure);
        sharedPref = getSharedPreferences("mySettings", MODE_PRIVATE);
        color_palette = (int)sharedPref.getLong("colorPalette", 0);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mode = extras.getInt("mode");
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        xtv = findViewById(R.id.xRotate);
        ytv = findViewById(R.id.yRotate);
        canvas = findViewById(R.id.canvas);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        TimerTask onTimerTask = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        miliseconds += 100;
                        long minutes = miliseconds/60000;
                        long seconds = (miliseconds%60000)/1000;
                        long tenth = (miliseconds%1000)/100;
                        String text = minutes + (seconds > 9 ? ":" : ":0") + seconds + "." + tenth;
                        ytv.setText(text);
                    }
                });
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(onTimerTask, 0, 100);
        startLevel(0);
    }

    private void startLevel(int level){
        map = new Map(level);
        canvas.drawMap(map, color_palette);
        ballX = map.startingX;
        ballY = map.startingY;
        ballJ = (int)(ballX/field_size);
        ballI = (int)(ballY/field_size);
        ballXspeed = 0;
        ballYspeed = 0;
        canvas.drawBall((int)ballX, (int)ballY, color_palette);
        String s = "Level "+ mode*level;
        xtv.setText(s);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sl);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sl, sensor, SensorManager.SENSOR_DELAY_GAME);

    }
    public void adventureWin(){
        Intent intent = new Intent(this, AdventureWinActivity.class);
        intent.putExtra("mode", mode).putExtra("level", level).putExtra("time", miliseconds);
        startActivity(intent);
    }
}