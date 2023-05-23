package com.example.roll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.WindowManager;
import android.widget.TextView;

public class OtherGameActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensor;
    SharedPreferences sharedPref;
    private TextView xtv;
    private CanvasView canvas;
    private Map map;
    private final double[] ballX = new double[4];
    private final double[] ballY = new double[4];
    private final double[] ballXspeed = new double[4];
    private final double[] ballYspeed = new double[4];
    private final int[] ballI = new int[4];
    private final int[] ballJ = new int[4];
    private final boolean[] inHole = new boolean[4];
    private final float[] Rot = new float[9];
    private final double field_size = 45.0;
    int color_palette;

    private final SensorEventListener sl = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            SensorManager.getRotationMatrixFromVector(Rot, sensorEvent.values);
            //xtv.setText(String.valueOf(Rot[0])+" "+String.valueOf(Rot[1])+" "+String.valueOf(Rot[2])+" ");
            //ytv.setText(String.valueOf(Rot[3])+" "+String.valueOf(Rot[4])+" "+String.valueOf(Rot[5])+" ");
            //ztv.setText(String.valueOf(Rot[6])+" "+String.valueOf(Rot[7])+" "+String.valueOf(Rot[8])+" ");
            double yAcc = -9.81*(Rot[2]*Rot[1]+Rot[5]*Rot[4]);
            double xAcc = 9.81*(Rot[2]*Rot[0]+Rot[5]*Rot[3]);
            canvas.drawMap(map, color_palette);

            for(int i =0; i<4; i++) {
                if(inHole[i] && Math.abs(xAcc)+Math.abs(yAcc) < 3) {
                    ballXspeed[i] = 0;
                    ballYspeed[i] = 0;
                    ballX[i] = 45 * ballJ[i] + 22.5;
                    ballY[i] = 45 * ballI[i] + 22.5;
                    canvas.drawBall((int) ballX[i], (int) ballY[i], color_palette);
                    continue;
                }
                //adding some friction
                ballXspeed[i] *= 0.999;
                ballYspeed[i] *= 0.999;

                double newBallXspeed = ballXspeed[i] + xAcc * 0.01;
                double newBallYspeed = ballYspeed[i] + yAcc * 0.01;
                double minX = map.fields[ballI[i]][ballJ[i]].edges[Map.LEFTEDGE] ? 17.0 : field_size * ballJ[i] + 17.0;
                double maxX = map.fields[ballI[i]][ballJ[i]].edges[Map.RIGHTEDGE] ? 343.0 : field_size * (ballJ[i] + 1) - 17.0;
                double minY = map.fields[ballI[i]][ballJ[i]].edges[Map.TOPEDGE] ? 17.0 : field_size * ballI[i] + 17.0;
                double maxY = map.fields[ballI[i]][ballJ[i]].edges[Map.BOTTOMEDGE] ? 433.0 : field_size * (ballI[i] + 1) - 17.0;
                double xInField = ballX[i] - field_size * ballJ[i];
                double yInField = ballY[i] - field_size * ballI[i];
                {
                    if(ballI[i]>0 && xInField>=yInField && xInField>= field_size-yInField){
                        if(map.fields[ballI[i]][ballJ[i]].edges[Map.RIGHTEDGE] && (!map.fields[ballI[i]-1][ballJ[i]].edges[Map.RIGHTEDGE] || !map.fields[ballI[i]][ballJ[i]+1].edges[Map.TOPEDGE]))
                            maxX = field_size*ballJ[i] + field_size - Math.sqrt(15*15 - (yInField)*(yInField));
                        if(map.fields[ballI[i]][ballJ[i]].edges[Map.LEFTEDGE] && (!map.fields[ballI[i]-1][ballJ[i]].edges[Map.LEFTEDGE]|| !map.fields[ballI[i]][ballJ[i]-1].edges[Map.TOPEDGE]))
                            minX = field_size*ballJ[i] +  Math.sqrt(15*15 - (yInField)*(yInField));

                    }
                    else if(ballJ[i]<7 && xInField<=yInField && xInField>= 45-yInField){
                        if(map.fields[ballI[i]][ballJ[i]].edges[Map.TOPEDGE] && (!map.fields[ballI[i]][ballJ[i]+1].edges[Map.TOPEDGE]|| !map.fields[ballI[i]-1][ballJ[i]].edges[Map.RIGHTEDGE]))
                            minY = field_size*ballI[i] + Math.sqrt(15*15 - (field_size-xInField)*(field_size-xInField));
                        if(map.fields[ballI[i]][ballJ[i]].edges[Map.BOTTOMEDGE] && (!map.fields[ballI[i]][ballJ[i]+1].edges[Map.BOTTOMEDGE]|| !map.fields[ballI[i]+1][ballJ[i]].edges[Map.RIGHTEDGE]))
                            maxY = field_size*ballI[i] + field_size - Math.sqrt(15*15 - (field_size-xInField)*(field_size-xInField));
                    }
                    else if(ballI[i]<9 && xInField<=yInField && xInField<= 45-yInField){
                        if(map.fields[ballI[i]][ballJ[i]].edges[Map.RIGHTEDGE] && (!map.fields[ballI[i]+1][ballJ[i]].edges[Map.RIGHTEDGE]|| !map.fields[ballI[i]][ballJ[i]+1].edges[Map.BOTTOMEDGE]))
                            maxX = field_size*ballJ[i] + field_size - Math.sqrt(15*15 - (field_size-yInField)*(field_size-yInField));
                        if(map.fields[ballI[i]][ballJ[i]].edges[Map.LEFTEDGE] && (!map.fields[ballI[i]+1][ballJ[i]].edges[Map.LEFTEDGE]|| !map.fields[ballI[i]][ballJ[i]-1].edges[Map.BOTTOMEDGE]))
                            minX = field_size*ballJ[i] + Math.sqrt(15*15 - (field_size-yInField)*(field_size-yInField));
                    }
                    else if(ballJ[i]>0 && xInField>=yInField && xInField<= 45-yInField){
                        if(map.fields[ballI[i]][ballJ[i]].edges[Map.TOPEDGE] && (!map.fields[ballI[i]][ballJ[i]-1].edges[Map.TOPEDGE]|| !map.fields[ballI[i]-1][ballJ[i]].edges[Map.LEFTEDGE]))
                            minY = field_size*ballI[i] + Math.sqrt(15*15 - (xInField)*(xInField));
                        if(map.fields[ballI[i]][ballJ[i]].edges[Map.BOTTOMEDGE] && (!map.fields[ballI[i]][ballJ[i]-1].edges[Map.BOTTOMEDGE]|| !map.fields[ballI[i]+1][ballJ[i]].edges[Map.LEFTEDGE]))
                            maxY = field_size*ballI[i] + field_size -  Math.sqrt(15*15 - (xInField)*(xInField));
                    }
                }
                double oldX = ballX[i];
                double oldY = ballY[i];
                ballX[i] = ballX[i] + 150.0 * (newBallXspeed + ballXspeed[i]) * 0.01;
                ballY[i] = ballY[i] + 150.0 * (newBallYspeed + ballYspeed[i]) * 0.01;
                if (ballX[i] < minX) {
                    ballX[i] = minX;
                    newBallXspeed = 0;
                } else if (ballX[i] > maxX) {
                    ballX[i] = maxX;
                    newBallXspeed = 0;
                }
                if (ballY[i] < minY) {
                    ballY[i] = minY;
                    newBallYspeed = 0;
                } else if (ballY[i] > maxY) {
                    ballY[i] = maxY;
                    newBallYspeed = 0;
                }
                boolean crash = false;
                for(int j=0; j<4; j++){
                    if(i==j) continue;
                    double Xspeed = (ballXspeed[i]+newBallXspeed)/2;
                    double Yspeed = (ballYspeed[i]+newBallYspeed)/2;
                    if((ballX[i]-ballX[j])*(ballX[i]-ballX[j]) + (ballY[i]-ballY[j])*(ballY[i]-ballY[j]) < 30*30){
                        double a = Yspeed*Yspeed + Xspeed*Xspeed;
                        double b = 2*(oldX-ballX[j])*Xspeed+2*(oldY-ballY[j])*Yspeed;
                        double c = (oldX-ballX[j])*(oldX-ballX[j]) + (oldY-ballY[j])*(oldY-ballY[j]) -30*30;
                        double delta = b*b-4*a*c;
                        if (delta < 0) continue;
                        if(a==0) continue;
                        delta = Math.sqrt(delta);
                        double t1 = (-b-delta)/(2*a);
                        double t2 = (-b+delta)/(2*a);
                        double t = Math.min(t1, t2);
                        double x = Math.min(Math.max(oldX + Xspeed * t, minX), maxX);
                        double y = Math.min(Math.max(oldY + Yspeed * t, minY), maxY);
                        if(Double.isNaN(x) || Double.isNaN(y)) continue;
                        ballX[i] = x;
                        ballY[i] = y;
                        crash = true;
                    }
                }
                if(crash){
                    newBallXspeed =0;
                    newBallYspeed =0;
                }
                ballJ[i] = ((int) ballX[i]) / 45;
                ballI[i] = ((int) ballY[i]) / 45;
                ballXspeed[i] = newBallXspeed;
                ballYspeed[i] = newBallYspeed;
                xInField = ballX[i] - field_size * ballJ[i] - 22.5;
                yInField = ballY[i] - field_size * ballI[i] - 22.5;
                if (map.fields[ballI[i]][ballJ[i]].hole && xInField * xInField + yInField * yInField <= 20.0 * 20.0) {
                    inHole[i] = true;
                }else{
                    inHole[i] = false;
                }
                canvas.drawBall((int) ballX[i], (int) ballY[i], color_palette);
            }
            if(inHole[0]&&inHole[1]&&inHole[2]&&inHole[3]){
                startLevel();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_game);
        sharedPref = getSharedPreferences("mySettings", MODE_PRIVATE);
        color_palette = (int)sharedPref.getLong("colorPalette", 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        xtv = findViewById(R.id.xOtherRotate);
        canvas = findViewById(R.id.otherCanvas);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        startLevel();
    }

    private void startLevel(){
        map = new Map(2137);
        canvas.drawMap(map, color_palette);
        for(int i = 0; i<4; i++) {
            Pair<Integer, Integer> p = map.getRandomPosition();
            ballJ[i] = p.second;
            ballI[i] = p.first;
            ballX[i] = 45 * ballJ[i] + 22.5;
            ballY[i] = 45 * ballI[i] + 22.5;
            ballXspeed[i] = 0;
            ballYspeed[i] = 0;
            canvas.drawBall((int) ballX[i], (int) ballY[i], color_palette);
            inHole[i] = false;
        }
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
}