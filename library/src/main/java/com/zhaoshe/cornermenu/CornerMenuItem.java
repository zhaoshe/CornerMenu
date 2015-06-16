package com.zhaoshe.cornermenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

/**
 * 这是菜单 item，如果有奇葩需求，请继承这个类实现
 * Created by august on 6/14/15.
 */
public class CornerMenuItem extends FrameLayout {

    private int mItemId;
    private Animation mOpenAnim;
    private Animation mCloseAnim;
    private Animation mClickAnim;

    private Animation.AnimationListener mOpenAnimListener;
    private Animation.AnimationListener mCloseAnimListener;
    private Animation.AnimationListener mClickAnimListener;

    public CornerMenuItem(Context context, int itemId) {
        super(context);
        mItemId = itemId;
        setupView(context);
    }

    public CornerMenuItem(Context context, AttributeSet attrs, int itemId) {
        super(context, attrs);
        mItemId = itemId;
        setupView(context);
    }

    public CornerMenuItem(Context context, AttributeSet attrs, int defStyleAttr, int itemId) {
        super(context, attrs, defStyleAttr);
        mItemId = itemId;
        setupView(context);
    }

    private void setupView(Context context) {
        setClickable(true);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean performClick() {
        if (isClickable()) {
            startClickAnimation();
        }
        return super.performClick();
    }

    public void startOpenAnimation() {
        setVisibility(VISIBLE);
        clearAnimation();
        if (mOpenAnimListener != null) {
            mOpenAnim.setAnimationListener(mOpenAnimListener);
        }
        startAnimation(mOpenAnim);
    }

    public void startCloseAnimation() {
        setVisibility(VISIBLE);
        clearAnimation();
        if (mCloseAnimListener != null) {
            mCloseAnim.setAnimationListener(mCloseAnimListener);
        }
        startAnimation(mCloseAnim);
    }

    public void startClickAnimation() {
        setVisibility(VISIBLE);
        clearAnimation();
        if (mClickAnimListener != null) {
            mClickAnim.setAnimationListener(mClickAnimListener);
        }
        startAnimation(mClickAnim);
    }

    public void setOpenAnimation(Animation anim) {
        mOpenAnim = anim;
    }

    public void setCloseAnimation(Animation anim) {
        mCloseAnim = anim;
    }

    public void setOnClickAnimation(Animation anim) {
        mClickAnim = anim;
    }

    public void setOpenAnimListener(Animation.AnimationListener listener) {
        mOpenAnimListener = listener;
    }

    public void setCloseAnimListener(Animation.AnimationListener listener) {
        mCloseAnimListener = listener;
    }

    public void setClickAnimListener(Animation.AnimationListener listener) {
        mClickAnimListener = listener;
    }

    public int getItemId() {
        return mItemId;
    }
}
