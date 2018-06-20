package toutiao.fake.com.faketoutiao.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import toutiao.fake.com.faketoutiao.ui.adpater.MicroAdapter;

/**
 * Created by lihaitao on 2018/6/20.
 */
public class PullLoadRecyclerview extends RecyclerView {
    private MicroAdapter mAdapter;
    private int mHeaderHeight;
    private int rawY = 0;
    private static final int STATUS_IDLE = 0;
    private static final int STATUS_DOWN = 1;
    private static final int STATUS_READY = 2;
    private static final int STATUS_FRESHING = 3;
    private int mCurrent_status = 0;
    private MicroHeaderView mHeaderView;
    private float mDragIndex = 0.35f;
    onFreshListener mFreshListener;
    public PullLoadRecyclerview(Context context) {
        super(context);
    }

    public PullLoadRecyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullLoadRecyclerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setOnFreshListener(onFreshListener freshListener){
        mFreshListener= freshListener;
    }
    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof MicroHeaderView) {
            mAdapter = (MicroAdapter) getAdapter();
            mHeaderView = ((MicroHeaderView) mAdapter.getHeaderView());
            mHeaderView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            mHeaderHeight = mHeaderView.getMeasuredHeight();
            mHeaderView.setOnFinish(new MicroHeaderView.onFinish() {
                @Override
                public void onfinish() {
                    MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
                    layoutParams.topMargin=-mHeaderHeight+1;
                    mHeaderView.setLayoutParams(layoutParams);
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mAdapter == null || mHeaderView == null) {
            return super.onTouchEvent(e);
        }
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                rawY = (int) e.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                restorReshView();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (canScrollVertically(-1) || mCurrent_status == STATUS_FRESHING) {
                    return super.onTouchEvent(e);
                }
                float distanceY = (e.getRawY() - rawY) * mDragIndex;
                if (distanceY > 0) {
                    float marginTop = distanceY - mHeaderHeight;
                    setFreshViewMarginTop(marginTop);
                    updateStatus(marginTop);
                }
                refresh();
                return true;
            default:
                return super.onTouchEvent(e);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (mHeaderView != null && mHeaderHeight > 0) {
                mHeaderHeight = mHeaderView.getMeasuredHeight();
                if (mHeaderHeight > 0) {
                    setFreshViewMarginTop(-mHeaderHeight + 1);
                }
            }
        }
    }
    private void restorReshView() {
        int currenttopMargin = ((MarginLayoutParams) mHeaderView.getLayoutParams()).topMargin;
        int finalMatgin = -mHeaderHeight + 1;
        if (mCurrent_status == STATUS_READY) {
            finalMatgin = 0;
            mCurrent_status = STATUS_FRESHING;
            refresh();
        }
        int distatnce = currenttopMargin - finalMatgin;
        ValueAnimator animator = ObjectAnimator.ofFloat(currenttopMargin, finalMatgin).setDuration(distatnce);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                setFreshViewMarginTop(value);
            }
        });
        animator.start();
    }
    private void updateStatus(float marginTop) {
        if (marginTop <= -mHeaderHeight) {
            mCurrent_status = STATUS_IDLE;
        } else if (marginTop < 0) {
            mCurrent_status = STATUS_DOWN;
        } else {
            mCurrent_status = STATUS_READY;
        }
        refresh();
    }
    private void setFreshViewMarginTop(float marginTop) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
        if (marginTop < -mHeaderHeight + 1) {
            marginTop = mHeaderHeight + 1;
        }
        layoutParams.topMargin = (int) marginTop;
        mHeaderView.setLayoutParams(layoutParams);

    }
    public void stopFresh(){
        mCurrent_status=STATUS_IDLE;
        refresh();
    }
    private void refresh() {
        switch (mCurrent_status) {
            case STATUS_DOWN:
                mHeaderView.down_fresh();
                break;
            case STATUS_FRESHING:
                if(mFreshListener!=null){
                    mFreshListener.onFreshing();
                }
                mHeaderView.freshing();
                break;
            case STATUS_READY:
                mHeaderView.ready_fresh();
                break;
            case STATUS_IDLE:
                mHeaderView.freshed();
                break;
        }
    }
   public  interface onFreshListener{
         void onFreshing();
    }
}