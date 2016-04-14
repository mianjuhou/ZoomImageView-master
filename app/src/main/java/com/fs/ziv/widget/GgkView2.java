package com.fs.ziv.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fs.ziv.R;

/**
 * Created by fangdean on 2016/4/14.
 */
public class GgkView2 extends View {

    public GgkView2(Context context) {
        this(context,null);
    }

    public GgkView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GgkView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Path mPath;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private void init() {
        mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.image6);
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        mPath=new Path();


        upPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        upPaint.setStyle(Paint.Style.STROKE);
        upPaint.setStrokeWidth(20);
        upPaint.setColor(Color.RED);

        downPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        downPaint.setColor(0xFFFFCC44);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        upBitmap=Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(),Bitmap.Config.ARGB_8888);
        upCanvas=new Canvas(upBitmap);

        downBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        downCanvas = new Canvas(downBitmap);
        downCanvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),downPaint);
    }

    private boolean isComplete=false;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,null);//绘制背景

        //是否遮盖
        if(isZG){
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            //绘制遮盖下层
            int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
            canvas.drawBitmap(downBitmap,0,0,null);
            Bitmap upBitmap2 = getUpBitmap(canvasWidth, canvasHeight);
            if(isPath){
                //绘制遮盖上层
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                canvas.drawBitmap(upBitmap2,0,0,mPaint);
                mPaint.setXfermode(null);
            }
            canvas.restoreToCount(layerId);

            //获取遮盖层位图
            if(isComplete){
                new Thread(new BitmapRunnable(upBitmap2)).start();
            }
        }
    }

    class BitmapRunnable implements Runnable{
        private Bitmap mBitmap;
        public BitmapRunnable(Bitmap bm) {
            mBitmap=bm;
        }

        @Override
        public void run() {
            if(computePercent(mBitmap)){
                isZG=false;
                postInvalidate();
            }
        }
    }

    private boolean isZG=true;
    private boolean computePercent(Bitmap bm) {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();

        int transPixs=0;
        for (int i = 0; i < bmHeight; i++) {
            for (int j = 0; j < bmWidth; j++) {
                int pixel = bm.getPixel(j, i);
                if(pixel!=Color.TRANSPARENT){
                    transPixs++;
                }
            }
        }

        float percent=transPixs*1.0f/(bmWidth*bmHeight);
        System.out.println(percent);
        if(percent>0.6){
            return true;
        }else{
            return false;
        }
    }
    private Paint upPaint;
    private Bitmap upBitmap;
    private Canvas upCanvas;
    private Bitmap getUpBitmap(int w,int h){
        upCanvas.drawPath(mPath,upPaint);
        return upBitmap;
    }

    private Bitmap downBitmap;
    private Canvas downCanvas;
    private Paint downPaint;



    boolean isPath=false;
    private float mLastX;
    private float mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                isComplete=false;
                isPath=true;
                mLastX=event.getX();
                mLastY=event.getY();
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float x=event.getX();
                float y=event.getY();

                float dx=Math.abs(x-mLastX);
                float dy=Math.abs(y-mLastY);

                if(dx>5||dy>5){
                    mPath.lineTo(x,y);
                    invalidate();

                    mLastX=x;
                    mLastY=y;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isComplete=true;
                invalidate();
                break;
        }
        return true;
    }


}
