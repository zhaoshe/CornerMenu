package com.zhaoshe.cornermenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by august on 6/14/15.
 */
public class CornerMenu extends FrameLayout implements View.OnClickListener {
    public static final int LEFT_TOP = Gravity.LEFT | Gravity.TOP;
    public static final int LEFT_BOTTOM = Gravity.LEFT | Gravity.BOTTOM;
    public static final int RIGHT_TOP = Gravity.RIGHT | Gravity.TOP;
    public static final int RIGHT_BOTTOM = Gravity.RIGHT | Gravity.BOTTOM;

    private static final int DEFAULT_ITEM_GAP = 30;

    // 主按钮在哪个角落
    private int mMenuGravity = LEFT_TOP;
    // 横向按钮之间的间距
    private int mHorizontalItemGap = DEFAULT_ITEM_GAP;
    // 纵向按钮之间的间距
    private int mVerticalItemGap = DEFAULT_ITEM_GAP;
    // 是否点击菜单 item 的时候自动关闭菜单
    private boolean mCloseMenuOnItemClick = true;

    private OnMenuItemClickListener mOnItemClickListener;

    // 主按钮
    private ImageView mMenuButton;

    private IMenuAnimationCreator mAnimationCreator = new DefaultMenuAnimationCreator();

    private boolean mIsMenuButtonAnimating;
    private boolean mIsMenuOpen;

    private List<CornerMenuItem> mHorizontalMenuItemList = new ArrayList<CornerMenuItem>();
    private List<CornerMenuItem> mVerticalMenuItemList = new ArrayList<CornerMenuItem>();

    Animation.AnimationListener mMenuBtnAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mIsMenuButtonAnimating = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    public CornerMenu(Context context) {
        super(context);
        setupView(context, null, 0);
    }

