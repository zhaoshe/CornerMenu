package com.zhaoshe.cornermenu;

import android.content.Context;
import android.view.animation.Animation;

/**
 * 各个视图的动画的创建者
 * Created by august on 6/14/15.
 */
public interface IMenuAnimationCreator {
    /**
     * 创建菜单打开时主按钮的动画
     * @param context
     * @return
     */
    Animation createMainButtonOpenAnimation(Context context);

    /**
     * 创建菜单关闭时主按钮的动画
     * @param context
     * @return
     */
    Animation createMainButtonCloseAnimation(Context context);

    /**
     * 创建当菜单打开时某个菜单按钮的动画
     * @param context 上下文
     * @param index 横向/纵向 第几个
     * @param fromX 一定要包含位移动画，x位置从 fromX 到 0 移动
     * @param fromY 一定要包含位移动画，y位置从 fromY 到 0 移动
     * @return
     */
    Animation createMenuItemOpenAnimation(Context context, int index, int fromX, int fromY);

    /**
     * 创建当菜单关闭是某个菜单按钮的动画
     * @param context 上下文
     * @param index 横向/纵向 第几个
     * @param toX 一定要包含位移动画，x位置从 0 到 toX 移动
     * @param toY 一定要包含位移动画，y位置从 0 到 toY 移动
     * @return
     */
    Animation createMenuItemCloseAnimation(Context context, int index, int toX, int toY);

    /**
     * 创建当菜单中的按钮被点击时，按钮的动画
     * @param context
     * @return
     */
    Animation createMenuItemClickAnimation(Context context);

}
