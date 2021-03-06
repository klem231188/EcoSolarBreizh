package bzh.eco.solar.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bzh.eco.solar.R;
import bzh.eco.solar.fragment.BluetoothConnectionFragment;
import bzh.eco.solar.fragment.DashboardFragmentV2;
import bzh.eco.solar.fragment.FileWriterFragment;
import bzh.eco.solar.fragment.KellyFragment;
import bzh.eco.solar.fragment.MotorFragment;
import bzh.eco.solar.fragment.SolarPanelFragment;
import bzh.eco.solar.model.bluetooth.BluetoothDeviceWrapper;
import bzh.eco.solar.model.voiture.Voiture;
import bzh.eco.solar.service.BluetoothService;
import bzh.eco.solar.service.GPSLocationService;


public class MainActivity
        extends Activity
        implements ActionBar.TabListener, BluetoothConnectionFragment.BluetoothCallback {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    private static final String TAG = "MainActivity";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private Voiture mVoiture;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public MainActivity() {
    }

    // -------------------------------------------------------------------------------------
    // Section : Android Lifecycle Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Désactivation du mode veille
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Activation du ode plein écran
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }

        getFragmentManager().beginTransaction().add(FileWriterFragment.newInstance(), "FileWriter").commit();

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
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();

        // Création du Service GPS
        Intent startGPSLocationService = new Intent(MainActivity.this, GPSLocationService.class);
        startService(startGPSLocationService);

        // Création du modèle de donnée
        mVoiture = Voiture.getInstance();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        Intent stopGPSLocationService = new Intent(MainActivity.this, GPSLocationService.class);
        stopService(stopGPSLocationService);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        initVersionInMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_version) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // -------------------------------------------------------------------------------------
    // Section : ActionBar.TabListener Method(s)
    // -------------------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------------------
    // Section : BluetoothConnectionFragment.BluetoothCallback Method(s)
    // -------------------------------------------------------------------------------------
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
    // Section : Private Method(s)
    // -------------------------------------------------------------------------------------
    private void initVersionInMenu(Menu menu) {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pInfo.versionName;
            menu.findItem(R.id.action_version).setTitle(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Impossible de trouver la version", e);
        }
    }

    // -------------------------------------------------------------------------------------
    // Section : Inner Class(es)
    // -------------------------------------------------------------------------------------
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public static final int BLUETOOTH_CONNECTION_SECTION = 0;

        public static final int DASHBOARD_SECTION = 1;

        public static final int SOLAR_PANEL_SECTION = 2;

        public static final int MOTOR_SECTION = 3;

        public static final int KELLY_SECTION = 4;

        List<Fragment> mFragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
            mFragments.add(BluetoothConnectionFragment.newInstance());
            mFragments.add(DashboardFragmentV2.newInstance());
            mFragments.add(SolarPanelFragment.newInstance());
            mFragments.add(MotorFragment.newInstance());
            mFragments.add(KellyFragment.newInstance());
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
                case DASHBOARD_SECTION:
                    return getString(R.string.title_dashboard_section).toUpperCase(locale);
                case SOLAR_PANEL_SECTION:
                    return getString(R.string.title_solar_panel_electical_power_section).toUpperCase(locale);
                case MOTOR_SECTION:
                    return getString(R.string.title_motor_section).toUpperCase(locale);
                case KELLY_SECTION:
                    return getString(R.string.title_kelly_section).toUpperCase(locale);
            }

            return null;
        }
    }

}
