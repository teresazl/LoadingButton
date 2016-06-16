package com.teresazl.library.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.teresazl.library.ProcessButton;
import com.teresazl.library.R;

/**
 * Created by teresa on 2016-6-16.
 */
public class LoadingButton extends ProcessButton {

    private static final int DELAY = 150;
    private static final int DURATION = 1500;

    private static final int DEFAULT_SPOT_COUNT = 5;
    private static final int DEFAULT_SPOT_RADIUS = 4;
    private static final int DEFAULT_SPOT_COLOR = 0XFFFFFFFF;

    private Paint paints[];

    private float cx0 = -10;
    private float cx1 = -10;
    private float cx2 = -10;
    private float cx3 = -10;
    private float cx4 = -10;

    private float start = -10;
    private float end;
    private int  mHeight;

    private int mSpotRadius = dp2px(DEFAULT_SPOT_RADIUS);
    private int mSpotColor = DEFAULT_SPOT_COLOR;

    private ObjectAnimator animators[];

    private boolean isRunning = false;

    public LoadingButton(Context context) {
        this(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyledAttrs(attrs);
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingButton);

        mSpotColor = typedArray.getColor(R.styleable.LoadingButton_spot_color, mSpotColor);
        mSpotRadius = (int) typedArray.getDimension(R.styleable.LoadingButton_spot_radius, mSpotRadius);

        typedArray.recycle();
    }

    private void init() {

        paints = new Paint[DEFAULT_SPOT_COUNT];
        animators = new ObjectAnimator[DEFAULT_SPOT_COUNT];

        for (int i = 0; i < DEFAULT_SPOT_COUNT; i++) {
            paints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            paints[i].setColor(mSpotColor);

            animators[i] = ObjectAnimator.ofFloat(this, "cx" + i, start, end);
            animators[i].setDuration(DURATION);
            if (i == 0) {
                animators[i].start();
            } else {
                animators[i].setStartDelay(i * DELAY);
            }
            animators[i].setInterpolator(new DecelerateAccelerateInterpolator());

            if (i == DEFAULT_SPOT_COUNT - 1) {
                animators[i].addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        start();
                    }
                });
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int measuredWidth, measuredHeight;

        measuredWidth = widthSize;
        measuredHeight = heightSize;

        setMeasuredDimension(measuredWidth, measuredHeight);

        end = -start + getMeasuredWidth() + mSpotRadius;
        mHeight = (measuredHeight - mSpotRadius) / 2;
        init();
    }

    @Override
    public void drawProgress(Canvas canvas) {
        if (!isRunning) {
            start();
            isRunning = true;
        }

        canvas.drawCircle(cx0, mHeight, mSpotRadius, paints[0]);
        canvas.drawCircle(cx1, mHeight, mSpotRadius, paints[1]);
        canvas.drawCircle(cx2, mHeight, mSpotRadius, paints[2]);
        canvas.drawCircle(cx3, mHeight, mSpotRadius, paints[3]);
        canvas.drawCircle(cx4, mHeight, mSpotRadius, paints[4]);
    }

    public void start() {
        for (int i = 0; i < DEFAULT_SPOT_COUNT; i++) {
            animators[i].start();
        }
    }

    public float getCx0() {
        return cx0;
    }

    public void setCx0(float cx0) {
        this.cx0 = cx0;
        invalidate();
    }

    public float getCx1() {
        return cx1;
    }

    public void setCx1(float cx1) {
        this.cx1 = cx1;
        invalidate();
    }

    public float getCx2() {
        return cx2;
    }

    public void setCx2(float cx2) {
        this.cx2 = cx2;
        invalidate();
    }

    public float getCx3() {
        return cx3;
    }

    public void setCx3(float cx3) {
        this.cx3 = cx3;
        invalidate();
    }

    public float getCx4() {
        return cx4;
    }

    public void setCx4(float cx4) {
        this.cx4 = cx4;
        invalidate();
    }

    private class DecelerateAccelerateInterpolator implements TimeInterpolator {

        private double POW = 1.0 / 2.0;

        @Override
        public float getInterpolation(float input) {
            return input < 0.5
                    ? (float) Math.pow(input * 2, POW) * 0.5f
                    : (float) Math.pow((1 - input) * 2, POW) * -0.5f + 1;
        }
    }

}