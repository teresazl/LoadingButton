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
    private SpotView spotViews[];

    private float start;
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
        spotViews = new SpotView[DEFAULT_SPOT_COUNT];

        for (int i = 0; i < DEFAULT_SPOT_COUNT; i++) {
            paints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            paints[i].setColor(mSpotColor);
            spotViews[i] = new SpotView();
            spotViews[i].setX(start);

            animators[i] = ObjectAnimator.ofFloat(spotViews[i], "x", start, end);
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

        start = -mSpotRadius;
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

        for (int i = 0; i < DEFAULT_SPOT_COUNT; i++) {
            canvas.drawCircle(spotViews[i].getX(), mHeight, mSpotRadius, paints[i]);
            invalidate();
        }
    }

    public void start() {
        for (int i = 0; i < DEFAULT_SPOT_COUNT; i++) {
            animators[i].start();
        }
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
