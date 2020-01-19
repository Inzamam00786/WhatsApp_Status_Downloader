package com.whats.status.downloader.saver;

import android.app.Application;

import com.whats.status.downloader.saver.injection.component.AppComponent;
import com.whats.status.downloader.saver.injection.component.DaggerAppComponent;
import com.whats.status.downloader.saver.injection.module.AppModule;


public class TheApplication extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}