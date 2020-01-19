package com.whats.status.downloader.saver.ui.imageslider.imagedetails;


import com.whats.status.downloader.saver.ui.base.MvpView;

/**
 * Created by shaz on 14/2/17.
 */

public interface ImageDetailsView extends MvpView {
    void displayLoadingAnimation(boolean status);
    void displayImageSavedMsg();
    void displayDeleteSuccessMsg();
}
