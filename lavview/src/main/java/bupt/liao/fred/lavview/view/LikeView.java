package bupt.liao.fred.lavview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import bupt.liao.fred.lavview.R;
import bupt.liao.fred.lavview.model.LavPoint;
import bupt.liao.fred.lavview.utils.LavUtils;


/**
 * Created by Fred.Liao on 2017/11/18.
 * Email:fredliaobupt@qq.com
 * Description: The Thumb part of the View
 */

public class LikeView extends View {
    //Color for circle
    private static final int START_COLOR = Color.parseColor("#00e24d3d");
    private static final int END_COLOR = Color.parseColor("#88e24d3d");
    //Time for Scale animation
    private static final int SCALE_DURING = 150;
    //Time for cirle expanding
    private static final int RADIUS_DURING = 100;

    private static final float SCALE_MIN = 0.9f;
    private static final float SCALE_MAX = 1f;

    private Bitmap mLikeUp;
    private Bitmap mShining;
    private Bitmap mLikeNormal;
    private Paint mBitmapPaint;

    private float mLikeWidth;
    private float mLikeHeight;
    private float mShiningWidth;
    private float mShiningHeight;

    private LavPoint mShiningPoint;
    private LavPoint mLikePoint;
    private LavPoint mCirclePoint;

    private float mRadiusMax;
    private float mRadiusMin;
    private float mRadius;
    private Path mClipPath;
    private Paint mCirclePaint;

    private boolean mIsLikeUp;
    private long mLastStartTime;
    //Call back for click
    private LikeUpClickListener mLikeUpClickListener;

    //Click number, even number
    private int mClickCount;
    private int mEndCount;
    private AnimatorSet mLikeUpAnim;

    public LikeView(Context context) {
        this(context, null);
    }

    public LikeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        initBitmapInfo();

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(LavUtils.dip2px(getContext(), 2));

        mCirclePoint = new LavPoint();
        mCirclePoint.x = mLikePoint.x + mLikeWidth / 2;
        mCirclePoint.y = mLikePoint.y + mLikeHeight / 2;

