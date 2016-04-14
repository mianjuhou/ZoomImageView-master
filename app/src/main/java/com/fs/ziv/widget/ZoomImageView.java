package com.fs.ziv.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangdean on 2016/4/12.
 */
public class ZoomImageView extends ImageView {
    private Matrix mMatrix = new Matrix();
    private Context mContext;
    private GestureDetector mGestureDetector;


    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
        mContext = context;
        mScaleGestureDetector=new ScaleGestureDetector(mContext, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scale=getScale();
                float scaleFactor = detector.getScaleFactor();
                mMatrix.postScale(//
                            scaleFactor,//
                            scaleFactor,//
                                detector.getFocusX(),//
                                detector.getFocusY());
                setImageMatrix(mMatrix);
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        });

        GestureDetector mGestureDetector = new GestureDetector(mContext,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }
        });
        setOnTouchListener(mOnTouchListener);
    }

    private float[] values=new float[9];
    private float getScale() {
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    private boolean mOnce = false;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (!mOnce) {
                mOnce = true;
                resetScaleAndTranslate(1);
            }
        }
    };

    private void resetScaleAndTranslate(float curScale) {
        //获取控件宽高
        int width = getWidth();
        int height = getHeight();

        //获取图片对象
        Drawable d = getDrawable();
        if(d==null)
            return;
        //获取图片宽高
        int iWidth = d.getIntrinsicWidth();
        int iHeight = d.getIntrinsicHeight();

        //居中
        //获取缩放后的图片中心点
        //获取控件中心点
        float dx=width/2-iWidth/2;
        float dy=height/2-iHeight/2;
        mMatrix.postTranslate(dx, dy);

        //分情况进行缩放
        float widthScale=(width*1.0f)/(iWidth*1.0f);
        float heightScale=(height*1.0f)/(iHeight*1.0f);
        float scale=widthScale>=heightScale?heightScale:widthScale;
        mMatrix.postScale(scale/curScale,scale/curScale,width/2,height/2);

        //旋转测试
//        mMatrix.postRotate(30,width/2,height/2);

        //设置图片矩阵
        setImageMatrix(mMatrix);
    }

    private ScaleGestureDetector mScaleGestureDetector;

//    private float mLastX;
//    private float mLastY;
//    private int mActiveIndex;
//    private int mActivePointerId;
//    private int mSecondPointerId;
//    private OnTouchListener mOnTouchListener = new OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            mScaleGestureDetector.onTouchEvent(event);
//            mGestureDetector.onTouchEvent(event);
//            switch (event.getActionMasked()){
//                case MotionEvent.ACTION_DOWN:
//                    mActiveIndex = event.getActionIndex();
//                    mActivePointerId = event.getPointerId(mActiveIndex);
//
//                    mLastX=event.getX(mActiveIndex);
//                    mLastY=event.getY(mActiveIndex);
//                    break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    mSecondPointerId = event.getPointerId(event.getActionIndex());
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    mActiveIndex = event.findPointerIndex(mActivePointerId);
//                    float x = event.getX(mActiveIndex);
//                    float y = event.getY(mActiveIndex);
//
//                    float dx=x-mLastX;
//                    float dy=y-mLastY;
//
//                    mMatrix.postTranslate(dx,dy);
//
//                    setImageMatrix(mMatrix);
//
//                    mLastX=x;
//                    mLastY=y;
//                    break;
//                case MotionEvent.ACTION_POINTER_UP:
//                    if(event.getPointerCount()>0){
//                        int pointerId = event.getPointerId(event.getActionIndex());
//                        if(mActivePointerId==pointerId){
//                            mActivePointerId=mSecondPointerId;
//                            mLastX=event.getX(event.findPointerIndex(mSecondPointerId));
//                            mLastY=event.getY(event.findPointerIndex(mSecondPointerId));
//                        }
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    System.out.println("ACTION_UP");
//                    break;
//            }
//            return true;
//        }
//    };



//    mMatrix.postTranslate(dx, dy);
//    mMatrix.postScale(2, 2, 50, 50);
//    mMatrix.postRotate(30, 50, 50);


    private float mLastX;
    private float mLastY;
    private double mLastArc=0;
    private int mActiveIndex;
    private int mActivePointerId;
    private List<Integer> mPointers=new ArrayList<Integer>();
    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mScaleGestureDetector.onTouchEvent(event);
//            mGestureDetector.onTouchEvent(event);
            switch (event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    mActiveIndex = event.getActionIndex();
                    mActivePointerId = event.getPointerId(mActiveIndex);
                    mPointers.add(mActivePointerId);

                    mLastX=event.getX(mActiveIndex);
                    mLastY=event.getY(mActiveIndex);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    int actionIndex = event.getActionIndex();
                    mPointers.add(event.findPointerIndex(event.getActionIndex()));
                    break;
                case MotionEvent.ACTION_MOVE:
                    int curPointerId = mPointers.get(0);
                    mActiveIndex=event.findPointerIndex(curPointerId);
                    if(mActivePointerId!=curPointerId){
                        mActivePointerId=curPointerId;
                        mLastX=event.getX(mActiveIndex);
                        mLastY=event.getY(mActiveIndex);
                    }

                    float x = event.getX(mActiveIndex);
                    float y = event.getY(mActiveIndex);

                    float dx=x-mLastX;
                    float dy=y-mLastY;
                    mMatrix.postTranslate(dx,dy);


                    if(event.getPointerCount()>1){
                        float x1=event.getX(event.findPointerIndex(mPointers.get(1)));
                        float y1=event.getY(event.findPointerIndex(mPointers.get(1)));

                        float db=y-y1;
                        float lb=x-x1;
                        double atan = Math.atan(db / lb);
                        double degrees = Math.toDegrees(atan);
                        if(mLastArc==0){
                            mLastArc=degrees;
                        }else {
                            float dArc=(float)(degrees-mLastArc);
                            mMatrix.postRotate(dArc,(x+x1)/2,(y+y1)/2);
                            mLastArc=degrees;
                        }
                    }

                    setImageMatrix(mMatrix);

                    mLastX=x;
                    mLastY=y;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    int pointerUpId = event.getPointerId(event.getActionIndex());
                    int i = 0;
                    for (; i < mPointers.size(); i++) {
                        if (mPointers.get(i)==pointerUpId){
                            break;
                        }
                    }
                    mPointers.remove(i);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mPointers.clear();
                    System.out.println("ACTION_UP");
                    break;
            }
            return true;
        }
    };

}
