package com.zhaoshe.cornermenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

/**
 * Created by august on 6/15/15.
 */
public class DefaultMenuItem extends CornerMenuItem {
    public DefaultMenuItem(Context context, int itemId) {
        super(context, itemId);
        setupView(context, null, 0);
    }

    public DefaultMenuItem(Context context, AttributeSet attrs, int itemId) {
        super(context, attrs, itemId);
        setupView(context, attrs, 0);
    }

    public DefaultMenuItem(Context context, AttributeSet attrs, int defStyleAttr, int itemId) {
        super(context, attrs, defStyleAttr, itemId);
        setupView(context, attrs, defStyleAttr);
    }

    private void setupView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.default_menu_item, this, true);
    }


}
