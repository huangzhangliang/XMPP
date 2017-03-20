package com.android.xlibrary.FlyTabLayout;

import com.android.xlibrary.FlyTabLayout.listener.CustomTabEntity;

/**
 * Created by leon on 16/10/25.
 */

public class TabBean implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabBean(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    public String getTabTitle() {
        return title;
    }

    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
