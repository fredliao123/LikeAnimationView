package bupt.liao.fred.lavview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import bupt.liao.fred.lavview.R;
import bupt.liao.fred.lavview.model.LavPoint;
import bupt.liao.fred.lavview.utils.LavUtils;


/**
 * Created by Fred.Liao on 2017/11/18.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class LikeUpView extends LinearLayout implements View.OnClickListener {
    public static final float DEFAULT_DRAWABLE_PADDING = 4f;
    private LikeView mLikeView;
    private CountView mCountView;

    private float mDrawablePadding;
    private int mTextColor;
    private int mCount;
    private float mTextSize;
    private boolean mIsLikeUp;
    private int mTopMargin;
    private boolean mNeedChangeChildView;

    public LikeUpView(Context context) {
        this(context, null);
    }

    public LikeUpView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeUpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LikeUpView);
        mDrawablePadding = typedArray.getDimension(R.styleable.LikeUpView_lav_drawable_padding, LavUtils.dip2px(context, DEFAULT_DRAWABLE_PADDING));
        mCount = typedArray.getInt(R.styleable.LikeUpView_lav_count, 0);
        mTextColor = typedArray.getColor(R.styleable.LikeUpView_lav_text_color, Color.parseColor(CountView.DEFAULT_TEXT_COLOR));
        mTextSize = typedArray.getDimension(R.styleable.LikeUpView_lav_drawable_padding, LavUtils.sp2px(context, CountView.DEFAULT_TEXT_SIZE));
        mIsLikeUp = typedArray.getBoolean(R.styleable.LikeUpView_lav_isLikeUp, false);
        typedArray.recycle();
        init();
    }

    private void init() {
        removeAllViews();
        setClipChildren(false);
        setOrientation(LinearLayout.HORIZONTAL);

        addLikeView();

        addCountView();

        setPadding(0, 0, 0, 0, false);
        setOnClickListener(this);
    }

    public LikeUpView setCount(int mCount) {
        this.mCount = mCount;
        mCountView.setCount(mCount);
        return this;
    }

    public LikeUpView setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        mCountView.setTextColor(mCount);
        return this;
    }

    public LikeUpView setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        mCountView.setTextSize(mCount);
        return this;
    }

    public LikeUpView setLikeUp(boolean isLikeUp) {
        this.mIsLikeUp = isLikeUp;
        mLikeView.setIsLikeUp(mIsLikeUp);
        return this;
    }

    public void setLikeUpClickListener(LikeView.LikeUpClickListener listener){
        mLikeView.setLikeUpClickListener(listener);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        if (mNeedChangeChildView) {
            resetLikeParams();
            resetCountViewParams();
            mNeedChangeChildView = false;
        } else {
            super.setPadding(left, top, right, bottom);
        }
    }

    private void resetLikeParams() {
        LayoutParams params = (LayoutParams) mLikeView.getLayoutParams();
        if (mTopMargin < 0) {
            params.topMargin = mTopMargin;
        }
        params.leftMargin = getPaddingLeft();
        params.topMargin += getPaddingTop();
        params.bottomMargin = getPaddingBottom();
        mLikeView.setLayoutParams(params);
    }

    private void resetCountViewParams() {
        LayoutParams params = (LayoutParams) mCountView.getLayoutParams();
        if (mTopMargin > 0) {
            params.topMargin = mTopMargin;
        }
        params.leftMargin = (int) mDrawablePadding;
        params.topMargin += getPaddingTop();
        params.bottomMargin = getPaddingBottom();
        params.rightMargin = getPaddingRight();
        mCountView.setLayoutParams(params);
    }

    @SuppressWarnings("SameParameterValue")
    public void setPadding(int left, int top, int right, int bottom, boolean needChange) {
        this.mNeedChangeChildView = needChange;
        setPadding(left, top, right, bottom);
    }

    private void addLikeView() {
        mLikeView = new LikeView(getContext());
        mLikeView.setIsLikeUp(mIsLikeUp);
        LavPoint circlePoint = mLikeView.getCirclePoint();
        mTopMargin = (int) (circlePoint.y - mTextSize / 2);
        addView(mLikeView, getLikeParams());
    }

    private void addCountView() {
        mCountView = new CountView(getContext());
        mCountView.setTextColor(mTextColor);
        mCountView.setTextSize(mTextSize);
        mCountView.setCount(mCount);

        addView(mCountView, getCountParams());
    }

    private LayoutParams getLikeParams() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTopMargin < 0) {
            params.topMargin = mTopMargin;
        }
        params.leftMargin = getPaddingLeft();
        params.topMargin += getPaddingTop();
        params.bottomMargin = getPaddingBottom();
        return params;
    }

    private LayoutParams getCountParams() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTopMargin > 0) {
            params.topMargin = mTopMargin;
        }
        params.leftMargin = (int) mDrawablePadding;
        params.topMargin += getPaddingTop();
        params.bottomMargin = getPaddingBottom();
        params.rightMargin = getPaddingRight();
        return params;
    }

    @Override
    public void onClick(View v) {
        mIsLikeUp = !mIsLikeUp;
        if (mIsLikeUp) {
            mCountView.calculateChangeNum(1);
        } else {
            mCountView.calculateChangeNum(-1);
        }
        mLikeView.startAnim();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle data = new Bundle();
        data.putParcelable("superData", super.onSaveInstanceState());
        data.putInt("count", mCount);
        data.putFloat("textSize", mTextSize);
        data.putInt("textColor", mTextColor);
        data.putBoolean("isLikeUp", mIsLikeUp);
        data.putFloat("drawablePadding", mDrawablePadding);
        return data;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle data = (Bundle) state;
        Parcelable superData = data.getParcelable("superData");
        super.onRestoreInstanceState(superData);
        mCount = data.getInt("count");
        mTextSize = data.getFloat("textSize", LavUtils.sp2px(getContext(), CountView.DEFAULT_TEXT_SIZE));
        mTextColor = data.getInt("textColor", Color.parseColor(CountView.DEFAULT_TEXT_COLOR));
        mIsLikeUp = data.getBoolean("isLikeUp", false);
        mDrawablePadding = data.getFloat("drawablePadding", LavUtils.sp2px(getContext(), DEFAULT_DRAWABLE_PADDING));
        init();
    }
}