    public CornerMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context, attrs, 0);
    }

    public CornerMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context, attrs, defStyleAttr);
    }

    private void setupView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.menu, this, true);
        mMenuButton = (ImageView) findViewById(R.id.corner_menu_button);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CornerStaightMenu, defStyleAttr, 0);
            mMenuGravity = typedArray.getInt(R.styleable.CornerStaightMenu_menu_gravity, LEFT_TOP);
            int gap = typedArray.getDimensionPixelSize(R.styleable.CornerStaightMenu_item_gap, DEFAULT_ITEM_GAP);
            int gapH = typedArray.getDimensionPixelSize(R.styleable.CornerStaightMenu_item_gap_horizontal, -1);
            int gapV = typedArray.getDimensionPixelSize(R.styleable.CornerStaightMenu_item_gap_vertical, -1);
            mHorizontalItemGap = mVerticalItemGap = gap;
            if (gapH >= 0) {
                mHorizontalItemGap = gapH;
            }
            if (gapV >= 0) {
                mVerticalItemGap = gapV;
            }

            Drawable drawable = typedArray.getDrawable(R.styleable.CornerStaightMenu_menu_img_src);
            if (drawable != null) {
                mMenuButton.setImageDrawable(drawable);
            }
        }

        mMenuButton.setOnClickListener(this);

        LayoutParams params = (LayoutParams) mMenuButton.getLayoutParams();
        params.gravity = mMenuGravity;
    }

    @Override
    public void onClick(View v) {
        if (v == mMenuButton) {
            switchMenu();
        }
    }

    /**
     * 转换菜单状态
     * 当菜单主按钮没有完成动画的时候，这个方法是不起作用的
     */
    private void switchMenu() {
        if (!mIsMenuButtonAnimating) {
            mIsMenuButtonAnimating = true;

            if (mIsMenuOpen) {
                Animation closeAnimation = mAnimationCreator.createMainButtonCloseAnimation(getContext());
                closeAnimation.setAnimationListener(mMenuBtnAnimListener);
                mMenuButton.clearAnimation();
                mMenuButton.startAnimation(closeAnimation);
                for (CornerMenuItem item : mVerticalMenuItemList) {
                    item.startCloseAnimation();
                }
                for (CornerMenuItem item : mHorizontalMenuItemList) {
                    item.startCloseAnimation();
                }
            } else {
                Animation openAnimation = mAnimationCreator.createMainButtonOpenAnimation(getContext());
                openAnimation.setAnimationListener(mMenuBtnAnimListener);
                mMenuButton.clearAnimation();
                mMenuButton.startAnimation(openAnimation);
                for (CornerMenuItem item : mVerticalMenuItemList) {
                    item.startOpenAnimation();
                }
                for (CornerMenuItem item : mHorizontalMenuItemList) {
                    item.startOpenAnimation();
                }
            }

            mIsMenuOpen = !mIsMenuOpen;
        }
    }

    public void openMenu() {
        if (!mIsMenuOpen) {
            switchMenu();
        }
    }

    public void closeMenu() {
        if (mIsMenuOpen) {
            switchMenu();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = mMenuButton.getMeasuredHeight();
        int width = mMenuButton.getMeasuredWidth();

        int tmpHeight = height; // 找出横向所有 item 中最高的高度
        int tmpWidth = width; // 找出纵向所有 item 中最宽的宽度

        int index = 0;
        for (CornerMenuItem item : mVerticalMenuItemList) {
            // 计算应该摆放的位置
            LayoutParams vParams = new LayoutParams(item.getLayoutParams());
            vParams.gravity = mMenuGravity;

            // 要获取的动画
            Animation openAnim;
            Animation closeAnim;
            Animation clickAnim;

            // 重置 margin 值
            vParams.setMargins(0, 0, 0, 0);
            // 如果是在下面
            if ((mMenuGravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
                vParams.bottomMargin = height + mVerticalItemGap;
                openAnim = mAnimationCreator.createMenuItemOpenAnimation(getContext(), index, 0, vParams.bottomMargin);
                closeAnim = mAnimationCreator.createMenuItemCloseAnimation(getContext(), index, 0, vParams.bottomMargin);
            } else { // 如果是在上面
                vParams.topMargin = height + mVerticalItemGap;
                openAnim = mAnimationCreator.createMenuItemOpenAnimation(getContext(), index, 0, -vParams.topMargin);
                closeAnim = mAnimationCreator.createMenuItemCloseAnimation(getContext(), index, 0, -vParams.topMargin);
            }
            clickAnim = mAnimationCreator.createMenuItemClickAnimation(getContext());

            item.setLayoutParams(vParams);

            // 设置动画
            item.setOpenAnimation(openAnim);
            item.setCloseAnimation(closeAnim);
            item.setOnClickAnimation(clickAnim);

            item.setVisibility(INVISIBLE);

            // 计算整个布局的高度
            height += item.getMeasuredHeight() + mVerticalItemGap;
            final int itemWidth = item.getMeasuredWidth();
            if (tmpWidth < itemWidth) {
                tmpWidth = itemWidth;
            }

            ++index;
        }

        index = 0;
        for (CornerMenuItem item : mHorizontalMenuItemList) {
            // 计算应该摆放的位置
            LayoutParams hParams = (LayoutParams) item.getLayoutParams();
            hParams.gravity = mMenuGravity;

            // 要获取的动画
            Animation openAnim;
            Animation closeAnim;
            Animation clickAnim;

            // 重置 margin 值
            hParams.setMargins(0, 0, 0, 0);
            // 如果是在左边
            if ((mMenuGravity & Gravity.LEFT) == Gravity.LEFT) {
                hParams.leftMargin = width + mHorizontalItemGap;
                // 设置动画
                openAnim = mAnimationCreator.createMenuItemOpenAnimation(getContext(), index, -hParams.leftMargin, 0);
                closeAnim = mAnimationCreator.createMenuItemCloseAnimation(getContext(), index, -hParams.leftMargin, 0);
            } else { // 如果是在右边
                hParams.rightMargin = width + mHorizontalItemGap;
                // 设置动画
                openAnim = mAnimationCreator.createMenuItemOpenAnimation(getContext(), index, hParams.rightMargin, 0);
                closeAnim = mAnimationCreator.createMenuItemCloseAnimation(getContext(), index, hParams.rightMargin, 0);
            }
            clickAnim = mAnimationCreator.createMenuItemClickAnimation(getContext());

            item.setLayoutParams(hParams);

            // 设置动画
            item.setOpenAnimation(openAnim);
            item.setCloseAnimation(closeAnim);
            item.setOnClickAnimation(clickAnim);

            item.setVisibility(INVISIBLE);

            // 计算整个布局的宽度
            width += item.getMeasuredWidth() + mHorizontalItemGap;
            final int itemHeight = item.getMeasuredHeight();
            if (tmpHeight < itemHeight) {
                tmpHeight = itemHeight;
            }

            ++index;
        }

        // 确认宽高，重新 measure
        if (height < tmpHeight) {
            height = tmpHeight;
        }

        if (width < tmpWidth) {
            width = tmpWidth;
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 向横向列表增加一些菜单按钮
     *
     * @param items
     */
    public void addHorizontalItems(List<CornerMenuItem> items) {
        // 移除主按钮
        this.removeView(mMenuButton);
        // 添加新的横向列表按钮
        for (CornerMenuItem item : items) {
            addView(item);

            item.setOpenAnimListener(new ItemAnimationListener(item, true));
            item.setCloseAnimListener(new ItemAnimationListener(item, false));
            item.setClickAnimListener(new ItemClickAnimListener(item.getItemId()));
        }

        // 将新添加的按钮加入列表中
        mHorizontalMenuItemList.addAll(items);

        // 加回主按钮
        this.addView(mMenuButton);
    }

    /**
     * 向纵向列表增加一些菜单按钮
     *
     * @param items
     */
    public void addVerticalItems(List<CornerMenuItem> items) {
        // 移除主按钮
        this.removeView(mMenuButton);
        // 添加新的横向列表按钮
        for (CornerMenuItem item : items) {
            addView(item);
            item.setOpenAnimListener(new ItemAnimationListener(item, true));
            item.setCloseAnimListener(new ItemAnimationListener(item, false));
            item.setClickAnimListener(new ItemClickAnimListener(item.getItemId()));
        }

        // 将新添加的按钮加入列表中
        mVerticalMenuItemList.addAll(items);

        // 加回主按钮
        this.addView(mMenuButton);
    }

    /**
     * 设置菜单朝向
     * @param gravity 取值必须是{@link #LEFT_TOP}、{@link #LEFT_BOTTOM}、{@link #RIGHT_TOP}、{@link #RIGHT_BOTTOM}其中一个
     */
    public void setmMenuGravity(int gravity) {
        if (gravity != LEFT_TOP && gravity != LEFT_BOTTOM && gravity != RIGHT_TOP && gravity != RIGHT_BOTTOM) {
            throw new IllegalArgumentException();
        }
        mMenuGravity = gravity;
    }

    /**
     * 设置菜单 item 之间的间距
     * @param horizontalGap 横向间距
     * @param verticalGap 纵向间距
     */
    public void setItemGap(int horizontalGap, int verticalGap) {
        mHorizontalItemGap = horizontalGap;
        mVerticalItemGap = verticalGap;
    }

    /**
     * 设置是否在点击菜单 item 的时候关闭菜单
     * @param b
     */
    public void setCloseOnItemClick(boolean b) {
        mCloseMenuOnItemClick = b;
    }

    /**
     * 获取是否会在点击菜单 item 的时候关闭菜单
     * @return
     */
    public boolean willCloseOnItemClick() {
        return mCloseMenuOnItemClick;
    }

    /**
     * 设置菜单中各个元素的动画的生成器
     * @param animationCreator
     */
    public void setAnimationCreator(IMenuAnimationCreator animationCreator) {
        mAnimationCreator = animationCreator;
    }

    /**
     * 设置当菜单 item 被点击事件监听器
     * @param listener
     */
    public void setOnItemClickListener(OnMenuItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 设置主按钮的显示图片
     * @param resId
     */
    public void setMenuButtonImage(int resId) {
        mMenuButton.setImageResource(resId);
    }

    /**
     * 设置主按钮的显示图片
     * @param drawable
     */
    public void setMenuButtonImage(Drawable drawable) {
        mMenuButton.setImageDrawable(drawable);
    }

    /**
     * 移除掉某个菜单 item
     * @param item
     */
    public void removeItem(CornerMenuItem item) {
        for (CornerMenuItem tmpItem : mVerticalMenuItemList) {
            if (item == tmpItem) {
                mVerticalMenuItemList.remove(item);
                removeView(item);
                return;
            }
        }

        for (CornerMenuItem tmpItem : mHorizontalMenuItemList) {
            if (item == tmpItem) {
                mHorizontalMenuItemList.remove(item);
                removeView(item);
                return;
            }
        }
    }

    /**
     * 移除掉横向的某个 item
     * @param index item 的位置
     */
    public void removeHorizontalItem(int index) {
        if (mHorizontalMenuItemList.size() > index) {
            CornerMenuItem item = mHorizontalMenuItemList.remove(index);
            removeView(item);
        }
    }

    /**
     * 移除掉纵向的某个 item
     * @param index item 的位置
     */
    public void removeVerticalItem(int index) {
        if (mVerticalMenuItemList.size() > index) {
            CornerMenuItem item = mVerticalMenuItemList.remove(index);
            removeView(item);
        }
    }

    /**
     * 移除掉所有的 item
     */
    public void cleanupItems() {
        mVerticalMenuItemList.clear();
        mHorizontalMenuItemList.clear();
        removeAllViews();
        addView(mMenuButton);
    }

    /**
     * 菜单 item 点击动画监听器
     */
    private class ItemClickAnimListener implements Animation.AnimationListener {
        private int mItemId;
        private boolean mHasStart; // 不知道为什么一个 start 会有两个 end
        public ItemClickAnimListener(int itemId) {
            mItemId = itemId;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            mHasStart = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (!mHasStart) {
                return;
            }
            mHasStart = false;

            if (mCloseMenuOnItemClick) {
                closeMenu();
            }
            mOnItemClickListener.onItemClick(mItemId);
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * 菜单 item 做展开/收缩动画的 监听器
     */
    private class ItemAnimationListener implements Animation.AnimationListener {
        private CornerMenuItem mItem;
        private boolean mIsOpen;
        public ItemAnimationListener(CornerMenuItem item, boolean isOpen) {
            mItem = item;
            mIsOpen = isOpen;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            mItem.setVisibility(VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mIsOpen) {
                mItem.setVisibility(VISIBLE);
            } else {
                mItem.setVisibility(GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * 菜单按钮被点击的回调接口
     */
    public interface OnMenuItemClickListener {
        void onItemClick(int itemId);
    }
}
