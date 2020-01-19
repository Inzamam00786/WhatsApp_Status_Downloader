package com.whats.status.downloader.saver.ui.imageslider;


import com.whats.status.downloader.saver.data.model.ImageModel;
import com.whats.status.downloader.saver.ui.base.MvpView;

import java.util.List;

/**
 * Created by shaz on 14/2/17.
 */

public interface ImageSliderView extends MvpView {
    void displayLoadingAnimation(boolean status);
    void displayImageSlider(List<ImageModel> mediaItems, int imageToDisplayPosition);
}