        mRadiusMax = Math.max(mCirclePoint.x - getPaddingLeft(), mCirclePoint.y - getPaddingTop());
        mRadiusMin = LavUtils.dip2px(getContext(), 8);
        mClipPath = new Path();
        mClipPath.addCircle(mCirclePoint.x, mCirclePoint.y, mRadiusMax, Path.Direction.CW);
    }

    private void initBitmapInfo() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        resetBitmap();

        mLikeWidth = mLikeUp.getWidth();
        mLikeHeight = mLikeUp.getHeight();

        mShiningWidth = mShining.getWidth();
        mShiningHeight = mShining.getHeight();

        mShiningPoint = new LavPoint();
        mLikePoint = new LavPoint();
        mShiningPoint.x = getPaddingLeft() + LavUtils.dip2px(getContext(), 2);
        mShiningPoint.y = getPaddingTop();
        mLikePoint.x = getPaddingLeft();
        mLikePoint.y = getPaddingTop() + LavUtils.dip2px(getContext(), 8);
    }

    private void resetBitmap() {
        mLikeUp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        mLikeNormal = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        mShining = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);
    }

    public void setIsLikeUp(boolean isLikeUp) {
        this.mIsLikeUp = isLikeUp;
        mClickCount = mIsLikeUp ? 1 : 0;
        mEndCount = mClickCount;
        postInvalidate();
    }

    public boolean isLikeUp() {
        return mIsLikeUp;
    }

    public void setLikeUpClickListener(LikeUpClickListener LikeUpClickListener) {
        this.mLikeUpClickListener = LikeUpClickListener;
    }

    public LavPoint getCirclePoint() {
        return mCirclePoint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(LavUtils.getDefaultSize(widthMeasureSpec, getContentWidth() + getPaddingLeft() + getPaddingRight()),
                LavUtils.getDefaultSize(heightMeasureSpec, getContentHeight() + getPaddingTop() + getPaddingBottom()));
    }

    private int getContentWidth() {
        float minLeft = Math.min(mShiningPoint.x, mLikePoint.x);
        float maxRight = Math.max(mShiningPoint.x + mShiningWidth, mLikePoint.x + mLikeWidth);
        return (int) (maxRight - minLeft);
    }

    private int getContentHeight() {
        float minTop = Math.min(mShiningPoint.y, mLikePoint.y);
        float maxBottom = Math.max(mShiningPoint.y + mShiningHeight, mLikePoint.y + mLikeHeight);
        return (int) (maxBottom - minTop);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle data = new Bundle();
        data.putParcelable("superData", super.onSaveInstanceState());
        data.putBoolean("isLikeUp", mIsLikeUp);
        return data;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle data = (Bundle) state;
        Parcelable superData = data.getParcelable("superData");
        super.onRestoreInstanceState(superData);

        mIsLikeUp = data.getBoolean("isLikeUp", false);

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsLikeUp) {
            if (mClipPath != null) {
                canvas.save();
                canvas.clipPath(mClipPath);
                canvas.drawBitmap(mShining, mShiningPoint.x, mShiningPoint.y, mBitmapPaint);
                canvas.restore();
                canvas.drawCircle(mCirclePoint.x, mCirclePoint.y, mRadius, mCirclePaint);
            }
            canvas.drawBitmap(mLikeUp, mLikePoint.x, mLikePoint.y, mBitmapPaint);
        } else {
            canvas.drawBitmap(mLikeNormal, mLikePoint.x, mLikePoint.y, mBitmapPaint);
        }
    }

    public void startAnim() {
        mClickCount++;
        boolean isFastAnim = false;
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mLastStartTime < 300) {
            isFastAnim = true;
        }
        mLastStartTime = currentTimeMillis;

        if (mIsLikeUp) {
            if (isFastAnim) {
                startFastAnim();
                return;
            }
            startLikeDownAnim();
            mClickCount = 0;
        } else {
            if (mLikeUpAnim != null) {
                mClickCount = 0;
            } else {
                startLikeUpAnim();
                mClickCount = 1;
            }
        }
        mEndCount = mClickCount;
    }

    private void startFastAnim() {
        ObjectAnimator LikeUpScale = ObjectAnimator.ofFloat(this, "LikeUpScale", SCALE_MIN, SCALE_MAX);
        LikeUpScale.setDuration(SCALE_DURING);
        LikeUpScale.setInterpolator(new OvershootInterpolator());

        ObjectAnimator circleScale = ObjectAnimator.ofFloat(this, "circleScale", mRadiusMin, mRadiusMax);
        LikeUpScale.setDuration(RADIUS_DURING);

        AnimatorSet set = new AnimatorSet();
        set.play(LikeUpScale).with(circleScale);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mEndCount++;
                if (mClickCount != mEndCount) {
                    return;
                }
                if (mClickCount % 2 == 0) {
                    startLikeDownAnim();
                } else {
                    if (mLikeUpClickListener != null) {
                        mLikeUpClickListener.LikeUpFinish();
                    }
                }
            }
        });
        set.start();
    }

    private void startLikeDownAnim() {
        ObjectAnimator LikeUpScale = ObjectAnimator.ofFloat(this, "LikeUpScale", SCALE_MIN, SCALE_MAX);
        LikeUpScale.setDuration(SCALE_DURING);
        LikeUpScale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsLikeUp = false;
                setNotLikeUpScale(SCALE_MAX);
                if (mLikeUpClickListener != null) {
                    mLikeUpClickListener.LikeDownFinish();
                }
            }
        });
        LikeUpScale.start();
    }

    private void startLikeUpAnim() {
        ObjectAnimator notLikeUpScale = ObjectAnimator.ofFloat(this, "notLikeUpScale", SCALE_MAX, SCALE_MIN);
        notLikeUpScale.setDuration(SCALE_DURING);
        notLikeUpScale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsLikeUp = true;
            }
        });

        ObjectAnimator LikeUpScale = ObjectAnimator.ofFloat(this, "LikeUpScale", SCALE_MIN, SCALE_MAX);
        LikeUpScale.setDuration(SCALE_DURING);
        LikeUpScale.setInterpolator(new OvershootInterpolator());

        ObjectAnimator circleScale = ObjectAnimator.ofFloat(this, "circleScale", mRadiusMin, mRadiusMax);
        LikeUpScale.setDuration(RADIUS_DURING);

        mLikeUpAnim = new AnimatorSet();
        mLikeUpAnim.play(LikeUpScale).with(circleScale);
        mLikeUpAnim.play(LikeUpScale).after(notLikeUpScale);
        mLikeUpAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mLikeUpAnim = null;
                if (mLikeUpClickListener != null) {
                    mLikeUpClickListener.LikeUpFinish();
                }
            }
        });
        mLikeUpAnim.start();
    }

    private void setNotLikeUpScale(float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        mLikeNormal = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        mLikeNormal = Bitmap.createBitmap(mLikeNormal, 0, 0, mLikeNormal.getWidth(), mLikeNormal.getHeight(),
                matrix, true);
        postInvalidate();
    }

    private void setLikeUpScale(float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        mLikeUp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        mLikeUp = Bitmap.createBitmap(mLikeUp, 0, 0, mLikeUp.getWidth(), mLikeUp.getHeight(),
                matrix, true);
        postInvalidate();
    }

    private void setShiningScale(float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        mShining = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);
        mShining = Bitmap.createBitmap(mShining, 0, 0, mShining.getWidth(), mShining.getHeight(),
                matrix, true);
        postInvalidate();
    }

    public void setCircleScale(float radius) {
        mRadius = radius;
        mClipPath = new Path();
        mClipPath.addCircle(mCirclePoint.x, mCirclePoint.y, mRadius, Path.Direction.CW);
        float fraction = (mRadiusMax - radius) / (mRadiusMax - mRadiusMin);
        mCirclePaint.setColor((int) LavUtils.evaluate(fraction, START_COLOR, END_COLOR));
        postInvalidate();
    }

    public interface LikeUpClickListener {
        //点赞回调
        void LikeUpFinish();

        //取消回调
        void LikeDownFinish();
    }

}

