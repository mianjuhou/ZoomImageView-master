package com.fs.ziv.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.fs.ziv.R;

/**
 * Created by fangdean on 2016/4/14.
 */
public class GgkView extends View {
    public GgkView(Context context) {
        this(context,null);
    }

    public GgkView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GgkView(Context context, AttributeSet attrs, int defStyleAttr) {
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
//        mPaint.setColor(Color.RED);
//        mPaint.setStrokeWidth(10);
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//        mPath=new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Bitmap mUpBitmap=Bitmap.createBitmap(300,100, Bitmap.Config.ARGB_8888);
//        mCanvas=new Canvas(mUpBitmap);
//        mPaint.setStyle(Paint.Style.FILL);
//        mCanvas.drawRoundRect(new RectF(0,0,getWidth(),getHeight()),30,30,mPaint);
//        mCanvas.drawBitmap(mBitmap,null,new RectF(0,0,getWidth(),getHeight()),null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        Bitmap src = makeSrc(canvasWidth, canvasHeight);
        Bitmap dst = makeDst(canvasWidth, canvasHeight);

        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(dst,0,0,null);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src,0,0,mPaint);
        mPaint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }


    private Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
//        c.drawOval(new RectF(0, 0, w*3/4, h*3/4), p);
        c.drawRoundRect(new RectF(0,0,w,h),30,30,p);
        return bm;
    }

    private Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
//        c.drawRect(w/3, h/3, w*19/20, h*19/20, p);
        c.drawBitmap(mBitmap,0,0,p);
        return bm;
    }
    //    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.drawARGB(255, 139, 197, 186);
//
//        int canvasHeight = canvas.getHeight();
//        int canvasWidth = canvas.getWidth();
//
//        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
//
//        int r=canvasHeight/3;
//        mPaint.setColor(0xFFFFCC44);
//        canvas.drawCircle(r,r,r,mPaint);
//
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//        mPaint.setColor(0xFF66AAFF);
//        canvas.drawRect(r,r,r*2.7f,r*2.7f,mPaint);
//        mPaint.setXfermode(null);
//
//        canvas.restoreToCount(layerId);
//    }

//    boolean isPath=false;
//    private float mLastX;
//    private float mLastY;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getActionMasked()){
//            case MotionEvent.ACTION_DOWN:
//                isPath=true;
//                mLastX=event.getX();
//                mLastY=event.getY();
//                mPath.moveTo(mLastX,mLastY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float x=event.getX();
//                float y=event.getY();
//
//                float dx=Math.abs(x-mLastX);
//                float dy=Math.abs(y-mLastY);
//
//                if(dx>5||dy>5){
//                    mPath.lineTo(x,y);
//                    invalidate();
//
//                    mLastX=x;
//                    mLastY=y;
//                }
//
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//
//        return true;
//    }
}
