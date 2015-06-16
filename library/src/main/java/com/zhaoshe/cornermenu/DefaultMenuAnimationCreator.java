package com.zhaoshe.cornermenu;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

/**
 * Created by august on 6/14/15.
 */
public class DefaultMenuAnimationCreator implements IMenuAnimationCreator {
    @Override
    public Animation createMainButtonOpenAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.default_menu_button_open);
    }

    @Override
    public Animation createMainButtonCloseAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.default_menu_button_close);
    }

    @Override
    public Animation createMenuItemOpenAnimation(Context context, int index, int fromX, int fromY) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(300);
        animationSet.setStartOffset(index * 100);

        TranslateAnimation transAnim = new TranslateAnimation(fromX, 0, fromY, 0);
        animationSet.addAnimation(transAnim);

        return animationSet;
    }

    @Override
    public Animation createMenuItemCloseAnimation(Context context, int index, int toX, int toY) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(300);
        animationSet.setStartOffset(index * 100);

        TranslateAnimation transAnim = new TranslateAnimation(0, toX, 0, toY);
        animationSet.addAnimation(transAnim);

        return animationSet;
    }

    @Override
    public Animation createMenuItemClickAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.default_menu_item_click);
    }
}
