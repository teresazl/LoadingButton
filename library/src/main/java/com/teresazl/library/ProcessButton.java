package com.teresazl.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

/**
 * Created by teresa on 2016-6-16.
 */
public abstract class ProcessButton extends FlatButton {

    private int mProgress;
    private int mMaxProgress;
    private int mMinProgress;

    private GradientDrawable mProgressDrawable;
    private GradientDrawable mCompleteDrawable;
    private GradientDrawable mErrorDrawable;

    private CharSequence mCompleteText;
    private CharSequence mErrorText;

    public ProcessButton(Context context) {
        this(context, null);
    }

    public ProcessButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
        obtainStyledAttrs(attrs);
    }

    private void init() {
        mMinProgress = 0;
        mMaxProgress = 100;

        mProgressDrawable = (GradientDrawable) getDrawable(R.drawable.rect_progress).mutate();

        mCompleteDrawable = (GradientDrawable) getDrawable(R.drawable.rect_complete).mutate();
        mCompleteDrawable.setCornerRadius(getCornerRadius());

        mErrorDrawable = (GradientDrawable) getDrawable(R.drawable.rect_error).mutate();
        mErrorDrawable.setCornerRadius(getCornerRadius());
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProcessButton);

        mCompleteText = typedArray.getString(R.styleable.ProcessButton_complete_text);
        mErrorText = typedArray.getString(R.styleable.ProcessButton_error_text);

        int purple = getColor(R.color.purple_progress);
        int colorProgress = typedArray.getColor(R.styleable.ProcessButton_progress_color, purple);
        mProgressDrawable.setColor(colorProgress);

        int green = getColor(R.color.green_complete);
        int colorComplete = typedArray.getColor(R.styleable.ProcessButton_complete_color, green);
        mCompleteDrawable.setColor(colorComplete);

        int red = getColor(R.color.red_error);
        int colorError = typedArray.getColor(R.styleable.ProcessButton_error_color, red);
        mErrorDrawable.setColor(colorError);

        typedArray.recycle();
    }

    private LoadingListener listener;

    public interface LoadingListener {
        void loadComplete();
        void loadError();
    }

    public void setOnLoadingListener(LoadingListener listener) {
        this.listener = listener;
    }

    private void invokeComplete() {
        if (listener != null) {
            listener.loadComplete();
        }
    }

    private void invokeError() {
        if (listener != null) {
            listener.loadError();
        }
    }

    public void setProgress(int progress) {
        mProgress = progress;

        if (mProgress == mMinProgress) {
            onNormalState();
        } else if (mProgress >= mMaxProgress) {
            onCompleteState();
            invokeComplete();
        } else if (mProgress < mMinProgress) {
            onErrorState();
            invokeError();
        } else {
            onProgress();
        }

        invalidate();
    }

    protected void onErrorState() {
        if (getErrorText() != null) {
            setText(getErrorText());
        }
        setBackgroundCompat(getErrorDrawable());
    }

    protected void onProgress() {
        setText("");
        setBackgroundCompat(getProgressDrawable());
    }

    protected void onCompleteState() {
        if (getCompleteText() != null) {
            setText(getCompleteText());
        }
        setBackgroundCompat(getCompleteDrawable());
    }

    protected void onNormalState() {
        if (getNormalText() != null) {
            setText(getNormalText());
        }
        setBackgroundCompat(getNormalDrawable());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // progress
        if (mProgress > mMinProgress && mProgress < mMaxProgress) {
            drawProgress(canvas);
        }

        super.onDraw(canvas);
    }

    public abstract void drawProgress(Canvas canvas);

    public int getProgress() {
        return mProgress;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public int getMinProgress() {
        return mMinProgress;
    }

    public GradientDrawable getProgressDrawable() {
        return mProgressDrawable;
    }

    public GradientDrawable getCompleteDrawable() {
        return mCompleteDrawable;
    }

    public CharSequence getCompleteText() {
        return mCompleteText;
    }

    public void setProgressDrawable(GradientDrawable progressDrawable) {
        mProgressDrawable = progressDrawable;
    }

    public void setCompleteDrawable(GradientDrawable completeDrawable) {
        mCompleteDrawable = completeDrawable;
    }

    public void setNormalText(CharSequence normalText) {
        super.setNormalText(normalText);
        if (mProgress == mMinProgress) {
            setText(normalText);
        }
    }

    public void setCompleteText(CharSequence completeText) {
        mCompleteText = completeText;
    }

    public GradientDrawable getErrorDrawable() {
        return mErrorDrawable;
    }

    public void setErrorDrawable(GradientDrawable errorDrawable) {
        mErrorDrawable = errorDrawable;
    }

    public CharSequence getErrorText() {
        return mErrorText;
    }

    public void setErrorText(CharSequence errorText) {
        mErrorText = errorText;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mProgress = mProgress;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            mProgress = savedState.mProgress;
            super.onRestoreInstanceState(savedState.getSuperState());
            setProgress(mProgress);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    /**
     * A {@link android.os.Parcelable} representing the {@link com.teresazl.library.ProcessButton}'s
     * state.
     */
    public static class SavedState extends BaseSavedState {

        private int mProgress;

        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            mProgress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mProgress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
