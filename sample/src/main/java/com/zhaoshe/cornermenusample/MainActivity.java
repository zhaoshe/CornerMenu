package com.zhaoshe.cornermenusample;

import android.app.Activity;
import android.os.Bundle;

import com.zhaoshe.cornermenu.CornerMenu;
import com.zhaoshe.cornermenu.CornerMenuItem;
import com.zhaoshe.cornermenu.DefaultMenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CornerMenu menu = (CornerMenu) findViewById(R.id.corner_menu);
        setupMenu(menu);
    }

    private void setupMenu(CornerMenu menu) {
        List<CornerMenuItem> vItems = new ArrayList<>();
        List<CornerMenuItem> hItems = new ArrayList<>();

        CornerMenuItem vItem0 = new DefaultMenuItem(this, 0);
        CornerMenuItem vItem1 = new DefaultMenuItem(this, 1);

        CornerMenuItem hItem0 = new DefaultMenuItem(this, 0);
        CornerMenuItem hItem1 = new DefaultMenuItem(this, 1);

        vItems.add(vItem0);
        vItems.add(vItem1);

        hItems.add(hItem0);
        hItems.add(hItem1);

        menu.addVerticalItems(vItems);
        menu.addHorizontalItems(hItems);
    }
}
