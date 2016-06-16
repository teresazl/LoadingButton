package com.teresazl.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

/**
 * Created by teresa on 2016-6-16.
 */
public class FlatButton extends Button {

    private StateListDrawable mNormalDrawable;
    private CharSequence mNormalText;
    private float mCornerRadius;

    public FlatButton(Context context) {
        this(context, null);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mNormalDrawable = new StateListDrawable();
        obtainStyledAttrs(attrs);
        mNormalText = getText().toString();
        setBackgroundCompat(mNormalDrawable);
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FlatButton);

        float defValue = getDimension(R.dimen.corner_radius);
        mCornerRadius = typedArray.getDimension(R.styleable.FlatButton_radius_corner, defValue);

        mNormalDrawable.addState(new int[]{android.R.attr.state_focused},
                createPressedDrawable(typedArray));
        mNormalDrawable.addState(new int[]{android.R.attr.state_pressed},
                createPressedDrawable(typedArray));
        mNormalDrawable.addState(new int[]{android.R.attr.state_selected},
                createPressedDrawable(typedArray));
        mNormalDrawable.addState(new int[]{},
                createNormalDrawable(typedArray));

        typedArray.recycle();
    }

    private GradientDrawable createNormalDrawable(TypedArray attr) {
        GradientDrawable drawableNormal =
                (GradientDrawable) getDrawable(R.drawable.rect_normal).mutate();
        drawableNormal.setCornerRadius(getCornerRadius());

        int blueNormal = getColor(R.color.blue_normal);
        int colorNormal = attr.getColor(R.styleable.FlatButton_normal_color, blueNormal);
        drawableNormal.setColor(colorNormal);

        return drawableNormal;
    }

    private Drawable createPressedDrawable(TypedArray attr) {
        GradientDrawable drawablePressed =
                (GradientDrawable) getDrawable(R.drawable.rect_pressed).mutate();
        drawablePressed.setCornerRadius(getCornerRadius());

        int blueDark = getColor(R.color.blue_pressed);
        int colorPressed = attr.getColor(R.styleable.FlatButton_pressed_color, blueDark);
        drawablePressed.setColor(colorPressed);

        return drawablePressed;
    }

    protected Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    protected float getDimension(int id) {
        return getResources().getDimension(id);
    }

    protected int getColor(int id) {
        return getResources().getColor(id);
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public StateListDrawable getNormalDrawable() {
        return mNormalDrawable;
    }

    public CharSequence getNormalText() {
        return mNormalText;
    }

    public void setNormalText(CharSequence normalText) {
        mNormalText = normalText;
    }

    /**
     * Set the View's background. Masks the API changes made in Jelly Bean.
     *
     * @param drawable
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setBackgroundCompat(Drawable drawable) {
        int pL = getPaddingLeft();
        int pT = getPaddingTop();
        int pR = getPaddingRight();
        int pB = getPaddingBottom();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
        setPadding(pL, pT, pR, pB);
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

}
