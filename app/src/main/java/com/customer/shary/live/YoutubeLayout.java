package com.customer.shary.live;

//from   w  w w.ja  v  a2s. co m
import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * Created by Flavien Laurent (flavienlaurent.com) on 23/08/13.
 */
public class YoutubeLayout extends ViewGroup {

    private final ViewDragHelper mDragHelper;

    private View mHeaderView;
    private View mDescView;

    private float mInitialMotionX;
    private float mInitialMotionY;

    private VelocityTracker mVelocityTracker = null;

    private int mVerticalDragRange;
    private int mHorizontalDragRange;

    private int mTop;
    private float mDragOffset;

    private boolean mIsMaximized = true;

    private Context mContext;

    public YoutubeLayout(Context context) {
        this(context, null);
    }

    public YoutubeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * to use in an xml definition
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public YoutubeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = findViewById(R.id.header);
        mDescView = findViewById(R.id.desc);
    }

    public void maximize() {
        if (smoothVerticalSlideTo(0f))
            mIsMaximized = true;
    }

    public void minimize() {
        if (smoothVerticalSlideTo(1f))
            mIsMaximized = false;
    }

    boolean smoothVerticalSlideTo(float slideOffset) {
        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * mVerticalDragRange);

        if (mDragHelper
                .smoothSlideViewTo(mHeaderView, mHeaderView.getLeft(), y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    boolean smoothHorizontalSlideTo(float slideOffset) {
        return false;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mHeaderView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            mTop = top;

            mDragOffset = (float) top / mVerticalDragRange;

            mHeaderView.setPivotX(mHeaderView.getWidth());
            mHeaderView.setPivotY(mHeaderView.getHeight());
            mHeaderView.setScaleX(1 - mDragOffset / 2);
            mHeaderView.setScaleY(1 - mDragOffset / 2);

            mDescView.setAlpha(1 - mDragOffset);

            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int top = getPaddingTop();
            if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                top += mVerticalDragRange;
            }
            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mVerticalDragRange;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mHorizontalDragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mHeaderView.getHeight()
                    - mHeaderView.getPaddingBottom();

            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - mHeaderView.getWidth();

            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);

            return newLeft;
        }

    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if ((action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        final float x = ev.getX();
        final float y = ev.getY();
        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                interceptTap = mDragHelper.isViewUnder(mHeaderView, (int) x,
                        (int) y);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = mDragHelper.getTouchSlop();
                /* useless */
                if (ady > slop && adx > ady) {
                    mDragHelper.cancel();
                    return false;
                }
            }
        }

        return !(mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap);
    }

    float vX = 0, vY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);

        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();

        final int index = ev.getActionIndex();
        final int pointerId = ev.getPointerId(index);

        boolean isHeaderViewUnder = mDragHelper.isViewUnder(mHeaderView,
                (int) x, (int) y);
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mVelocityTracker == null)
                    mVelocityTracker = VelocityTracker.obtain();
                else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(ev);

                mInitialMotionX = x;
                mInitialMotionY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(ev);
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                mVelocityTracker.computeCurrentVelocity(1000);
                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                vX = VelocityTrackerCompat
                        .getXVelocity(mVelocityTracker, pointerId);
                vY = VelocityTrackerCompat
                        .getYVelocity(mVelocityTracker, pointerId);

                break;

            case MotionEvent.ACTION_UP:
                Log.d("", "X velocity: " + vX);
                Log.d("", "Y velocity: " + vY);

                final float dx = x - mInitialMotionX;
                final float dy = y - mInitialMotionY;
                final int slop = mDragHelper.getTouchSlop();
                if (isHeaderViewUnder) {
                    if (dx * dx + dy * dy < slop * slop) {
                        if (mLayoutListener != null)
                            mLayoutListener.onHeadViewClickListener(mHeaderView);
                    } else {
                        if (vY > 0) {
                            minimize();
                        } else {
                            maximize();
                        }
                    }
                }

                break;
        }

        return isHeaderViewUnder && isViewHit(mHeaderView, (int) x, (int) y)
                || isViewHit(mDescView, (int) x, (int) y);
    }

    private boolean isViewHit(View view, int x, int y) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0]
                && screenX < viewLocation[0] + view.getWidth()
                && screenY >= viewLocation[1]
                && screenY < viewLocation[1] + view.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mVerticalDragRange = getHeight() - mHeaderView.getHeight();
        mHorizontalDragRange = mDescView.getWidth();

        mHeaderView.layout(0, mTop, r, mTop + mHeaderView.getMeasuredHeight());

        mDescView
                .layout(0, mTop + mHeaderView.getMeasuredHeight(), r, mTop + b);

        if (mTop + mHeaderView.getMeasuredHeight() >= getHeight()) {
            // TODO
        } else {
            // TODO
        }
    }

    private YoutubeLayoutListener mLayoutListener;

    public void setYoutubeLayoutListener(YoutubeLayoutListener listener) {
        this.mLayoutListener = listener;
    }

    public interface YoutubeLayoutListener {
        public void onHeadViewClickListener(View view);
    }

}