package com.whats.status.downloader.saver.ui.main;


import com.whats.status.downloader.saver.ui.base.MvpView;

/**
 * Created by shaz on 14/2/17.
 */

public interface MainView extends MvpView {
    void displayWelcomeMessage(String msg);
    void displayLoadingAnimation(boolean status);
    void displayRecentAndSavedPics();
}
