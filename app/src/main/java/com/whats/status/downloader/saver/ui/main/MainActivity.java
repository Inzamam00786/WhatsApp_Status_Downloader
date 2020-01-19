package com.whats.status.downloader.saver.ui.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.whats.status.downloader.saver.save.audios.images.BuildConfig;
import com.whats.status.downloader.saver.save.audios.images.R;
import com.whats.status.downloader.saver.ui.base.BaseActivity;
import com.whats.status.downloader.saver.ui.main.recentscreen.RecentPicsFragment;
import com.whats.status.downloader.saver.ui.main.saved.SavedPicsFragment;
import com.whats.status.downloader.saver.util.DialogFactory;
import com.whats.status.downloader.saver.util.PermissionUtil;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView, NavigationView.OnNavigationItemSelectedListener{

    private static final int PERMISSION_REQUEST_CODE_EXT_STORAGE = 10;
    @Inject
    MainPresenter presenter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    CustomPagerAdapter pagerAdapter;
     AdView adView;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    final Context context = this;
    private DrawerLayout drawerLayout;
    ImageView draw_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheApplication().getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Setup toolbar
        toolbar.setSubtitle(getResources().getString(R.string.sub_title));
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_layout);


        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle= new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Attach presenter
        presenter.attachView(this);


        presenter.setLoadingAnimation(true);

        // Load images
        if (!PermissionUtil.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermission();
        }else {
            presenter.loadRecentAndSavedPics();
        }

        adView = findViewById(R.id.adView);
         adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.original_interstitial));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if(position == 1)

                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                });
            }
        });


    }

@Override
public void onBackPressed() {

    if (drawerLayout.isDrawerOpen(GravityCompat.START))
    {
        drawerLayout.closeDrawer(GravityCompat.START);
    }else {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        super.onBackPressed();
    }

}
    private void requestPermission() {
        // TODO: 5/3/17 check permission rational
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionUtil.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE_EXT_STORAGE);
    }

    @Override
    public void displayWelcomeMessage(String msg) {
    }

    @Override
    public void displayLoadingAnimation(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayRecentAndSavedPics() {
        presenter.setLoadingAnimation(false);

        // Setup tabs
        pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_EXT_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                presenter.loadRecentAndSavedPics();
            }else{
                // Permission denied, show rational
                if (PermissionUtil.shouldShowRational(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    DialogFactory
                            .createSimpleOkErrorDialog(this, "Access required", "Permission to access local files is required for the app to perform as intended.",
                                    new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    requestPermission();
                                }
                            })
                            .show();
                }else{
                    // Exit maybe?
                }
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.share:
//                Toast.makeText(getApplicationContext(),"Share this app",Toast.LENGTH_SHORT).show();
                try {
                    Intent link = new Intent(Intent.ACTION_SEND);
                    link.setType("text/plain");
                    link.putExtra(Intent.EXTRA_SUBJECT, "Whatsapp Status Downloader");
                    String sAux = "Let me recommend you this application ";
                    sAux = sAux + "https://play.google.com/store/apps/details?id="+getPackageName();
                    link.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(link, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

                break;
            case R.id.more:
//                Toast.makeText(this, "More Apps", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.wastatus.downloaderfree.waimagesaver");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                break;
            case R.id.rate:
//                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show();
                Uri uri2 = Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName());
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                startActivity(intent2);

                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public class CustomPagerAdapter extends FragmentStatePagerAdapter {

        private String[] tabTitles = new String[]{"Recent", "Saved"};

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: return RecentPicsFragment.newInstance();
                case 1:

                    return SavedPicsFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.side_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_settings:

                    if (item.isChecked()) {


                        item.setChecked(false);

                    } else {
                        item.setChecked(true);

                        File source = new File(Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses");
                        File dest = new File(Environment.getExternalStorageDirectory().toString() + "/StatusSaver");
                        try {
                            FileUtils.copyDirectory(source, dest);
                            Toast.makeText(getApplicationContext(), "Files Saved Successfully", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Exceptions is" + e, Toast.LENGTH_SHORT).show();
                        }
                    }

                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }


}
