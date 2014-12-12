package bzh.eco.solar.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bzh.eco.solar.R;
import bzh.eco.solar.fragment.BluetoothConnectionFragment;
import bzh.eco.solar.fragment.BluetoothDisplayFragment;
import bzh.eco.solar.model.BluetoothDeviceWrapper;
import bzh.eco.solar.service.BluetoothService;


public class MainActivity
        extends Activity
        implements ActionBar.TabListener, BluetoothConnectionFragment.BluetoothCallback, BluetoothDisplayFragment.BluetoothCallback {

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    // -------------------------------------------------------------------------------------
    // Section : @Override Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onBluetoothConnecting(BluetoothDeviceWrapper device) {
        Intent startBluetoothServiceIntent = new Intent(this, BluetoothService.class);
        startBluetoothServiceIntent.putExtra(BluetoothService.EXTRA_DEVICE, device);
        startService(startBluetoothServiceIntent);
    }

    @Override
    public void onBluetoothDisconnecting() {
        Intent stopBluetoothServiceIntent = new Intent(this, BluetoothService.class);
        stopService(stopBluetoothServiceIntent);
    }

    // -------------------------------------------------------------------------------------
    // Section : Inner Class(es)
    // -------------------------------------------------------------------------------------
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public static final int BLUETOOTH_CONNECTION_SECTION = 0;

        public static final int BLUETOOTH_DISPLAY_SECTION = 1;

        List<Fragment> mFragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
            mFragments.add(BluetoothConnectionFragment.newInstance());
            mFragments.add(BluetoothDisplayFragment.newInstance());
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position >= 0 && position < mFragments.size()) {
                fragment = mFragments.get(position);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale locale = Locale.getDefault();
            switch (position) {
                case BLUETOOTH_CONNECTION_SECTION:
                    return getString(R.string.title_bluetooth_connection_section).toUpperCase(locale);
                case BLUETOOTH_DISPLAY_SECTION:
                    return getString(R.string.title_bluetooth_display_section).toUpperCase(locale);
            }

            return null;
        }
    }
}