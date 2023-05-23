package com.example.roll;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CanvasView extends androidx.appcompat.widget.AppCompatImageView {
    public int dpToPx(Context context, float dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }
    static int COLOR_PALETTE;
    static final int FIELD_SIZE = 45;
    private Bitmap mBitmap;
    private final Canvas canvas;
    public static Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static int[] bgColor = {Color.LTGRAY, 0xffff0000, 0xff00ff00, 0xff2222ff, 0xffffff00, 0xff000000};
    public static Paint wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static int[] wallColor = {Color.DKGRAY, 0xffbb0000, 0xff00bb00, 0xff0000bb, 0xffcccc00, 0xff0000dd};
    public static Paint ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static int[] ballColor = {Color.YELLOW, 0xffc0c0c0, 0xffc0c0c0, 0xffc0c0c0, 0xffc0c0c0, 0xffffff00};
    public static Paint holePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static int[] holeColor = {Color.BLACK, 0xffbb0000, 0xff00bb00, 0xff0000aa, 0xffcccc00, 0xffee0000};
    public static Paint finishPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int ballX;
    private int ballY;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Bitmap imageBitmap = Bitmap.createBitmap(
                dpToPx(this.getContext(), 360),
                dpToPx(this.getContext(), 450),
                Bitmap.Config.ARGB_8888
        );
        canvas = new Canvas(imageBitmap);
        this.setImageBitmap(imageBitmap);
        finishPaint.setColor(Color.WHITE);
        ballX = 20;
        ballY = 20;
    }

    public void drawMap(Map map, int color){
        bgPaint.setColor(bgColor[color]);
        wallPaint.setColor(wallColor[color]);
        holePaint.setColor(holeColor[color]);
        canvas.drawRect(dpToPx(this.getContext(), 0),
            dpToPx(this.getContext(), 0),
            dpToPx(this.getContext(), 360),
            dpToPx(this.getContext(), 450),
            bgPaint);
        for(int j=0; j<10; j++){
            for(int i=0; i<8;i++){
                if(map.fields[j][i].hole)
                    canvas.drawCircle(dpToPx(this.getContext(),(float)(i*FIELD_SIZE+22.5)),
                            dpToPx(this.getContext(),  (float)(j*FIELD_SIZE+22.5)),
                            dpToPx(this.getContext(),  (float)19.0),
                            holePaint);
                if(map.fields[j][i].finish)
                    canvas.drawCircle(dpToPx(this.getContext(),(float)(i*FIELD_SIZE+22.5)),
                            dpToPx(this.getContext(),  (float)(j*FIELD_SIZE+22.5)),
                            dpToPx(this.getContext(),  (float)17.5),
                            finishPaint);
                if(!map.fields[j][i].edges[Map.TOPEDGE])
                    canvas.drawRect(dpToPx(this.getContext(),i*FIELD_SIZE),
                            dpToPx(this.getContext(),  j*FIELD_SIZE),
                            dpToPx(this.getContext(),  (i+1)*FIELD_SIZE),
                            dpToPx(this.getContext(),  j*FIELD_SIZE+2),
                            wallPaint);
                if(!map.fields[j][i].edges[Map.BOTTOMEDGE])
                    canvas.drawRect(dpToPx(this.getContext(), i*FIELD_SIZE),
                            dpToPx(this.getContext(),  (j+1)*FIELD_SIZE-2),
                            dpToPx(this.getContext(),  (i+1)*FIELD_SIZE),
                            dpToPx(this.getContext(),  (j+1)*FIELD_SIZE),
                            wallPaint);
                if(!map.fields[j][i].edges[Map.RIGHTEDGE])
                    canvas.drawRect(dpToPx(this.getContext(), (i+1)*FIELD_SIZE-2),
                            dpToPx(this.getContext(),  j*FIELD_SIZE),
                            dpToPx(this.getContext(),  (i+1)*FIELD_SIZE),
                            dpToPx(this.getContext(),  (j+1)*FIELD_SIZE),
                            wallPaint);
                if(!map.fields[j][i].edges[Map.LEFTEDGE])
                    canvas.drawRect(dpToPx(this.getContext(), i*FIELD_SIZE),
                            dpToPx(this.getContext(),  j*FIELD_SIZE),
                            dpToPx(this.getContext(),  (i)*FIELD_SIZE+2),
                            dpToPx(this.getContext(),  (j+1)*FIELD_SIZE),
                            wallPaint);
            }
        }
        this.invalidate();
    }

    public void drawBall(int x, int y, int color){
        ballPaint.setColor(ballColor[color]);
        ballX = x;
        ballY = y;
        canvas.drawCircle(dpToPx(this.getContext(), ballX), dpToPx(this.getContext(), ballY), dpToPx(this.getContext(), 15), ballPaint);
        this.invalidate();
    }
}
