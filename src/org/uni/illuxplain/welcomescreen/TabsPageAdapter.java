package org.uni.illuxplain.welcomescreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPageAdapter extends FragmentPagerAdapter {
	
	private String[] tabs = { "Home", "Profile","Friends" };
  
	public TabsPageAdapter(FragmentManager fm) {
		super(fm);
	}

    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            return new HomeFragment();
        case 1:
            return new ProfileFragment();
            
        case 2:
        	return new FriendsFragment();
        }
        
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return tabs.length;
    }
    
    @Override
	public CharSequence getPageTitle(int position) {
		return tabs[position];
	}

}
