package com.caijia.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * Created by cai.jia on 2015/9/1.
 */
public class ExpandableLayout extends FrameLayout implements View.OnClickListener {

    public static final int DEFAULT_ANIMATION_TIME = 300;

    private View mExpandableView;

    private boolean mCollapsed = true;

    private int maxExpandableHeight;

    private boolean mAnimating;

    private int mAnimDuration;

    private boolean mIsAllowClickCollapsed;

    private OnExpandStateChangeListener mListener;

    private int mPosition;

    private SparseArray<Boolean> mCollapsedStatus;

    public ExpandableLayout(Context context) {
        this(context, null);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
        try {
            mAnimDuration = attributes.getInt(
                    R.styleable.ExpandableLayout_animDuration, DEFAULT_ANIMATION_TIME);

            mIsAllowClickCollapsed = attributes.getBoolean(
                    R.styleable.ExpandableLayout_isAllowClickCollapsed, false);
        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
        if (mIsAllowClickCollapsed) {
            mExpandableView.setOnClickListener(this);
        }
    }

    private void findViews() {
        int childCount = getChildCount();
        if (childCount != 1) {
            throw new RuntimeException("Only one child View");
        }
        mExpandableView = getChildAt(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getVisibility() == View.GONE || mAnimating) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mExpandableView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        maxExpandableHeight = mExpandableView.getMeasuredHeight();
        if (mCollapsed) {
            mExpandableView.getLayoutParams().height = 0;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setCollapsed(int position, SparseArray<Boolean> collapsedStatus) {
        this.mPosition = position;
        this.mCollapsedStatus = collapsedStatus;
        mCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        setVisibility(VISIBLE);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    public void toggle() {
        if (mAnimating) {
            return;
        }
        mAnimating = true;
        mCollapsed = !mCollapsed;

        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }

        ExpandCollapseAnimation animation;
        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, maxExpandableHeight, 0);
        } else {
            setVisibility(View.VISIBLE);
            animation = new ExpandCollapseAnimation(this, 0, maxExpandableHeight);
        }
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
                mAnimating = false;
                if (mCollapsed) {
                    setVisibility(View.GONE);
                }

                if (mListener != null) {
                    mListener.onExpandStateChanged(mExpandableView, !mCollapsed);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        clearAnimation();
        startAnimation(animation);
    }

    class ExpandCollapseAnimation extends Animation {

        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int curHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mExpandableView.getLayoutParams().height = curHeight;
            mTargetView.getLayoutParams().height = curHeight;
            mTargetView.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    public interface OnExpandStateChangeListener {
        void onExpandStateChanged(View expandableView, boolean isExpanded);
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    public void setIsAllowClickCollapsed(boolean mIsAllowClickCollapsed) {
        this.mIsAllowClickCollapsed = mIsAllowClickCollapsed;
    }

    public void setAnimDuration(int mAnimDuration) {
        this.mAnimDuration = mAnimDuration;
    }
}
