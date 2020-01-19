package com.whats.status.downloader.saver.injection.component;


import com.whats.status.downloader.saver.data.local.FileHelper;
import com.whats.status.downloader.saver.injection.module.AppModule;
import com.whats.status.downloader.saver.ui.imageslider.ImageSliderActivity;
import com.whats.status.downloader.saver.ui.imageslider.imagedetails.ImageDetailsFragment;
import com.whats.status.downloader.saver.ui.main.MainActivity;
import com.whats.status.downloader.saver.ui.main.recentscreen.RecentPicsFragment;
import com.whats.status.downloader.saver.ui.main.saved.SavedPicsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by shaz on 14/2/17.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(RecentPicsFragment fragment);
    void inject(SavedPicsFragment fragment);
    void inject(ImageSliderActivity activity);
    void inject(ImageDetailsFragment fragment);
    FileHelper fileHelper();
}
